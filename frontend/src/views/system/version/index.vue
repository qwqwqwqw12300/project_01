<template>
  <div class="app-container">
          <!--用户数据-->

        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="版本号" prop="content">
            <el-input
              v-model="queryParams.content"
              placeholder="请输入版本号"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>

          <el-form-item label="类型" prop="type">
            <el-select
              v-model="queryParams.type"
              placeholder="类型"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in dict.type.sys_app_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="删除标记" prop="delFlag">
            <el-select
              v-model="queryParams.delFlag"
              placeholder="删除标记"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in dict.type.sys_del_flag"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
    
    <!-- <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px"> -->
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
          v-hasPermi="['system:version:add']"
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
          v-hasPermi="['system:version:edit']"
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
          v-hasPermi="['system:version:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:version:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="versionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="版本id" align="center" prop="versionId" />
      <el-table-column label="版本号" align="center" prop="content" />
      <!-- <el-table-column label="类型" align="center" prop="type" /> -->
      <el-table-column label="类型" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_app_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <!-- <el-table-column label="删除标记" align="center" prop="delFlag" /> -->
      <el-table-column label="删除标记" align="center" prop="delFlag">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_del_flag" :value="scope.row.delFlag"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:version:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:version:remove']"
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

    <!-- 添加或修改版本对话框 -->
 <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <!--  <el-form-item label="版本号">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item> -->
        <el-form-item label="版本号" prop="content">
          <el-input v-model="form.content" placeholder="请输入版本号" />
        </el-form-item>
        <!-- <el-form-item label="类型" prop="type">
          <el-input v-model="form.type" placeholder="请输入类型" />
        </el-form-item> -->
            <el-form-item label="类型">
              <el-select v-model="form.type" placeholder="请选择类型">
                <el-option
                  v-for="dict in dict.type.sys_app_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>

        <!-- <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
        </el-form-item> -->
        <el-col :span="12">
            <el-form-item label="删除标志">
              <el-radio-group v-model="form.delFlag">
                <el-radio
                  v-for="dict in dict.type.sys_del_flag"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listVersion, getVersion, delVersion, addVersion, updateVersion } from "@/api/system/version";

export default {
  name: "Version",
  dicts: ['sys_del_flag', 'sys_app_type'],

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
      // 版本表格数据
      versionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        content: null,
        type: null,
        delFlag: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        content: [
          { required: true, message: "版本号不能为空", trigger: "blur" },
        ],
        type: [
          { required: true, message: "请选择类型", trigger: "blur" }
        ]

      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询版本列表 */
    getList() {
      this.loading = true;
      listVersion(this.queryParams).then(response => {
        this.versionList = response.rows;
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
        versionId: null,
        content: null,
        type: null,
        delFlag: "0",
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
      this.ids = selection.map(item => item.versionId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加版本";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const versionId = row.versionId || this.ids
      getVersion(versionId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改版本";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.versionId != null) {
            updateVersion(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addVersion(this.form).then(response => {
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
      const versionIds = row.versionId || this.ids;
      this.$modal.confirm('是否确认删除版本编号为"' + versionIds + '"的数据项？').then(function() {
        return delVersion(versionIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/version/export', {
        ...this.queryParams
      }, `version_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
