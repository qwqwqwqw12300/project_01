<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="80px"
    >
      <el-form-item label="查询时间">
        <el-date-picker
          v-model="dateRange"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始查询时间"
          end-placeholder="结束查询时间"
          style="width: 215px"

        ></el-date-picker>
      </el-form-item>

      <el-form-item label="处理标志" prop="operateFlag">
        <el-select
          v-model="queryParams.operateFlag"
          placeholder="处理标志"
          clearable
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
        >
          <el-option
            v-for="dict in dict.type.event_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <!-- 待调整 -->
      <el-form-item label="设备名称" prop="deviceName">
        <el-input
          v-model="queryParams.deviceName"
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
      <el-form-item label="会员名称" prop="memberName">
        <el-input
          v-model="queryParams.memberName"
          placeholder="请输入会员名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <!-- 待调整 -->
      <el-form-item label="处理人员" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入处理人员"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
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
      <!-- :disabled="single" -->

      <el-col :span="3">
        <el-button
          type="primary"
          plain
          icon="el-icon-edit"
          size="medium"
          :disabled="multiple"
          @click="handleAdd"
          v-hasPermi="['device:serveRecord:add']"
          >服务登记</el-button
        >
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
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <!-- 记录行点击事件 -->
    <el-table
      v-loading="loading"
      :data="eventList"
      @selection-change="handleSelectionChange"
      @row-click="cardDetails"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="重要级别" align="center" prop="level">
        <template slot-scope="scope">
          {{ eventLevelFormat(scope.row) }}
        </template>
      </el-table-column>

      <!-- <el-table-column label="事件编号" align="center" prop="no" width="120" show-overflow-tooltip/> -->
      <el-table-column label="事件内容" align="center" prop="content" width="120" show-overflow-tooltip/>
      <el-table-column label="设备名称" align="center" prop="deviceName" width="120" show-overflow-tooltip/>
      <el-table-column label="设备编号" align="center" prop="deviceNo"  width="120" show-overflow-tooltip/>
      <el-table-column
        label="报警时间"
        align="center"
        prop="createTime"
        width="180"
        color="#FF0000"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作时间"
        align="center"
        prop="operateTime"
        width="180"
        color="#FF0000"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.operateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="设备归属会员" align="center" prop="memberPhone" />
      <el-table-column label="处理标志" align="center" prop="operateFlag">
        <template slot-scope="scope">
          {{ operateFlagFormat(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-info"
            @click="handleView(scope.row)"
            :style="{ display: scope.row.eventId == '' ? 'none' : '' }"
            >查看消息</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <el-row><br /><br /></el-row>
    <!-- 会员基本信息嵌入 -->
    <el-row>
      <member-info-card :value="memberId"></member-info-card>
    </el-row>

    <!-- 设备基本信息嵌入位置 -->
    <el-row>
      <device-info-card :value="deviceId"></device-info-card>
    </el-row>

    <el-dialog :title="title" :visible.sync="msg" width="900px" append-to-body>
      <el-table
        v-loading="loading"
        :data="msgList"
        @selection-change="handleMsgSelectionChange"
      >
        <!-- <el-table-column type="selection" width="60" align="center" /> -->
        <el-table-column label="消息类型" align="center" prop="msgType">
          <template slot-scope="scope">
            {{ msgTypeFormat(scope.row) }}
            <!-- <span>{{ dict.type.sys_msg_type?.find(x=>x.value == scope.row.msgType)?.label }}</span> -->
          </template>
        </el-table-column>
        <el-table-column label="消息内容" align="center" prop="content" show-overflow-tooltip/>
        <el-table-column label="操作人员" align="center" prop="operator" />
        <el-table-column label="接收人员" align="center" prop="memberName" />
        <el-table-column
          label="发送时间"
          align="center"
          prop="sendTime"
          width="180"
        >
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
      </el-table>
      <pagination
        v-show="msgTotal > 0"
        :total="msgTotal"
        :page.sync="queryMsgParams.pageNum"
        :limit.sync="queryMsgParams.pageSize"
        @pagination="getMsgList"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改事件对话框 -->
    <!-- 服务登记 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-form-item label="紧急联系人电话:" prop="servedInfo">
          <el-radio-group v-model="form.servedInfo" size="medium">
            <el-radio
              v-for="(item, index) in phoneListOptions"
              :key="index"
              :label="item.value"
              :disabled="item.disabled"
              border
              >{{ item.value }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <!-- <el-form-item label="联系人:" prop="name">
            <el-select
              v-model="form.servedInfo"
              placeholder="联系人"
              clearable
              style="width: 240px"
            >
          <el-option
            v-for="item in phoneOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>

            </el-select>
          </el-form-item> -->
        <el-form-item label="快捷回复:" prop="reply">
          <!-- <el-input v-model="form.name" placeholder="" /> -->
          <el-select
            v-model="form.reply"
            placeholder="请选择快捷回复"
            clearable
            style="width: 240px"
            @change="selectReply"
          >
            <el-option
              v-for="item in replyOptions"
              :key="item.value"
              :label="item.label"
              :value="item.label"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="服务备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入内容"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">提 交</el-button>
        <el-button @click="cancel">返 回</el-button>
      </div>
    </el-dialog>
  </div>
</template>

  <script>
import { listEvent } from "@/api/eventAndMessage/event";
//获取会员信息
import { getMember } from "@/api/member/member";
//服务登记操作
import { addRecord } from "@/api/member/servedOprLog";
import { listMsg, getMsg } from "@/api/eventAndMessage/message";

import MemberInfoCard from "@/views/member/components/MemberInfoCard";
import DeviceInfoCard from "@/views/device/components/DeviceInfoCard";

export default {
  name: "HandleEvents",
  dicts: [
    "sys_operate_flag",
    "sys_operate_type",
    "sys_msg_type",
    "sys_send_status",
    "device_type",
    "event_level",
  ],

  // components: { Treeselect },
  components: { MemberInfoCard, DeviceInfoCard },

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
      msg: false,
      // 查询参数

      member: undefined,

      //联络人号码
      phoneListOptions: [],
      phoneOptions: [
        // {
        //   "label": "试点",
        //   "value": 13770000001
        // }, {
        //   "label": "士大夫",
        //   "value": 13774522537
        // }, {
        //   "label": "陈琛下",
        //   "value": 13770000002
        // }
      ],
      //快捷回复
      replyOptions: [
        {
          label: "已联系机构联系人",
          value: 0,
        },
        {
          label: "无人接听",
          value: 1,
        },
        {
          label: "已通知",
          value: 2,
        },
      ],

      memberId: 0,
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
        params: {
          memberEvent: true,
        },
      },

      // 消息表格数据
      msgList: [],
      queryMsgParams: {
        pageNum: 1,
        pageSize: 20,
        eventId: 0,
      },
      msgTotal:0,

      //卡片传值使用
      memberId: null,
      deviceId: null,

      currentOperateFlag: undefined,
      // 表单参数
      form: {
        servedType: 0,
        servedInfo: null,
        deviceId: null,
        serveEvents: [],
        remark:undefined
      },
      // 表单校验
      rules: {
        servedInfo: [
          { required: true, message: "必须选择手机号", trigger: "blur" },
        ],
      },
    };
  },
  watch: {
    //路由传值接收
    "$route.query.operateFlag": {
      immediate: true,
      handler: function (val) {
        if (this.$route.name == "UnHandleEvents") {
          if (this.currentOperateFlag != val) {
            this.currentOperateFlag =
              val == undefined ? undefined : Number(val);
            this.resetQuery();
          }
        }
      },
    },
  },
  created() {
    this.currentOperateFlag =
      this.$route.query.operateFlag == undefined
        ? undefined
        : this.$route.query.operateFlag;
    this.resetQuery();
    // this.getDeptTree();
    //初始化获取路由传参
  },
  methods: {
    /** 查询事件列表 */
    getList() {
      this.loading = true;
      // listEvent(this.queryParams).then(response => {
      listEvent(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          this.eventList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },

    /** 查询消息列表 */
    getMsgList() {
      this.queryMsgParams.eventId = this.eventId;

      listMsg(this.queryMsgParams).then((response) => {
        this.msgList = response.rows;
        this.msgTotal = response.total;
        // console.log(JSON.stringify(response) + ">>>>>" + ">>>>>>>" )
      });
    },

    initMember() {
      if (this.value != undefined)
        getMember(this.value).then((response) => {
          this.member = response.data;
        });
    },

    operateFlag() {
      return this.$route.query.operateFlag;
    },
    //记录行点击事件
    cardDetails(row) {
      this.memberId = row.memberId;
      this.deviceId = row.deviceId;
    },

    // 消息类型字典翻译
    msgTypeFormat(row, column) {
      //return this.dict.type.sys_msg_type?.find(x=>x.value == row.msgType)?.label
      return this.selectDictLabel(this.dict.type.sys_msg_type, row.msgType);
    },

    // 发送状态字典翻译
    operateFlagFormat(row, column) {
      return this.selectDictLabel(
        this.dict.type.sys_operate_flag,
        row.operateFlag
      );
    },

    // 发送状态字典翻译
    sendStatusFormat(row, column) {
      return this.selectDictLabel(
        this.dict.type.sys_send_status,
        row.sendStatus
      );
    },

    // 处理标志字典翻译
    operateFlagFormat(row, column) {
      return this.selectDictLabel(
        this.dict.type.sys_operate_flag,
        row.operateFlag
      );
    },

    // 操作类型字典翻译
    operateTypeFormat(row, column) {
      return this.selectDictLabel(
        this.dict.type.sys_operate_type,
        row.operateType
      );
    },

    //事件级别字段翻译
    eventLevelFormat(row, column) {
      return this.selectDictLabel(this.dict.type.event_level, row.level);
    },

    //快捷回复选项框事件
    selectReply(value) {
      const item = this.replyOptions.find(
        (item1) => item1.label === this.form.reply
      );
      this.form.remark = item?.label || ""
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
        updateTime: null,
        servedType: 0,
        servedInfo: null,
        deviceId: null,
        serveEvents: [],
        remark: undefined
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
        orgId: null,
        orgName: null,
        operateType: null,
        memberId: null,
        memberPhone: null,
        memberName: null,
        userId: null,
        userName: null,
        operateTime: null,
        operateFlag: this.currentOperateFlag,
        params: {
          memberEvent: true,
        },
      };
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.eventId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;

      this.form.deviceId = selection[0]?.deviceId;
      if (this.form.deviceId != undefined) {
        this.form.serveEvents = selection.map((item) => {
          return {
            eventId: item.eventId,
          };
        });

        //获取选中记录的会员id
        this.form.memberId = selection[0]?.memberId;
        getMember(this.form.memberId).then((response) => {
          this.phoneOptions = response.data.contacts;
        });
      }
    },

    /** 查看按钮操作 */
    handleView(row) {
      //this.reset();
      this.eventId = row.eventId;
      //  console.log("eventId=============="+ this.eventId)
      this.getMsgList();
      this.msg = true;
    },
    // msgId多选框选中数据
    handleMsgSelectionChange(selection) {
      this.ids = selection.map((item) => item.msgId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleAdd(row) {
      // this.reset();
      //获取会员紧急联系人联系方式
      this.phoneListOptions = this.phoneOptions.map((item) => ({
        value: item.phone,
        label: item.phone,
      }));
      this.open = true;
      this.title = "服务登记";
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
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
        //this.addData(this.form, this.eventList)
        // addRecord(this.form).then(response => {
        if (valid) {
          addRecord(this.form).then((response) => {
            this.$modal.msgSuccess("服务登记成功");
            this.open = false;
            this.getList();
          });
        }
      });
    },
  },
};
</script>


<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

<style scoped lang="scss">
.el-row {
  margin-bottom: 30px;

  & :last-child {
    margin-bottom: 0;
  }
}
</style>

<style land="css">
  .el-tooltip__popper{max-width:50%}
  .el-table__body,.el-table__header,.el-table__footer {
    width: 100%;
    table-layout: fixed !important;
  }
</style>
