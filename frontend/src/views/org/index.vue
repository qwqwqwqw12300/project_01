<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
          <el-form-item label="机构名称" prop="orgName">
            <el-input
              v-model="queryParams.orgName"
              placeholder="请输入机构名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="机构类型" prop="type">
            <el-select v-model="queryParams.type" placeholder="机构类型" clearable>
              <el-option
                v-for="dict in dict.type.org_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="机构状态" clearable>
              <el-option
                v-for="dict in dict.type.sys_normal_disable"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
<!--            <el-button size="mini" @click="handleArrange">分配</el-button>-->
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd()"
              v-hasPermi="['org:add']"
            >新增</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </el-col>

      <el-table
        v-if="refreshTable"
        v-loading="loading"
        :data="orgList"
        row-key="orgId"
      >
        <el-table-column v-for="column in columns" :label="column.label" :key="column.key" :prop="column.prop" align="center"
                         v-if="column.visible" :formatter="column.formatter" :width="column.width"
                         :show-overflow-tooltip="column.showOverFlowToolTip == undefined?true:column.showOverFlowToolTip">
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['org:edit']"
            >编辑</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-info"
              @click="handleView(scope.row)"
              v-hasPermi="['org:query']"
            >查看</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleDeviceGroupManage(scope.row)"
              v-hasPermi="['org:deviceGroup:list']"
            >设备组管理</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleImport(scope.row)"
              v-hasPermi="['org:deviceImport']"
            >导入设备</el-button>
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
    </el-row>
    <el-dialog :title="'分配运营'" :visible.sync="arrangeOpen" append-to-body>
      <el-transfer v-model="arrangeValue" :data="arrangeData"></el-transfer>
    </el-dialog>
    <!-- 添加或修改机构对话框 -->
    <form-panel :title="title" :visible.sync="open" width="600px" append-to-body >
      <h4 class="form-header h4">基本信息
        <span class="notice_require">为必填项</span>
      </h4>
      <el-form ref="form" :model="form" :rules="rules" label-position="top" :disabled="forView">

        <el-row type="flex" justify="center" :gutter="20">
          <el-col :span="12">
            <el-form-item label="机构名称" prop="orgName">
              <el-input v-model="form.orgName" placeholder="请输入机构名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构编号" prop="orgNo" class="is-required">
              <el-input v-model="form.orgNo" placeholder="机构编号自动生成" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="center" :gutter="20">
          <el-col :span="12">
            <el-form-item label="上级机构" prop="parentId">
              <treeselect v-if="!forView" :value="form.parentId === 0?null:form.parentId" @input="handleParentId" :options="orgOptions" :normalizer="normalizer" placeholder="选择上级机构" >
              </treeselect>
              <el-input v-if="forView" disabled v-model="form.parentName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构类型" prop="type">
              <el-select disabled v-model="form.type" placeholder="机构类型" clearable>
                <el-option
                  v-for="dict in dict.type.org_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="center" :gutter="20">
          <el-col :span="12">
            <el-form-item label="机构负责人姓名" prop="leader">
              <el-input v-model="form.leader" placeholder="请输入机构负责人姓名" maxlength="20" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构负责人号码" prop="leaderPhone">
              <el-input v-model="form.leaderPhone" placeholder="请输入机构负责人号码" maxlength="11" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="center" :gutter="20">
          <el-col :span="12">
            <el-form-item label="第一服务人员和号码" prop="attendantName1">
              <el-col :span="7">
                <el-input v-model="form.attendantName1" placeholder="请输入姓名" maxlength="20" />
              </el-col>
              <el-col :span="1">
                <span style="text-align: center">-</span>
              </el-col>
              <el-col :span="16">
                <el-input v-model="form.phone1" placeholder="请输入电话号码" maxlength="20" />
              </el-col>
              </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入机构地址" maxlength="255" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="center" :gutter="20">
          <el-col :span="12">
            <el-form-item label="第二服务人员和号码" prop="attendantName2">
              <el-col :span="7">
                <el-input v-model="form.attendantName2" placeholder="请输入姓名" maxlength="20" />
              </el-col>
              <el-col :span="1">
                <span style="text-align: center">-</span>
              </el-col>
              <el-col :span="16">
                <el-input v-model="form.phone2" placeholder="请输入电话号码" maxlength="20" />
              </el-col>
              </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="第三服务人员和号码" prop="attendantName3">
              <el-col :span="7">
                <el-input v-model="form.attendantName3" placeholder="请输入姓名" maxlength="20" />
              </el-col>
              <el-col :span="1">
                <span style="text-align: center">-</span>
              </el-col>
              <el-col :span="16">
                <el-input v-model="form.phone3" placeholder="请输入电话号码" maxlength="20" />
              </el-col>
              </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="机构状态" prop="status">
              <el-select v-model="form.status" placeholder="机构类型" clearable>
                <el-option
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div v-if="!forView" slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">提 交</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </form-panel>
    <!-- 设备导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?orgId=' + upload.orgId"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrg,pageOrg, getOrg, delOrg, addOrg, updateOrg, listOrgExcludeChild } from "@/api/org/org";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {getToken} from "@/utils/auth";

export default {
  name: "Org",
  dicts: ['sys_normal_disable','org_type'],
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格树数据
      orgList: [],
      // 机构树选项
      orgOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 弹出层仅查看
      forView: false,
      // 是否显示分配运营弹出层
      arrangeOpen:false,
      arrangeValue:[],
      arrangeData:[],
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 设备导入参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 机构id
        orgId: undefined,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/org/devices/importData"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orgName: undefined,
        type: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 列信息
      columns: [
        { key: 0, label: `机构名称`, prop:`orgName`, visible: true },
        { key: 1, label: `机构编号`, prop:`orgNo`, visible: true },
        { key: 2, label: `上级机构`, prop:`parentName`, visible: true },
        { key: 3, label: `机构类型`, prop:`type`, visible: true , formatter:(row, column, cellValue) => this.dict.type.org_type?.find(x=>x.value == cellValue)?.label},
        { key: 4, label: `机构负责人`, prop:`leader`, visible: true },
        { key: 5, label: `机构负责人联系号码`, prop:`leaderPhone`, visible: true },
        { key: 6, label: `机构地址`, prop:`address`, visible: true },
        { key: 7, label: `第一服务电话`, prop:`phone1`, visible: true },
        { key: 8, label: `第二服务号码`, prop:`phone2`, visible: true },
        { key: 9, label: `第三服务号码`, prop:`phone3`, visible: true, },
        { key: 10, label: `状态`, prop:`status`, visible: true, formatter:(row, column, cellValue) => this.dict.type.sys_normal_disable?.find(x=>x.value == cellValue)?.label},
        { key: 11, label: `创建时间`, prop:`createTime`, visible: false, formatter:(row, column, cellValue) => this.parseTime(cellValue) }
      ],
      // 表单校验
      rules: {
        orgName: [
          { required: true, message: "机构名称不能为空", trigger: "blur" }
        ],
        // orgNo: [
        //   { required: true, message: "机构编号不能为空", trigger: "blur" }
        // ],
        type: [
          { required: true, message: "请选择机构类型", trigger: "blur" }
        ],
        parentId: [
          { required: true, message: "请选择上级机构", trigger: "blur" },
          { min: 1, type:'number', message: "请选择上级机构", trigger: "blur" }
        ],
        orderNum: [
          { required: true, message: "显示排序不能为空", trigger: "blur" }
        ],
        leader: [
          { required: true, message: "机构负责人姓名不能为空", trigger: "blur" }
        ],
        leaderPhone: [
          { required: true, message: "机构负责人号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ],
        attendantName1: [
          { required: true, message: "第一服务人员姓名不能为空", trigger: "blur" }
        ],
        phone1: [
          { required: true, message: "第一服务人员号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ],
        address: [
          { required: true, message: "机构地址不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "请选择机构状态", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.reset();
    this.getList();
    const data = [];
    for (let i = 1; i <= 15; i++) {
      data.push({
        key: i,
        label: `备选项 ${ i }`,
        disabled: i % 4 === 0
      });
    }
    this.arrangeData = data;
  },
  methods: {
    /** 查询机构列表 */
    getList() {
      this.loading = true;
      pageOrg(this.queryParams).then(response => {
        this.orgList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 转换机构数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.orgId,
        label: node.orgName,
        children: node.children
      };
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.forView = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        orgId: undefined,
        parentId: 0,
        orgName: undefined,
        orgNo: "",
        type: "0",
        orderNum: 0,
        leader: undefined,
        leaderPhone: undefined,
        attendantName1: undefined,
        phone1: undefined,
        attendantName2: undefined,
        phone2: undefined,
        attendantName3: undefined,
        phone3: undefined,
        address: undefined,
        status: "0"
      };
      this.resetForm("form");
    },
    handleParentId(data){
      if(!data || data == null){
        this.form.parentId = 0;
      }else{
        this.form.parentId = data;
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 分配运营操作按钮 */
    handleArrange(){
      this.arrangeOpen = true;
    },
    /** 导入按钮操作 */
    handleImport(row) {
      this.upload.title = "设备导入";
      this.upload.orgId = row.orgId;
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('org/devices/importTemplate', {
      }, `devices_template_${new Date().getTime()}.xlsx`)
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      if (row) {
        this.form.parentId = row.orgId;
      }
      this.forView = false;
      this.open = true;
      this.title = "添加机构";
      listOrg().then(response => {
        this.orgOptions = this.handleTree(response.data, "orgId");
      });
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getOrg(row.orgId).then(response => {
        this.form = response.data;
        this.forView = false;
        this.open = true;
        this.title = "修改机构";
        listOrg().then(response => {
          this.orgOptions = this.handleTree(response.data, "orgId");
          if (this.orgOptions.length == 0) {
            const noResultsOptions = { orgId: this.form.parentId, orgName: this.form.parentName, children: [] };
            this.orgOptions.push(noResultsOptions);
          }
        });
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.reset();
      getOrg(row.orgId).then(response => {
        this.form = response.data;
        this.open = true;
        this.forView = true;
        this.title = "查看机构";
        listOrg().then(response => {
          this.orgOptions = this.handleTree(response.data, "orgId");
          if (this.orgOptions.length == 0) {
            const noResultsOptions = { orgId: this.form.parentId, orgName: this.form.parentName, children: [] };
            this.orgOptions.push(noResultsOptions);
          }
        });
      });
    },
    handleDeviceGroupManage(row){
      this.$router.push({path:'/org/deviceGroup', query: {orgId: row.orgId}});
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.orgId != undefined) {
            updateOrg(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOrg(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除名称为"' + row.orgName + '"的数据项？').then(function() {
        return delOrg(row.orgId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    }
  }
};
</script>
