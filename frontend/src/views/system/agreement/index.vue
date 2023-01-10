<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="协议版本" prop="ver">
        <el-input
          v-model="queryParams.ver"
          placeholder="请输入协议版本"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="协议类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择"  >
          <!-- <el-option v-for ="item in stateArr" :key="item.value":label="item.label" :value="item.value"></el-option> -->
          <el-option
                v-for="dict in dict.type.sys_agreement_type"
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:agreement:add']"
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
          v-hasPermi="['system:agreement:edit']"
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
          v-hasPermi="['system:agreement:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:agreement:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="agreementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="协议id" align="center" prop="agreementId" />
      <el-table-column label="协议内容" align="center" prop="content" />
      <el-table-column label="协议版本" align="center" prop="ver" />
      <el-table-column label="协议类型" align="center" prop="type" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:agreement:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:agreement:remove']"
          >删除</el-button>
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

    <!-- 添加或修改用户协议管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="协议内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="协议版本" prop="ver">
          <el-input v-model="form.ver" placeholder="请输入协议版本" />
        </el-form-item>
        <el-form-item label="协议类型" prop="type">
          <!-- <el-radio-group v-model="form.type">
            <el-radio-button label="1" >APP协议</el-radio-button>
            <el-radio-button label="0" >隐私协议</el-radio-button>
          </el-radio-group> -->
          <el-select v-model="form.type" placeholder="请选择协议类型">
                <el-option
                  v-for="dict in dict.type.sys_agreement_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>       
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAgreement, getAgreement, delAgreement, addAgreement, updateAgreement } from "@/api/system/agreement";

export default {
  name: "Agreement",
  // 字典引入
  dicts: ['sys_agreement_type'],

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
      // 用户协议管理表格数据
      agreementList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        content: null,
        ver: null,
        type: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
          content:{ required: true, message: '协议内容不能为空！', trigger: 'blur' },
          ver:{ required: true, message: '协议版本不能为空！', trigger: 'blur' },
          type:{ required: true, message: '请选择协议类型', trigger: 'blur' }
      },
      //协议类型
      // stateArr:[
      //     { value:1,label:'APP协议'},
      //     { value:0,label:'隐私协议'}
      // ],
      // radioType:"1"
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户协议管理列表 */
    getList() {
      this.loading = true;
      listAgreement(this.queryParams).then(response => {
        this.agreementList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        agreementId: null,
        content: null,
        ver: null,
        type: null,
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
      this.ids = selection.map(item => item.agreementId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户协议管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const agreementId = row.agreementId || this.ids
      getAgreement(agreementId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户协议管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.agreementId != null) {
            updateAgreement(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAgreement(this.form).then(response => {
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
      const agreementIds = row.agreementId || this.ids;
      this.$modal.confirm('是否确认删除用户协议管理编号为"' + agreementIds + '"的数据项？').then(function() {
        return delAgreement(agreementIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/agreement/export', {
        ...this.queryParams
      }, `agreement_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
