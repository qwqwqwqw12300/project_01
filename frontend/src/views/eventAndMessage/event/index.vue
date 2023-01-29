<template>
    <div class="app-container">
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
          <el-select
              v-model="queryParams.level"
              placeholder="重要级别"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in dict.type.event_level"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
        </el-form-item>

        <el-form-item label="设备名称" prop="deviceName">
          <el-input
            v-model="queryParams.deviceName"
            placeholder="请输入设备名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        
        <el-form-item label="归属机构" prop="orgId">
            <treeselect style="width: 240px" v-model="queryParams.orgId" :options="orgOptions" :show-count="true" placeholder="请选择归属机构" />
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
        </el-col>
        <el-col :span="1.5">
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
        </el-col> -->
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['event:export']"
          >导出</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
  
      <el-table v-loading="loading" :data="eventList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <!-- <el-table-column label="重要级别" align="center" prop="level" /> -->
        <el-table-column label="重要级别" align="center" prop="level">
          <template slot-scope="scope">
          {{ eventLevelFormat(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column label="事件编号" align="center" prop="no" width="115" show-overflow-tooltip/>
        <el-table-column label="事件内容" align="center" prop="content" width="115" show-overflow-tooltip />
        <el-table-column label="报警时间" align="center" prop="createTime" width="180" color="#FF0000">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="设备名称" align="center" prop="deviceName" width="115" show-overflow-tooltip/>
        <el-table-column label="设备编号" align="center" prop="deviceNo" width="115" show-overflow-tooltip/>
       
        <el-table-column label="设备类型" align="center" prop="deviceType">
          <template slot-scope="scope">
          {{ deviceTypeFormat(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column label="设备归属机构" align="center" prop="orgName" /> 
        <el-table-column label="机构类型" align="center" prop="orgType">
        <template slot-scope="scope">
          {{ orgTypeFormat(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column label="处理标志" align="center" prop="operateFlag" >
          <template slot-scope="scope">
           {{ operateFlagFormat(scope.row) }}
          </template>
        </el-table-column>
   
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-info"
              @click="handleView(scope.row)"
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
          <el-table-column label="消息内容" align="center" prop="content" show-overflow-tooltip/>
          <el-table-column label="操作人员" align="center" prop="operator" />
          <el-table-column label="接收人员" align="center" prop="memberName" />
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
    </div>
  </template>
  
  <script>
  import { listEvent, getEvent, delEvent, addEvent, updateEvent ,orgTreeSelect } from "@/api/eventAndMessage/event";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import { listMsg, getMsg} from "@/api/eventAndMessage/message";

  export default {
    name: "Event",
    dicts: ['sys_operate_flag','sys_msg_type','sys_send_status','device_type','event_level','org_type'],
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
           pageSize: 100,
           eventId: 0,
        
        },
        // eventId:0,
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
      this.getDeptTree();
      //this.getMsgList();

    },
  //   activated() {
  //  this.getMsgList()
  //  },
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
      /** 查询用户列表 */
    // getList() {
    //   this.loading = true;
    //   pageUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
    //       this.userList = response.rows;
    //       this.total = response.total;
    //       this.loading = false;
    //     }
    //   );
    // },
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
    operateFlagFormat(row, column) {
      return this.selectDictLabel(this.dict.type.sys_operate_flag, row.operateFlag)
    },

    // 发送状态字典翻译
    sendStatusFormat(row, column) {
      return this.selectDictLabel(this.dict.type.sys_send_status, row.sendStatus)
    },
    //设备类型字段翻译(修改)
    
    deviceTypeFormat(row, column) {
      return this.selectDictLabel(this.dict.type.device_type, row.deviceType)
    },
    //事件级别字段翻译
    eventLevelFormat(row, column) {
      return this.selectDictLabel(this.dict.type.event_level, row.level)
    },

    //机构类型字段翻译
    orgTypeFormat(row, column) {
      return this.selectDictLabel(this.dict.type.org_type, row.orgType)
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
          eventId:null,
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },

       /** 查看按钮操作 */
       handleView(row) {
        //this.reset();
      this.eventId = row.eventId 
       console.log("eventId=============="+ this.eventId)
       this.getMsgList();
       this.open = true;

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

      // msgId多选框选中数据
    handleMsgSelectionChange(selection) {
      this.ids = selection.map(item => item.msgId)
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
          if (valid) {
            if (this.form.eventId != null) {
              updateEvent(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addEvent(this.form).then(response => {
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
        this.download('event/export', {
          ...this.queryParams
        }, `事件_${new Date().getTime()}.xlsx`)
      }
    }
  };
  </script>

<style land="css">.el-tooltip__popper{max-width:50%}
</style>
  