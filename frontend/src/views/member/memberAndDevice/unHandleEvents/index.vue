<template>
    <div class="app-container">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">

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
          <el-input
            v-model="queryParams.level"
            placeholder="请输入级别"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
<!-- 待调整 -->
        <el-form-item label="设备名称" prop="deviceNo">
          <el-input
            v-model="queryParams.deviceNo"
            placeholder="请输入设备名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        
        <!-- <el-form-item label="归属机构" prop="orgId">
            <treeselect style="width: 240px" v-model="queryParams.orgId" :options="orgOptions" :show-count="true" placeholder="请选择归属机构" />
          </el-form-item> -->
          <el-form-item label="设备编号" prop="deviceNo">
          <el-input
            v-model="queryParams.deviceNo"
            placeholder="请输入设备编号"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>

        <!-- 待调整 -->
        <el-form-item label="会员名称" prop="deviceNo">
          <el-input
            v-model="queryParams.deviceNo"
            placeholder="请输入会员名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>

         <!-- 待调整 -->
         <el-form-item label="处理人员" prop="deviceNo">
          <el-input
            v-model="queryParams.deviceNo"
            placeholder="请输入处理人员"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>


        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
  
      <el-row :gutter="10" class="mb8">
        <!-- <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['system:event:add']"
          >新增</el-button>
        </el-col> -->
        <el-col :span="3">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['system:event:add']"
          >服务登记</el-button>
        </el-col>
        <!-- <el-col :span="4">
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['system:event:edit']"
          >服务登记</el-button>
        </el-col> -->
        
       
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
  
      <el-table v-loading="loading" :data="eventList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="重要级别" align="center" prop="level" />
        <el-table-column label="消息编号" align="center" prop="no" />
        <el-table-column label="消息内容" align="center" prop="content" />
        <el-table-column label="设备名称" align="center" prop="deviceId" />
        <el-table-column label="设备编号" align="center" prop="deviceNo" />
        <el-table-column label="报警时间" align="center" prop="operateTime" width="180" color="#FF0000">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.operateTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作时间" align="center" prop="operateTime" width="180" color="#FF0000">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.operateTime) }}</span>
          </template>
        </el-table-column>
        
        <!-- 待调整 -->
        <el-table-column label="设备归属会员" align="center" prop="memberPhone" />
        <el-table-column label="处理标志" align="center" prop="operateFlag">
          <template slot-scope="scope">
          {{ operateFlagFormat(scope.row) }}
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
  
      <!-- 会员基本信息嵌入 -->
     <el-row>
		  <member-info-card :value="memberId"></member-info-card>
		 </el-row>	

     <!-- 设备基本信息嵌入位置 -->
     <el-row>
      <device-info-card></device-info-card>
		  </el-row>	  

      <!-- 添加或修改事件对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
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
          <el-form-item label="服务备注" prop="content">
            <el-input v-model="form.phone" placeholder="" />
          </el-form-item>
          <!-- <el-form-item label="服务备注"  prop="phone">
            <editor v-model="form.content" :min-height="192"/>
          </el-form-item> -->
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">提 交</el-button>
          <el-button @click="cancel">返 回</el-button>
        </div>
      </el-dialog>
    </div>
  </template>
  
  <script>
  //import { listEvent, getEvent, delEvent, addEvent, updateEvent ,orgTreeSelect } from "@/api/eventAndMessage/event";
  import { listEvent, getEvent, delEvent, addEvent, updateEvent  } from "@/api/eventAndMessage/event";
  import {getMember} from "@/api/member/member";
  //服务登记
  import {addRecord} from "@/api/member/servedOprLog";
  // import Treeselect from "@riophae/vue-treeselect";
  // import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import MemberInfoCard from "@/views/member/components/MemberInfoCard";
  import DeviceInfoCard from "@/views/device/components/DeviceInfoCard";

  export default {
    name: "HandleEvents",
    dicts: ['sys_operate_flag'],
    // components: { Treeselect },
    components: { MemberInfoCard ,DeviceInfoCard},

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
        // 是否显示弹出层
        open: false,
        // 查询参数

         phoneOptions:[]  ,
      // {
      //   "label": "13770000001",
      //   "value": 13770000001
      // }, {
      //   "label": "13774522537",
      //   "value": 13774522537
      // }, {
      //   "label": "13770000002",
      //   "value": 13770000002
      // }],

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
        // 表单参数
        form: {},
        // 表单校验
        rules: {
        }
      };
    },
    watch: {
 
  },
    created() {
      this.getList();
      // this.getDeptTree();

    },
    methods: {
      /** 查询事件列表 */
      getList() {
        this.loading = true;
        listEvent(this.queryParams).then(response => {
          this.eventList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      initMember() {
      if (this.value != undefined)
        getMember(this.value).then(response => {
          this.member = response.data;
        })
    },

     

    // 处理标志字典翻译
      operateFlagFormat(row, column) {
      return this.selectDictLabel(this.dict.type.sys_operate_flag, row.operateFlag)
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
        this.resetForm("queryForm");
        // this.$refs.tree.setCurrentKey(null);
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.eventId)
        this.single = selection.length!==1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
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
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
          });
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const eventIds = row.eventId || this.ids;
        this.$modal.confirm('是否确认删除事件编号为"' + eventIds + '"的数据项？').then(function() {
          return delEvent(eventIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('system/event/export', {
          ...this.queryParams
        }, `event_${new Date().getTime()}.xlsx`)
      }
    }
  };
  </script>
  