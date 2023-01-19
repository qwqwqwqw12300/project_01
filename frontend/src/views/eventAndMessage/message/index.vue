<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="操作人员" prop="operator">
            <treeselect style="width: 240px" v-model="queryParams.operator" :options="orgOptions" :show-count="true" placeholder="请选择操作人员" />
      </el-form-item>
      
      <el-form-item label="查询处理时间">
            <el-date-picker
              v-model="dateRange"
              style="width: 300px"
              value-format="yyyy-MM-dd HH:mm:ss"
              type="datetimerange"
              range-separator="-"
              start-placeholder="开始查询时间"
              end-placeholder="结束查询时间"
            ></el-date-picker>
      </el-form-item>
      <el-form-item label="消息类型" prop="msgType">
            <el-select
              v-model="queryParams.msgType"
              placeholder="消息类型"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in dict.type.sys_msg_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
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
          v-hasPermi="['system:msg:add']"
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
          v-hasPermi="['system:msg:edit']"
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
          v-hasPermi="['system:msg:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:msg:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="msgList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="消息类型" align="center" prop="msgType" >
        <template slot-scope="scope">
          {{ msgTypeFormat(scope.row) }}
          <!-- <span>{{ dict.type.sys_msg_type?.find(x=>x.value == scope.row.msgType)?.label }}</span> -->
        </template>

      </el-table-column>
      <el-table-column label="消息内容" align="center" prop="content" />
      <el-table-column label="操作人员" align="center" prop="operator" />
      <el-table-column label="接收人员" align="center" prop="memberId" />
      <el-table-column label="发送时间" align="center" prop="sendTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.sendTime) }}</span>
        </template>
      </el-table-column>  
      <el-table-column label="发送状态" align="center" prop="sendStatus">
        <template slot-scope="scope">
          {{ sendStatusFormat(scope.row) }}
          <!-- <span>{{ dict.type.sys_msg_type?.find(x=>x.value == scope.row.msgType)?.label }}</span> -->
        </template>
      </el-table-column>

      <el-table-column label="失败原因" align="center" prop="reason" />
        
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:msg:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:msg:remove']"
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

    <!-- 添加或修改消息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <!-- <el-form-item label="消息编号" prop="no">
          <el-input v-model="form.no" placeholder="请输入消息编号" />
        </el-form-item> -->
        <el-form-item label="消息类型">
              <el-select v-model="form.msgType" placeholder="消息类型">
                <el-option
                  v-for="dict in dict.type.sys_msg_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
        </el-form-item>
        <el-form-item label="消息内容">
          <!-- <editor v-model="form.content" :min-height="192"/> -->
          <el-input v-model="form.content" placeholder="请输入消息内容" />
        </el-form-item>
        <!-- <el-form-item label="事件id" prop="eventId">
          <el-input v-model="form.eventId" placeholder="请输入事件id" />
        </el-form-item>
        <el-form-item label="设备id" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备id" />
        </el-form-item>
        <el-form-item label="家庭id" prop="familyId">
          <el-input v-model="form.familyId" placeholder="请输入家庭id" />
        </el-form-item>
        <el-form-item label="处理人会员id" prop="memberId">
          <el-input v-model="form.memberId" placeholder="请输入处理人会员id" />
        </el-form-item> -->
        <el-form-item label="操作人员" prop="operator">
          <el-input v-model="form.operator" placeholder="请输入操作人员" />
        </el-form-item>
        <el-form-item label="失败原因" prop="reason">
          <el-input v-model="form.reason" placeholder="请输入失败原因" />
        </el-form-item>
        <el-form-item label="发送状态">
              <el-select v-model="form.sendStatus" placeholder="发送状态">
                <el-option
                  v-for="dict in dict.type.sys_send_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
        </el-form-item>
        <!-- <el-form-item label="已读未读标志" prop="operateFlag">
          <el-input v-model="form.operateFlag" placeholder="请输入已读未读标志" />
        </el-form-item> -->
        <!-- <el-form-item label="发送时间" prop="sendTime">
          <el-date-picker clearable
            v-model="form.sendTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择发送时间">
          </el-date-picker>
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMsg, getMsg, delMsg, addMsg, updateMsg, orgTreeSelect} from "@/api/eventAndMessage/message";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";


export default {
  name: "Msg",
  dicts: ['sys_msg_type','sys_send_status'],
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
      // 消息表格数据
      msgList: [],
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
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        msgType: null,
        no: null,
        content: null,
        eventId: null,
        deviceId: null,
        familyId: null,
        memberId: null,
        operator: null,
        sendStatus: null,
        reason: null,
        operateFlag: null,
        sendTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },

  // watch: {
  //   // 根据名称筛选机构树
  //   orgName(val) {
  //     this.$refs.tree.filter(val);
  //   },
  //   'form.name':{
  //     handler:function (val) {
  //       this.form.nickName = val;
  //     }
  //   }
  // },
  
  created() {
    this.getList();
    this.getDeptTree();
  },
  methods: {
    /** 查询消息列表 */
    getList() {
      this.loading = true;
      listMsg(this.queryParams).then(response => {
        this.msgList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
         /** 查询机构下拉树结构 */
    getDeptTree() {
        orgTreeSelect().then(response => {
        this.orgOptions = response.data;
      });
    },

    // 消息类型字典翻译
    msgTypeFormat(row, column) {
      //return this.dict.type.sys_msg_type?.find(x=>x.value == row.msgType)?.label
      return this.selectDictLabel(this.dict.type.sys_msg_type, row.msgType);
    },
    // 发送状态字典翻译
    sendStatusFormat(row, column) {
      return this.selectDictLabel(this.dict.type.sys_send_status, row.sendStatus)
    },

    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        msgId: null,
        msgType: null,
        no: null,
        content: null,
        eventId: null,
        deviceId: null,
        familyId: null,
        memberId: null,
        operator: null,
        sendStatus: null,
        reason: null,
        operateFlag: null,
        sendTime: null,
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
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.msgId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加消息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const msgId = row.msgId || this.ids
      getMsg(msgId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改消息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.msgId != null) {
            updateMsg(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMsg(this.form).then(response => {
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
      const msgIds = row.msgId || this.ids;
      this.$modal.confirm('是否确认删除消息编号为"' + msgIds + '"的数据项？').then(function() {
        return delMsg(msgIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/msg/export', {
        ...this.queryParams
      }, `msg_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
