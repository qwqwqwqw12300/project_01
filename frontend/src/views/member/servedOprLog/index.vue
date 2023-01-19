<template>
  <div class="app-container">
  
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="服务人员姓名" prop="servedUser.name">
        <el-input
          v-model="queryParams.servedUser.name"
          placeholder="请输入服务人员姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务人员号码" prop="servedUser.mobilePhone">
        <el-input
          v-model="queryParams.servedUser.mobilePhone"
          placeholder="请输入服务人员号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="查询时间">
            <el-date-picker
              v-model="dateRange"
              style="width: 240px"
              value-format="yyyy-MM-dd HH:mm:ss"
              type="datetimerange"
              range-separator="-"
              start-placeholder="开始查询时间"
              end-placeholder="结束查询时间"
            ></el-date-picker>
          </el-form-item> -->
          <el-form-item label="查询时间" prop="createTimeBegin,createTimeEnd" >
            <el-date-picker
              v-model="queryParams.params.createTimeBegin"
              value-format="yyyy-MM-dd HH:mm:ss"
              type="datetime"
              placeholder="开始查询时间"
            ></el-date-picker>
			-
			<el-date-picker
              v-model="queryParams.params.createTimeEnd"
              value-format="yyyy-MM-dd HH:mm:ss"
              type="datetime"
              placeholder="结束查询时间"
            ></el-date-picker>
</el-form-item>    
      <el-form-item label="处理方式" prop="servedType">
        <el-input
          v-model="queryParams.servedType"
          placeholder="处理方式"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备类型" prop="device.type">
            <el-select
              v-model="queryParams.device.type"
              placeholder="设备类型"
              clearable
             
            >
              <el-option
                v-for="dict in dict.type.device_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
        </el-form-item>
        <!-- 机构下拉补充 -->
        <el-form-item label="归属机构" prop="orgId">
            <treeselect style="width: 220px"  v-model="queryParams.servedUser.orgId" :options="orgOptions" :show-count="true" placeholder="请选择归属机构" />
        </el-form-item>

      <el-form-item label="设备编号" prop="device.no">
        <el-input
          v-model="queryParams.device.no"
          placeholder="请输入设备编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备名称" prop="device.name">
        <el-input
          v-model="queryParams.device.name"
          placeholder="请输入设备名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
     <!--查询：device:serveRecord:query  -->
     <!--服务登记：device:serveRecord:add  -->

    <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['device:serveRecord:export']"
          >导出</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="处理方式" align="center" prop="servedType" />
        <el-table-column label="处理时间" align="center" prop="createTime" />
        <el-table-column label="服务人员" align="center" prop="servedUser.name" />
        <el-table-column label="服务人员手机号" align="center" prop="servedUser.mobilePhone" />
        <el-table-column label="处理人归属机构" align="center" prop="servedUser.orgName" />
        <el-table-column label="处理人服务备注" align="center" prop="remark" />
        <el-table-column label="设备名称" align="center" prop="device.name" />
        <el-table-column label="设备编号" align="center" prop="device.no" /> 
        <el-table-column label="关联消息编号" align="center" prop="operateFlag">
          <template slot-scope="scope">
            <span v-for="({no},index) in scope.row.serveEvents" :key="no">{{no}}
            <span v-if="index!==scope.row.serveEvents.length-1">,
            </span></span>
          </template>
        </el-table-column>
        <el-table-column label="设备分组＋位置" align="center"  prop="deviceGroupName,location">
          <template slot-scope="scope">
                    {{scope.row.device.deviceGroupName}}  {{scope.row.device.location}}
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
      







  </div>
</template>

<script>
import { listRecord, getRecord, delRecord, addRecord, updateRecord } from "@/api/member/servedOprLog";
import { orgTreeSelect } from "@/api/system/user";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Record",
  dicts: ['sys_device_status','device_type'],
  components: { Treeselect },

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
      // 服务记录表格数据
      recordList: [],
      // 弹出层标题
      title: "",
      // 机构树选项
      //orgOptions: undefined,
      orgOptions: [],
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,

        //设备组
        device:{
          //设备编号
          no : undefined,
          //设备名称
          name :undefined,
          //设备类型
          type :undefined,
        },
        
        servedUser:{
          //服务人员姓名
          name : undefined,
          //服务人员号码
          mobilePhone :undefined,
          //机构名
          orgName :undefined,
          //机构id
          orgId  :undefined,

        },
        params:{
        //处理时间起始
        createTimeBegin: null,
        //处理时间截止
        createTimeEnd: null, 
        },
        // //处理时间起始
        // createTimeBegin: null,
        // //处理时间截止
        // createTimeEnd: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
       
      }
    };
  },
  watch: {
    // 根据名称筛选机构树
    orgName(val) {
      this.$refs.tree.filter(val);
    },
    'form.name':{
      handler:function (val) {
        this.form.nickName = val;
      }
    }
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询服务记录列表 */
    getList() {
      this.loading = true;
      listRecord(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.recordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      this.getDeptTree();


      
    },
    /** 查询机构下拉树结构 */
    getDeptTree() {
        orgTreeSelect().then(response => {
        this.orgOptions = response.data;
      });
    },

    // 设备类型字典翻译
    deviceTypeFormat(row, column) {
      return this.selectDictLabel(this.dict.type.device_type, row.type)
    },
    // 设备状态字典翻译
    deviceStatusFormat(row, column) {
      return this.selectDictLabel(this.dict.type.sys_device_status, row.status)
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        recordId: null,
        servedUserId: null,
        servedUserSnapshot: null,
        servedType: null,
        servedInfo: null,
        deviceId: null,
        remark: null,
        delFlag: null,
        createBy: null,
        createTime: null
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
      this.ids = selection.map(item => item.recordId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加服务记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const recordId = row.recordId || this.ids
      getRecord(recordId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改服务记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.recordId != null) {
            updateRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRecord(this.form).then(response => {
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
      const recordIds = row.recordId || this.ids;
      this.$modal.confirm('是否确认删除服务记录编号为"' + recordIds + '"的数据项？').then(function() {
        return delRecord(recordIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/record/export', {
        ...this.queryParams
      }, `record_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
