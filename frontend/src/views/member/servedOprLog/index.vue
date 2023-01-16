<template>
  <div class="app-container">
    <h1>服务人员操作记录</h1>

        
       
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="服务人员姓名" prop="nickName">
        <el-input
          v-model="queryParams.nickName"
          placeholder="请输入服务人员姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务人员号码" prop="phonenumber">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="请输入服务人员号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="设备类型" prop="type">
        <el-input
          v-model="queryParams.type"
          placeholder="请输入设备类型"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="设备类型" prop="type">
            <el-select
              v-model="queryParams.type"
              placeholder="设备类型"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in dict.type.device_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
        </el-form-item>

      <el-form-item label="设备编号" prop="no">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="请输入设备编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备名称" prop="name">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="请输入设备名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备编号" prop="no">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="请输入设备编号"
          clearable
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
      
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>



  </div>
</template>

<script>
import { listRecord, getRecord, delRecord, addRecord, updateRecord } from "@/api/member/servedOprLog";

export default {
  name: "Record",
  dicts: ['sys_device_status','device_type'],

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
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        //服务人员姓名
        nickName: null,
        //服务人员号码
        phonenumber:null,
        //设备类型
        type: null,
        //设备编号
        no: null,
        //设备名称
        name:null,
        //处理时间起始
        createTimeBegin: null,
        //处理时间截止
        createTimeEnd: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
       
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询服务记录列表 */
    getList() {
      this.loading = true;
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows;
        this.total = response.total;
        this.loading = false;
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
