<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="125px">

      <el-form-item label="会员手机号" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入会员手机号"
          clearable
          style="width: 220px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务人员手机号" prop="params.userMobilePhone">
        <el-input
          v-model="queryParams.params.userMobilePhone"
          placeholder="请输入服务人员手机号"
          clearable
          style="width: 220px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="分配标志" prop="distributeFlag">
            <el-select
              v-model="queryParams.distributeFlag"
              placeholder="全部"
              clearable
              style="width: 220px"
            >
              <el-option
                v-for="dict in dict.type.sys_distribute_flag"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
        </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="20"></el-row>
 <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>


    <el-table v-loading="loading" :data="memberList" @selection-change="handleSelectionChange">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="会员手机号" align="center" prop="phone" />
      <!-- <el-table-column label="会员编号" align="center" prop="no" show-overflow-tooltip/> -->
      <el-table-column label="分配标志" align="center" prop="distributeFlag">
        <template slot-scope="scope">
          {{ distributeFlagFormat(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column label="服务人员" align="center" prop="userName" />
      <!-- 修改 -->
      <el-table-column label="服务人员手机号" align="center" prop="userPhone" />
      <el-table-column label="注册时间" align="center" prop="createTime" />

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-info"
              @click="handleView(scope.row)"
              v-hasPermi="['member:query']"
            >查看</el-button>
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:member:remove']"
          >删除</el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />


    <form-panel :title="title" :visible.sync="open" width="600px" append-to-body >
      <h4 class="form-header h4">会员基本信息</h4>
        `<el-row>
          <member-info-card :value="this.memberId"></member-info-card>
        </el-row>

    <!-- 瀑布流布局 -->
      <el-row>
        <el-card>
          <div slot="header" class="clearfix">
          <span class="el-dialog__title">家庭组信息</span>
          </div>
          <water-fall :value="familyGroupList" :columnNumber="3">
            <template slot-scope="{item}">
              <el-card shadow="hover" class="card-item card-item-click" >
                {{ shareFlagFormat(item.shareFlag)  == "共享"?"创建人:"+ item.createMemberName :"" }}
                <br>
                家庭名称： {{ item.name }}
                <br>
                家庭地址：{{ item.address}}
                <br>
                共享标志：{{ shareFlagFormat(item.shareFlag)}}
                <br>
                <br>
                    <water-fall :value="item.devices" :columnNumber="2">
                      <template slot-scope="{item}">
                        <!-- 点击事件添加，跳转单个设备消息查询页面 -->
                        <el-card shadow="hover" class="card-item card-item-cp" @click.native="goSingleDeviceRB(item.deviceId,item.memberId)">
                            <br>
                            {{item.name}}
                            <br>
                            <!-- 待调整 --> 
                            <el-row>
                            {{JSON.parse(item.nowLoacation).address}} | {{item.onlineFlag=='1'?"在线":"不在线"}}
                            </el-row>
                            <br>
                            <br>
                        </el-card>
                      </template>
                    </water-fall>
                </el-card>
                <br>
            </template>
          </water-fall>
        </el-card>
      </el-row>
    </form-panel>`
  </div>
</template>

<script>
import { listMember, getMember,listFamily } from "@/api/member/member";
import MemberInfoCard from "@/views/member/components/MemberInfoCard";

export default {
  name: "Member",
  components:{MemberInfoCard},
  dicts: ['sys_distribute_flag','sys_share_flag'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 会员表格数据
      memberList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        phone: null,
        distributeFlag: "0",
        params:{
          userMobilePhone:null
        }
      },
      // 家庭组列表
      familyGroupList:[],
      familyTotal:0,
      // 遮罩层
      familyLoading: true,
      queryFamilyParams: {
        pageNum: 1,
        pageSize: 50,
        memberId: 0,

      },
      memberId:0,

      // 表单参数
      form: {
      },
      // 表单校验
      rules: {
        phone: [
          // { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ],
        userPhone: [
          // { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      }
    };
  },
  //页面初始化
  created() {
    this.getList();
  },
  methods: {
    /** 查询会员列表 */
    getList() {
      this.loading = true;
      listMember(this.queryParams).then(response => {
        this.memberList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // getJSON(){
    //   const result = JSON.parse(item.nowLoacation)
    // //console.log("result:========="+result) 
    // /** 查询家庭组列表 */
    // },
    init(){
      this.familyGroupList = [];
      this.queryFamilyParams.memberId=this.memberId
      listFamily(this.queryFamilyParams).then(response => {
      this.familyGroupList = response.rows;
      //console.log(this.familyGroupList + ">>>>>" + ">>>>>>>" )

      });
    },
    //字典翻译部分
     // 分配标志字典翻译
    distributeFlagFormat(row, column) {
      return this.selectDictLabel(this.dict.type.sys_distribute_flag, row.distributeFlag)
    },

    // 共享标志字典翻译
    shareFlagFormat(column) {
      return this.selectDictLabel(this.dict.type.sys_share_flag, column)
    },

    //页面路由跳转
    //待分配运营的会员
    goSingleDeviceRB(deviceId,memberId){
      this.$router.push({path:'/members/singleDeviceRB',query: {deviceId: deviceId, memberId: memberId}})
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        memberId: null,
        no: null,
        phone: null,
        password: null,
        distributeFlag: null,
        userId: null,
        userName: null,
        delFlag: null,
        createTime: null,
        updateTime: null
      };
      this.resetForm("form");
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.memberId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加会员";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const memberId = row.memberId || this.ids
      getMember(memberId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改会员";
      });
    },



    /** 查看按钮操作 */
    handleView(row) {
        //this.reset();
       this.open = true;
       this.title = "会员名下所有设备总览";
       this.memberId = row.memberId
      //家庭组信息查询触发
       this.init();
    },


    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.memberId != null) {
            updateMember(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMember(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const memberIds = row.memberId || this.ids;
      this.$modal.confirm('是否确认删除会员编号为"' + memberIds + '"的数据项？').then(function() {
        return delMember(memberIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/member/export', {
        ...this.queryParams
      }, `member_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style land="css">.el-tooltip__popper{max-width:50%}
</style>
<style scoped lang="scss">

.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

.card-item card-item-cp{
  min-height: 20px;
  height: 100;
}
  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

<style scoped lang="scss">

.el-row {
  margin-bottom: 30px;

  &
  :last-child {
    margin-bottom: 0;
  }
}
.el-card {
    margin-top: 20px;
    border-radius: 4px;
    border: 1px solid #e6ebf5;
    background-color: #FFFFFF;
    overflow: hidden;
    color: #303133;
    -webkit-transition: 0.3s;
    transition: 0.3s;
}
</style>
