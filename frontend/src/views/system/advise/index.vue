<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="问题内容" prop="content">
        <el-input
          v-model="queryParams.content"
          placeholder="问题内容"
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
    <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:advise:edit']"
        >一键已读</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:advise:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="adviseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="问题内容" align="center" prop="content" />
      <el-table-column label="会员名称" align="center" prop="memberId" />
      <el-table-column label="提交时间" align="center" prop="createTime" />
      <el-table-column label="已读标志" align="center" prop="readFlag" >
        <template slot-scope="scope">
          {{ readFlagFormat(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:advise:edit']"
          >已读</el-button>
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

    <!-- 添加或修改问题建议管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="建议内容">
          <el-input v-model="form.content" :disabled="true" Style="width:100% "/>
        </el-form-item>
        <el-form-item label="会员名称" prop="memberId">
          <el-input v-model="form.memberId" :disabled="true" />
        </el-form-item>
        <el-form-item label="已读标志" prop="readFlag">
            <el-input v-model="form.readFlag" :disabled="true"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :disabled="form.readFlag == '已读' ? true:false"
                   type="primary" @click="submitForm">已 读</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAdvise, getAdvise, delAdvise, addAdvise, updateAdvise } from "@/api/system/advise";

export default {
  name: "Advise",
  dicts: ['sys_read_flag'],
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
      // 问题建议管理表格数据
      adviseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        content: null,
        memberId: null,
        readFlag: null,
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
    /** 查询问题建议管理列表 */
    getList() {
      this.loading = true;
      listAdvise(this.queryParams).then(response => {
        this.adviseList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    // 已读未读字典翻译
    readFlagFormat(row, column) {
      return this.selectDictLabel(this.dict.type.sys_read_flag, row.readFlag)
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        adviseId: null,
        content: null,
        memberId: null,
        readFlag: null,
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
      this.ids = selection.map(item => item.adviseId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加问题建议管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const adviseId = row.adviseId || this.ids
      getAdvise(adviseId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "建议已读操作";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.adviseId != null) {
            updateAdvise(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAdvise(this.form).then(response => {
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
      const adviseIds = row.adviseId || this.ids;
      this.$modal.confirm('是否确认删除问题建议管理编号为"' + adviseIds + '"的数据项？').then(function() {
        return delAdvise(adviseIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/advise/export', {
        ...this.queryParams
      }, `advise_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
