<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      
      <el-form-item label="会员手机号" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入会员手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务人员手机号" prop="userPhone">
        <el-input
          v-model="queryParams.userPhone"
          placeholder="请输入服务人员手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="分配标志" prop="distributeFlag">
            <el-select
              v-model="queryParams.distributeFlag"
              placeholder="全部"
              clearable
              style="width: 200px"
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

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:member:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:member:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:member:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:member:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->

    <el-table v-loading="loading" :data="memberList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="会员手机号" align="center" prop="phone" />
      <el-table-column label="会员编号" align="center" prop="no" />
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
              v-hasPermi="['system:member:query']"
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

    <!-- 添加或修改会员对话框 -->
    <!-- <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="会员编号" prop="no">
          <el-input v-model="form.no" placeholder="请输入会员编号" />
        </el-form-item>
        <el-form-item label="会员手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入会员手机号" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="分配标志" prop="distributeFlag">
          <el-input v-model="form.distributeFlag" placeholder="请输入分配标志" />
        </el-form-item>
        <el-form-item label="运营者id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入运营者id" />
        </el-form-item>
        <el-form-item label="运营者姓名" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入运营者姓名" />
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog> -->
    <!-- 添加或修改机构对话框 -->
    <form-panel :title="title" :visible.sync="open" width="600px" append-to-body >
      <h4 class="form-header h4">会员基本信息</h4>
		<el-row>
		  <member-info-card :value="form.memberId"></member-info-card>
		</el-row>	  
    


    <!-- 先用表格代替卡片组 -->
    <el-card class="box-card">

    <h3 class="form-header h4">家庭组信息</h3>
    <el-table v-loading="familyLoading" :data="familyList" >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="家庭名称：" align="center" prop="name" />
      <el-table-column label="家庭地址：" align="center" prop="address" />
      <el-table-column label="共享标志：" align="center" prop="shareFlag">
        <template slot-scope="scope">
          {{ shareFlagFormat(scope.row) }}
        </template>
      </el-table-column>
      <!-- <el-table-column label="" align="center" prop="userName" />
      <el-table-column label="设备名称：" align="center" prop="userPhone" />
      <el-table-column label="位置：" align="center" prop="createTime" />
      <el-table-column label="状态：" align="center" prop="createTime" /> -->
    </el-table>
    <pagination
      v-show="familyTotal>0"
      :total="familyTotal"
      :page.sync="queryFamilyParams.pageNum"
      :limit.sync="queryFamilyParams.pageSize"
      @pagination="familyList"
    />
    </el-card>
    </form-panel>
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

      //家庭设备组表格数据
      // 遮罩层
      familyTotal:0,
      familyLoading: true,
      familyList: [],
      queryFamilyParams: {
        pageNum: 1,
        pageSize: 10,
        memberId: 100,

      },


      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        phone: null,
        userPhone: null,
        distributeFlag: null,
      },
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
    //页面默认查询未分配
    // this.queryParams.distributeFlag = this.dict.type.sys_distribute_flag[0].key
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

    /** 查询家庭组信息列表 */
    // getfamilyList() {
    //   this.familyLoading = true;
    //   listFamily(this.queryFamilyParams).then(response => {
    //     this.familyList = response.rows;
    //     this.familyTotal = response.total;
    //     this.familyLoading = false;
    //   });
    // },

    //字典翻译部分
     // 分配标志字典翻译
    distributeFlagFormat(row, column) {
      return this.selectDictLabel(this.dict.type.sys_distribute_flag, row.distributeFlagFormat)
    },

    // 共享标志字典翻译
    shareFlagFormat(row, column) {
      return this.selectDictLabel(this.dict.type.sys_share_flag, row.shareFlagFormat)
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

    //  /** 修改页面路由跳转操作 */
    //  handleAuthRole: function(row) {
    //   // const userId = row.userId;
    //   // this.$router.push("/system/user-auth/role/" + userId);
    //   this.$router.push({path:'/member/memberTest', query: {memberId: 101}});

    // },

       /** 查看按钮操作 */
       handleView(row) {
        this.reset();
      const memberId = row.memberId || this.ids
      getMember(memberId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "查看会员";
      });
      //家庭组信息查询触发
      this.familyLoading = true;
      listFamily(this.queryFamilyParams).then(response => {
      //listFamily(memberId).then(response => {

        this.familyList = response.rows;
        this.familyTotal = response.total;
        this.familyLoading = false;
      });

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
