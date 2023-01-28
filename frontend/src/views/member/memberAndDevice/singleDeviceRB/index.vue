<template>
    <div class="app-container">
     <!-- 会员基本信息嵌入 -->
     <el-row>
		  <member-info-card :value="currentMemberId"></member-info-card>
		 </el-row>	 
     <el-row><br><br>
    </el-row>	

      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">

        <!-- <el-form-item label="操作时间" prop="operateTime">
          <el-date-picker clearable
            v-model="queryParams.operateTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择操作时间"> 
          </el-date-picker>
        </el-form-item> -->

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
  
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="medium"
            :disabled="multiple"
            @click="handleAdd"
            v-hasPermi="['device:serveRecord:add']"
          >服务登记</el-button>
        </el-col>
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
        </el-col>-->
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
  
      <el-table v-loading="loading" :data="eventList" @selection-change="handleSelectionChange" @row-click="cardDetails">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="级别" align="center" prop="level" />
        <el-table-column label="事件编号" align="center" prop="no" />
        <el-table-column label="事件内容" align="center" prop="content" />
        <el-table-column label="设备名称" align="center" prop="deviceName" />
        <el-table-column label="设备编号" align="center" prop="deviceNo" />
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
        <el-table-column label="操作类型" align="center" prop="operateType" />

        <!-- 待调整 -->
        <el-table-column label="会员手机号／操作员手机号" align="center" prop="memberPhone" />
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
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-info"
              @click="handleView(scope.row) "
              :style="{display: scope.row.eventId==''?'none':'' }"
            >查看消息</el-button>     
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
   
     <el-row><br><br>
    </el-row>

      <!-- 设备基本信息嵌入位置 -->
      <el-row>
        <device-info-card :value="currentDeviceId"></device-info-card>
		  </el-row>	  

      <!--  -->
      <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
        <el-table v-loading="loading" :data="msgList" @selection-change="handleMsgSelectionChange">
          <!-- <el-table-column type="selection" width="60" align="center" /> -->
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
      </el-table>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryMsgParams.pageNum"
        :limit.sync="queryMsgParams.pageSize"
        @pagination="getMsgList"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 服务登记 -->
    <el-dialog :title="title" :visible.sync="server" width="830px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="130px">
          <el-form-item label="紧急联系人电话:" prop="servedInfo">
          <el-radio-group v-model="form.servedInfo" size="medium">
            <el-radio v-for="(item, index) in phoneListOptions" :key="index" :label="item.value"
              :disabled="item.disabled" border>{{item.value}}</el-radio>
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
              v-model="form.remark"
              placeholder="快捷回复"
              clearable
              style="width: 240px"
            >
          <el-option
            v-for="item in replyOptions"
            :key="item.value"
            :label="item.label"
            :value="item.label">
          </el-option>

            </el-select>
          </el-form-item>
          <el-form-item label="服务备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder=""  style="width: 500px"></el-input>
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
  import { listEvent, getEvent, delEvent, addEvent, updateEvent  } from "@/api/eventAndMessage/event";
 //获取会员信息
  import {getMember} from "@/api/member/member";
  //服务登记操作
  import {addRecord} from "@/api/member/servedOprLog";
  import { listMsg, getMsg} from "@/api/eventAndMessage/message";
  import MemberInfoCard from "@/views/member/components/MemberInfoCard";
  import DeviceInfoCard from "@/views/device/components/DeviceInfoCard";

  export default {
    name: "singleDeviceRB",
    dicts: ['sys_operate_flag','sys_msg_type','sys_send_status','device_type'],
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
        //联络人号码
        phoneListOptions:[],
         phoneOptions:[],
      //快捷回复
      replyOptions:[
      {
        "label": "已联系机构联系人",
        "value": 0
      },
      {
        "label": "无人接听",
        "value": 1
      },
      {
        "label": "已通知",
        "value": 2
      }
    
      ],
        // 是否显示弹出层
        open: false,
        server:false,
        is_view:false,
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
      // 消息表格数据
      msgList: [],
        queryMsgParams: {
           pageNum: 1,
           pageSize: 20,
           eventId: 0,
        
        },

         //卡片传值使用
        memberId: null,
        deviceId: null,

        //路由接收参数定义
        currentDeviceId: undefined,
        currentMemberId: undefined,

         // 表单参数
         form: {
          servedType:0,
          servedInfo:null,
          deviceId:null,
          serveEvents:[],
        },
        // 表单校验
        rules: {
          servedInfo: [
          { required: true, message: "必须选择手机号", trigger: "blur" },
        ],
        }
      };
    },

    created() {
      //初始化获取路由传参
      this.currentDeviceId = this.$route.query.deviceId==undefined?undefined:Number(this.$route.query.deviceId)  
      this.currentMemberId = this.$route.query.memberId==undefined?undefined:Number(this.$route.query.memberId) 
      // console.log(this.currentDeviceId + "+++++++" + this.currentMemberId)
      //this.getList();
      this.is_view = false;
      this.resetQuery();
      

      // this.getDeptTree();
  },
//监听路由配置
    watch: {
      //路由传值接收
      //设备id接收
      "$route.query.deviceId": {
      immediate: true,
      handler: function (val) {
        if (this.$route.name == "SingleDeviceRB") {
          if (this.currentDeviceId != val) {
            this.currentDeviceId = val==undefined?undefined:Number(val);
            this.resetQuery();
          }
        }
      }
    },
     //会员id
    "$route.query.memberId": {
      immediate: true,
      handler: function (val) {
        if (this.$route.name == "SingleDeviceRB") {
          if (this.currentMemberId != val) {
            this.currentMemberId = val==undefined?undefined:Number(val);
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
     
      /** 查询消息列表 */
        getMsgList() {

    this.queryMsgParams.eventId= this.eventId

    listMsg(this.queryMsgParams).then(response => {
      this.msgList = response.rows;
      this.total = response.total;
      // console.log(JSON.stringify(response) + ">>>>>" + ">>>>>>>" )
    });

    },
     //记录行点击事件
     cardDetails(row){
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
      return this.selectDictLabel(this.dict.type.sys_operate_flag, row.operateFlag)
    },

    // 发送状态字典翻译
    sendStatusFormat(row, column) {
      return this.selectDictLabel(this.dict.type.sys_send_status, row.sendStatus)
    },
    

    /** 查看按钮操作 */
    handleView(row) {
        //this.reset();
      this.eventId = row.eventId 
      //  console.log("eventId=============="+ this.eventId)
       this.getMsgList();
       this.open = true;

    },
      // 取消按钮
      cancel() {
        this.open = false;
        this.server = false;
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
        
        this.queryParams= {
          pageNum: 1,
          pageSize: 10,
          no: null,
          level: null,
          content: null,
          deviceId: this.currentDeviceId,
          devicegroupId: null,
          familyId: null,
          deviceNo: null,
          orgId: null,
          orgName: null,
          operateType: null,
          memberId: this.currentMemberId,
          memberPhone: null,
          memberName: null,
          userId: null,
          userName: null,
          operateTime: null,
          operateFlag: null,
        },        
        //this.resetForm("queryForm");
        // this.$refs.tree.setCurrentKey(null);
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.eventId)
        this.single = selection.length!==1
        this.multiple = !selection.length

        this.form.deviceId = selection[0]?.deviceId
        if(this.form.deviceId!=undefined){
          this.form.serveEvents =  selection.map(item => {
          return {
            eventId:item.eventId,
          }
        })


        //获取选中记录的会员id
        this.form.memberId = selection[0]?.memberId
           getMember(this.form.memberId).then(response => {
        this.phoneOptions = response.data.contacts;
      });

        }
        
      },

       // msgId多选框选中数据
    handleMsgSelectionChange(selection) {
      this.ids = selection.map(item => item.msgId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
      /** 新增按钮操作 */
      handleAdd() {
        // this.reset();
        //获取会员紧急联系人联系方式
      this.phoneListOptions = this.phoneOptions.map(item => ({ value: item.phone, label: item.phone }))       
      this.server = true;
      this.title = "服务登记";
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
          //this.addData(this.form, this.eventList)
          // addRecord(this.form).then(response => {
          if (valid) {  
          addRecord(this.form).then(response => {

                this.$modal.msgSuccess("服务登记成功");
                this.server = false;
                this.getList();
                
          });
        
          }
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
  