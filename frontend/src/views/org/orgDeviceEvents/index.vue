<template>
    <div class="app-container">

      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="85px">


        <el-form-item label="处理人员" prop="userName">
          <el-input
            v-model="queryParams.userName"
            placeholder="处理人员"
            clearable
            style="width: 240px"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="查询时间">
            <el-date-picker
              v-model="dateRange"
              style="width: 240px"
              value-format="yyyy-MM-dd HH:mm:ss"
              type="datetimerange"
              range-separator="-"
              start-placeholder="开始查询时间"
              end-placeholder="结束查询时间"
            ></el-date-picker>
          </el-form-item>

        <el-form-item label="处理标志" prop="operateFlag">
            <el-select
              v-model="queryParams.operateFlag"
              placeholder="处理标志"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in dict.type.sys_operate_flag"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
        </el-form-item>

         <el-form-item label="重要级别" prop="level">
          <el-select
              v-model="queryParams.level"
              placeholder="重要级别"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in dict.type.event_level"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
        </el-form-item>

        <el-form-item label="设备名称" prop="deviceName">
          <el-input
            v-model="queryParams.deviceName"
            placeholder="请输入设备名称"
            clearable
            style="width: 240px"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>

        <!-- <el-form-item label="归属机构" prop="orgId">
            <treeselect style="width: 240px" v-model="queryParams.orgId" :options="orgOptions" :show-count="true" placeholder="请选择归属机构" />
          </el-form-item> -->
          <el-form-item label="设备编号" prop="deviceNo">
          <el-input
            v-model="queryParams.deviceNo"
            placeholder="设备编号"
            clearable
            style="width: 240px"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['system:event:add']"
          >服务登记</el-button>
        </el-col> -->
        <!-- <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['system:event:edit']"
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
            v-hasPermi="['system:event:remove']"
          >删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['system:event:export']"
          >导出</el-button>
        </el-col> -->
        <!-- <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row> -->

      <el-table v-loading="loading" :data="eventList" @selection-change="handleSelectionChange"  @row-click="cardDetails">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="重要级别" align="center" prop="level">
          <template slot-scope="scope">
          {{ eventLevelFormat(scope.row) }}
          </template>
        </el-table-column>

        <!-- <el-table-column label="事件编号" align="center" prop="no" show-overflow-tooltip/> -->
        <el-table-column label="设备名称" align="center" prop="deviceName" width="120" show-overflow-tooltip />
        <el-table-column label="设备编号" align="center" prop="deviceNo" width="120" show-overflow-tooltip/>
        <el-table-column label="事件内容" align="center" prop="content" width="120" show-overflow-tooltip/>
        <el-table-column label="报警时间" align="center" prop="createTime" width="180" color="#FF0000">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作时间" align="center" prop="operateTime" width="180" color="#FF0000">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.operateTime) }}</span>
          </template>
        </el-table-column>
        <!-- 待调整 -->
        <el-table-column label="操作类型" align="center" prop="operateType" >
          <template slot-scope="scope">
          {{ operateTypeFormat(scope.row) }}
          </template>
        </el-table-column>

        <!-- 待调整 -->
        <el-table-column label="会员手机号" align="center" prop="memberPhone"/>
        <!-- 待调整 -->
        <el-table-column label="操作员姓名" align="center" prop="userName" />
        <el-table-column label="处理标志" align="center" prop="operateFlag">
          <template slot-scope="scope">
          {{ operateFlagFormat(scope.row) }}
          </template>
        </el-table-column>
        <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:event:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:event:remove']"
            >删除</el-button>
          </template>
        </el-table-column> -->
        <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-info"
              @click="handleView(scope.row)"
            >查看</el-button>
          </template>
        </el-table-column> -->
      </el-table>

      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
      
      <br>
       <!-- 机构基本信息嵌入位置 -->
       <el-row>
        <org-info-card :value="currentOrgId"></org-info-card>
		  </el-row>
      <el-row><br></el-row>
            <!-- 设备基本信息嵌入位置 -->
      <el-row>
        <device-info-card :value="deviceId"></device-info-card>
		  </el-row>
      <!-- 服务登记 -->
      <!-- <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-form-item label="紧急联系人电话" prop="phone">
          <el-radio-group v-model="form.phone" size="medium">
            <el-radio v-for="(item, index) in phoneOptions" :key="index" :label="item.value"
              :disabled="item.disabled" border>{{item.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
          <el-form-item label="快捷回复" prop="level">
            <el-input v-model="form.level" placeholder="" />
          </el-form-item>
          <el-form-item label="服务备注" prop="remark">
            <el-input v-model="form.remark" placeholder="" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">提 交</el-button>
          <el-button @click="cancel">返 回</el-button>
        </div>
      </el-dialog>   -->

      <!-- 添加或修改事件对话框 -->
      <!-- <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="事件编号" prop="no">
            <el-input v-model="form.no" placeholder="请输入事件编号" />
          </el-form-item>
          <el-form-item label="级别" prop="level">
            <el-input v-model="form.level" placeholder="请输入级别" />
          </el-form-item>
          <el-form-item label="内容">
            <editor v-model="form.content" :min-height="192"/>
          </el-form-item>
          <el-form-item label="设备id" prop="deviceId">
            <el-input v-model="form.deviceId" placeholder="请输入设备id" />
          </el-form-item>
          <el-form-item label="设备组id" prop="devicegroupId">
            <el-input v-model="form.devicegroupId" placeholder="请输入设备组id" />
          </el-form-item>
          <el-form-item label="家庭id" prop="familyId">
            <el-input v-model="form.familyId" placeholder="请输入家庭id" />
          </el-form-item>
          <el-form-item label="设备编号" prop="deviceNo">
            <el-input v-model="form.deviceNo" placeholder="请输入设备编号" />
          </el-form-item>
          <el-form-item label="机构id" prop="orgId">
            <el-input v-model="form.orgId" placeholder="请输入机构id" />
          </el-form-item>
          <el-form-item label="机构名称" prop="orgName">
            <el-input v-model="form.orgName" placeholder="请输入机构名称" />
          </el-form-item>
          <el-form-item label="会员id" prop="memberId">
            <el-input v-model="form.memberId" placeholder="请输入会员id" />
          </el-form-item>
          <el-form-item label="会员手机号" prop="memberPhone">
            <el-input v-model="form.memberPhone" placeholder="请输入会员手机号" />
          </el-form-item>
          <el-form-item label="会员姓名" prop="memberName">
            <el-input v-model="form.memberName" placeholder="请输入会员姓名" />
          </el-form-item>
          <el-form-item label="运营者id" prop="userId">
            <el-input v-model="form.userId" placeholder="请输入运营者id" />
          </el-form-item>
          <el-form-item label="运营者姓名" prop="userName">
            <el-input v-model="form.userName" placeholder="请输入运营者姓名" />
          </el-form-item>
          <el-form-item label="操作时间" prop="operateTime">
            <el-date-picker clearable
              v-model="form.operateTime"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="请选择操作时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="操作标志" prop="operateFlag">
            <el-input v-model="form.operateFlag" placeholder="请输入操作标志" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog> -->
    </div>
  </template>

  <script>
  import { listEvent, getEvent  } from "@/api/eventAndMessage/event";
  //服务登记操作
  //import {addRecord} from "@/api/member/servedOprLog";
  import OrgInfoCard from "@/views/org/components/OrgInfoCard";
  import DeviceInfoCard from "@/views/device/components/DeviceInfoCard";



  export default {
    name: "orgDeviceEvents",
    dicts: ['sys_operate_flag','sys_operate_type','event_level'],
    // components: { Treeselect },
    components: { OrgInfoCard ,DeviceInfoCard},
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
        // 事件表格数据
        eventList: [],
        // 弹出层标题
        title: "",
        // 机构树选项
        //orgOptions: undefined,
        orgOptions: [],
        // 日期范围
        dateRange: [],
        //联系人列表
        phoneOptions:[],
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          no: null,
          level: null,
          content: null,
          deviceId: null,
          devicegroupId: null,
          familyId: null,
          deviceNo: null,
          orgId: null,
          orgName: null,
          operateType: null,
          memberId: null,
          memberPhone: null,
          memberName: null,
          userId: null,
          userName: null,
          operateTime: null,
          operateFlag: null,
        },
         //卡片传值使用
         orgId: null,
         deviceId: null,
        //路由接收参数定义
        currentOrgId: undefined,
        currentOperateFlag: undefined,
        // 表单参数
        form: {
          servedType:0,
          servedInfo:null,
          deviceId:null,
          serveEvents:[],

        },
        // 表单校验
        rules: {
        }
      };
    },

    created() {
      //this.getList();
      //初始化获取路由传参
      this.currentOrgId = this.$route.query.orgId==undefined?undefined:Number(this.$route.query.orgId)
      this.currentOperateFlag = this.$route.query.operateFlag==undefined?undefined:this.$route.query.operateFlag
      //this.getList();
      this.resetQuery();

    },
    //监听路由配置
    watch: {
      //路由传值接收
      //设备id接收
      "$route.query.orgId": {
      immediate: true,
      handler: function (val) {
        if (this.$route.name == "orgDeviceEvents") {
          if (this.currentOrgId != val) {
            this.currentOrgId = val==undefined?undefined:Number(val);
            this.resetQuery();
          }
        }
      }
    },
     //处理标志
    "$route.query.operateFlag": {
      immediate: true,
      handler: function (val) {
        if (this.$route.name == "orgDeviceEvents") {
          if (this.currentOperateFlag != val) {
            this.currentOperateFlag = val==undefined?undefined:Number(val);
            this.resetQuery();
          }
        }
      }
    },

  },

    methods: {
      /** 查询事件列表 */
      getList() {
        this.loading = true;
        listEvent(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.eventList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },

      //记录行点击事件
      cardDetails(row){
            this.orgId = row.orgId;
            this.deviceId = row.deviceId;
          },
    // 处理标志字典翻译
      operateFlagFormat(row, column) {
      return this.selectDictLabel(this.dict.type.sys_operate_flag, row.operateFlag)
    },
    // 操作类型字典翻译
    operateTypeFormat(row, column) {
      return this.selectDictLabel(this.dict.type.sys_operate_type, row.operateType)
    },

    //事件级别字段翻译
    eventLevelFormat(row, column) {
      return this.selectDictLabel(this.dict.type.event_level, row.level)
    },

      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          eventId: null,
          no: null,
          level: null,
          content: null,
          deviceId: null,
          devicegroupId: null,
          familyId: null,
          deviceNo: null,
          orgId: null,
          orgName: null,
          operateType: null,
          memberId: null,
          memberPhone: null,
          memberName: null,
          userId: null,
          userName: null,
          operateTime: null,
          operateFlag: null,
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
        this.dateRange = [];
        //this.resetForm("queryForm");
        // this.$refs.tree.setCurrentKey(null);
        this.queryParams = {
          pageNum: 1,
          pageSize: 10,
          no: null,
          level: null,
          content: null,
          deviceId: null,
          devicegroupId: null,
          familyId: null,
          deviceNo: null,
          orgId: this.currentOrgId,
          orgName: null,
          operateType: null,
          memberId: null,
          memberPhone: null,
          memberName: null,
          userId: null,
          userName: null,
          operateTime: null,
          operateFlag: this.currentOperateFlag,
        }
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.eventId)
        this.single = selection.length!==1
        this.multiple = !selection.length

        this.form.deviceId = selection[0]?.deviceId
        this.form.serveEvents =  selection.map(item => {
          return {
            eventId:item.eventId,
          }
        })
      },
      /** 新增按钮操作 */
      handleAdd() {
        //this.reset();
        this.open = true;
        this.title = "添加事件";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const eventId = row.eventId || this.ids
        getEvent(eventId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改事件";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          // if (valid) {
          //   if (this.form.eventId != null) {
          //     updateEvent(this.form).then(response => {
          //       this.$modal.msgSuccess("修改成功");
          //       this.open = false;
          //       this.getList();
          //     });
          //   } else {
          //     addEvent(this.form).then(response => {
          //       this.$modal.msgSuccess("新增成功");
          //       this.open = false;
          //       this.getList();
          //     });
          //   }
          // }
          addRecord(this.form).then(response => {
            this.$modal.msgSuccess("服务登记成功");
            this.open = false;
            this.getList();
            });
        });
      },
      // /** 删除按钮操作 */
      // handleDelete(row) {
      //   const eventIds = row.eventId || this.ids;
      //   this.$modal.confirm('是否确认删除事件编号为"' + eventIds + '"的数据项？').then(function() {
      //     return delEvent(eventIds);
      //   }).then(() => {
      //     this.getList();
      //     this.$modal.msgSuccess("删除成功");
      //   }).catch(() => {});
      // },
      // /** 导出按钮操作 */
      // handleExport() {
      //   this.download('system/event/export', {
      //     ...this.queryParams
      //   }, `event_${new Date().getTime()}.xlsx`)
      // }
    }
  };
  </script>
  <style land="css">
  .el-tooltip__popper{max-width:50%}
  .el-table__body,.el-table__header,.el-table__footer {
    width: 100%;
    table-layout: fixed !important;
  }
</style>
