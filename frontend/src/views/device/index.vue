<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--设备筛选-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="100px">
          <el-form-item label="设备编号" prop="no">
            <el-input
              v-model="queryParams.no"
              placeholder="设备编号"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="设备名称" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="设备名称"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="设备类型" prop="type">
            <el-select
              v-model="queryParams.type"
              placeholder="请选择设备类型"
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
          <el-form-item label="归属机构" prop="orgId">
            <treeselect style="width: 240px" v-model="queryParams.orgId" :options="orgOptions" :show-count="true"
                        placeholder="请选择归属机构"/>
          </el-form-item>
          <el-form-item label="是否已分配" prop="distributeFlag">
            <el-select
              v-model="queryParams.distributeFlag"
              placeholder="请选择是否已分配"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in distributeFlagDict"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="设备状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="请选择设备状态"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in dict.type.sys_device_status"
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

          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
        <el-table v-loading="loading" :data="deviceList">
          <el-table-column v-for="column in columns" :label="column.label" :key="column.key" :prop="column.prop"
                           v-if="column.visible" :formatter="column.formatter"
                           :show-overflow-tooltip="column.showOverFlowToolTip == undefined?true:column.showOverFlowToolTip">
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="180"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope" v-if="scope.row.userId !== 1">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleAssociate(scope.row)"
                v-hasPermi="['device:associate']"
              >设备分配
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-circle-check"
                @click="handleGroupArrange(scope.row)"
                v-hasPermi="['device:groupArrange']"
              >设备分组
              </el-button>
              <el-button
                v-if="scope.row.status == '2'"
                size="mini"
                type="text"
                icon="el-icon-circle-check"
                @click="active(scope.row)"
                v-hasPermi="['device:activeOrOffline']"
              >上线
              </el-button>
              <el-button
                v-if="scope.row.status == '1'"
                size="mini"
                type="text"
                icon="el-icon-circle-check"
                @click="offlineDialog(scope.row)"
                v-hasPermi="['device:activeOrOffline']"
              >下线
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-circle-check"
                @click="handleSettings(scope.row)"
                v-hasPermi="['device:settings']"
              >设备参数设置
              </el-button>
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
      </el-col>
    </el-row>
    <!--    设备配对弹窗-->
    <el-dialog title="设备分配" :visible.sync="associateOpen" center>
      <el-form ref="associateForm" :model="associateForm" :rules="associateRules" label-width="80px">
        <el-row type="flex" justify="center">
          <el-col :span="24">
            <el-form-item label="设备位置" porp="location">
              <el-input v-model="associateForm.location" placeholder="请输入设备位置" maxlength="100"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="center">
          <el-col :span="24">
            <el-form-item label="设备名称" porp="name">
              <el-input v-model="associateForm.name" placeholder="请输入设备名称" maxlength="30"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="center">
          <el-col :span="24">
            <el-form-item label="归属机构" porp="orgId">
              <treeselect style="width: 240px" v-model="associateForm.orgId" :options="orgOptions" :show-count="true"
                          placeholder="请选择归属机构"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAssociateForm">确 定</el-button>
        <el-button @click="associateOpen = false">取 消</el-button>
      </div>
    </el-dialog>
    <!--    设备配对弹窗-->
    <el-dialog title="分配设备组" :visible.sync="groupArrangeOpen" center width="400px">
      <el-form ref="groupArrangeForm" :model="groupArrangeForm" :rules="groupArrangeRules" label-width="80px">
        <el-row type="flex" justify="center">
          <el-col :span="24">
            <el-form-item label="设备组" porp="groupArrangeGroupId">
              <el-select v-model="groupArrangeForm.groupArrangeGroupId" placeholder="请输入设备设备组" clearable>
                <el-option
                  v-for="item in groupArrangeGroupList"
                  :key="item.deviceGroupId"
                  :label="item.name"
                  :value="item.deviceGroupId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitGroupArrangeForm">确 定</el-button>
        <el-button @click="groupArrangeOpen = false">取 消</el-button>
      </div>
    </el-dialog>
    <radar-wave-settings v-model="settings" :visible.sync="radarWaveSettingsOpen" @submit="settingsSubmit"></radar-wave-settings>
    <watch-settings v-model="settings" :visible.sync="watchSettingsOpen" @submit="settingsSubmit"></watch-settings>
    <el-dialog
      title="提示"
      :visible.sync="offlineDialogOpen"
      width="30%"
      center>
      <span>下线后设备将不再触发事件</span>
      <span slot="footer" class="dialog-footer">
    <el-button @click="offlineDialogOpen = false">取 消</el-button>
    <el-button type="primary" @click="offLine">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import {getUserProfile, orgTreeSelect} from "@/api/system/user";
import {pageDevice, associateDevice, groupArrange, active, offline, loadSettings, setSettings} from "@/api/device/device";
import {pageDeviceGroup} from "@/api/org/deviceGroup"
import Treeselect from "@riophae/vue-treeselect";
import RadarWaveSettings from "@/views/device/components/RadarWaveSettings";
import WatchSettings from "@/views/device/components/WatchSettings";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Device",
  dicts: ['device_type','sys_device_status'],
  components: {Treeselect, RadarWaveSettings, WatchSettings},
  data() {
    return {
      // 遮罩层
      loading: true,
      showSearch: true,
      distributeFlagDict: [{label: '已分配', value: 1}, {label: '未分配', value: 0}],
      // 机构树选项
      orgOptions: [],
      total: 0,
      deviceList: [],
      orgType:"1",
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        no: undefined,
        name: undefined,
        type: undefined,
        orgId: undefined,
        distributeFlag: undefined,
        status: undefined
      },
      /** 设备配对相关 */
      associateOpen: false,
      associateRules: {
        location: [
          {required: true, message: "设备位置不能为空", trigger: "blur"}
        ],
        name: [
          {required: true, message: "设备名称不能为空", trigger: "blur"}
        ],
        orgId: [
          {required: true, message: "请选择机构", trigger: "blur"}
        ],
      },
      associateForm: {
        location: undefined,
        name: undefined,
        orgId: undefined,
      },
      /** 设备分组相关 */
      groupArrangeOpen: false,
      groupArrangeForm: {
        groupArrangeGroupId: undefined,
        groupArrangeDeviceId: undefined
      },
      groupArrangeRules: {},
      groupArrangeGroupList: [],
      currentDistributeFlag: undefined,
      currentStatus: undefined,
      currentOrgId: undefined,
      /** 设备配置相关 */
      //雷达波设置页面开启
      radarWaveSettingsOpen: false,
      //监护设备设置页面开启
      watchSettingsOpen: false,
      settingsDeviceId: undefined,
      settings:undefined,
      offLineRow:undefined,
      offlineDialogOpen:false
    }
  },
  computed:{
    columns(){
      return [
        {key: 0, label: `设备名称`, prop: `name`, visible: true},
        {key: 1, label: `设备编号`, prop: `no`, visible: true},
        {key: 2, label: `位置`, prop: `location`, visible: true},
        {key: 3, label: `设备分组`, prop: `deviceGroupName`, visible: this.deviceGroupNameVisible},
        {
          key: 4,
          label: `是否已分配`,
          prop: `distributeFlag`,
          visible: true,
          formatter: (row, column, cellValue) => this.distributeFlagDict.find(x => cellValue == x.value)?.label
        },
        {key: 5, label: `设备配网时间`, prop: `registerTime`, visible: true},
        {
          key: 6,
          label: `设备类型`,
          prop: `type`,
          visible: true,
          formatter: (row, column, cellValue) => this.dict.type.device_type.find(x => cellValue == x.value)?.label
        },
        {
          key: 7,
          label: `设备状态`,
          prop: `status`,
          visible: true,
          formatter: (row, column, cellValue) => this.dict.type.sys_device_status.find(x => cellValue == x.value)?.label
        },
        {key: 8, label: `机构名称`, prop: `orgName`, visible: true},
        {key: 9, label: `所属会员`, prop: `memberPhone`, visible: this.memberPhoneVisible},
      ]
    },
    memberPhoneVisible(){
      return this.orgType == "1"
    },
    deviceGroupNameVisible(){
      return this.orgType != "1"
    }
  },
  async created() {
    const {data:userProfile} = await getUserProfile();
    this.orgType = userProfile?.org?.type || "0";
    this.currentDistributeFlag = this.$route.query.distributeFlag==undefined?undefined:Number(this.$route.query.distributeFlag)
    this.currentStatus = this.$route.query.status==undefined?undefined:this.$route.query.status
    this.currentOrgId = this.$route.query.orgId==undefined?undefined:Number(this.$route.query.orgId)
    this.getDeptTree();
    this.resetQuery();
  },
  // activated() {
  //   this.currentDistributeFlag = this.$route.query.distributeFlag==undefined?undefined:Number(this.$route.query.distributeFlag)
  //   this.currentOrgId = this.$route.query.orgId==undefined?undefined:Number(this.$route.query.orgId)
  //   this.getDeptTree();
  //   this.resetQuery();
  // },
  watch: {
    "$route.query.distributeFlag": {
      immediate: true,
      handler: function (val) {
        if (this.$route.name == "Device") {
          if (this.currentDistributeFlag != val) {
            this.currentDistributeFlag = val==undefined?undefined:Number(val);
            this.currentStatus = this.$route.query.status==undefined?undefined:this.$route.query.status
            this.currentOrgId = this.$route.query.orgId==undefined?undefined:Number(this.$route.query.orgId)
            this.resetQuery();
          }
        }
      }
    },
    "$route.query.orgId": {
      immediate: true,
      handler: function (val) {
        if (this.$route.name == "Device") {
          if (this.currentOrgId != val) {
            this.currentOrgId = val;
            this.currentStatus = this.$route.query.status==undefined?undefined:this.$route.query.status
            this.currentDistributeFlag = this.$route.query.distributeFlag==undefined?undefined:Number(this.$route.query.distributeFlag)
            this.resetQuery();
          }
        }
      }
    },
    "$route.query.status": {
      immediate: true,
      handler: function (val) {
        if (this.$route.name == "Device") {
          if (this.currentOrgId != val) {
            this.currentStatus = val
            this.currentOrgId = this.$route.query.orgId==undefined?undefined:Number(this.$route.query.orgId)
            this.currentDistributeFlag = this.$route.query.distributeFlag==undefined?undefined:Number(this.$route.query.distributeFlag)
            this.resetQuery();
          }
        }
      }
    }
  },
  methods: {
    /** 查询设备列表 */
    getList() {
      this.loading = true;
      pageDevice(this.queryParams).then(response => {
          this.deviceList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 查询机构下拉树结构 */
    async getDeptTree() {
      const response = await orgTreeSelect()
      this.orgOptions = response.data;
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
        no: undefined,
        name: undefined,
        type: undefined,
        orgId: this.currentOrgId,
        distributeFlag: this.currentDistributeFlag,
        status: this.currentStatus
      }
      // this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 设备配对相关 */
    handleAssociate(row) {
      this.associateOpen = true;
      this.associateForm = row;
      this.resetForm("associateForm");
    },
    submitAssociateForm() {
      this.$refs["associateForm"].validate(valid => {
        if (valid) {
          associateDevice(this.associateForm).then(response => {
            this.$modal.msgSuccess("配对成功");
            this.associateOpen = false;
            this.getList();
          })
        }
      })

    },
    /** 分配设备组相关 */
    handleGroupArrange(row) {
      this.groupArrangeOpen = true;
      this.groupArrangeForm = {
        groupArrangeGroupId: row.devicegroupId,
        groupArrangeDeviceId: row.deviceId
      }
      const queryForm = {
        pageNo: 1,
        pageSize: 1000,
        orgId: row.orgId
      }
      this.groupArrangeGroupList = []
      pageDeviceGroup(queryForm).then(response => {
        this.groupArrangeGroupList = response.rows
      })
    },
    submitGroupArrangeForm() {
      this.$refs["groupArrangeForm"].validate(valid => {
        if (valid) {
          groupArrange(this.groupArrangeForm.groupArrangeDeviceId, this.groupArrangeForm.groupArrangeGroupId).then(response => {
            this.$modal.msgSuccess("设备分组成功");
            this.groupArrangeOpen = false;
            this.getList();
          })
        }
      })
    },
    offlineDialog(row){
      this.offLineRow = row;
      this.offlineDialogOpen = true;
    },
    offLine(){
      offline(this.offLineRow.deviceId).then(response=>{
        this.offlineDialogOpen = false;
        this.$modal.msgSuccess("设备下线成功");
        this.getList();

      })
    },
    active(row){
      active(row.deviceId).then(response=>{
        this.$modal.msgSuccess("设备上线成功");
        this.getList();
      })
    },
    /** 设备参数配置相关 */
    async handleSettings(row) {
      const {data} = await loadSettings(row.deviceId);
      this.settingsDeviceId = row.deviceId;
      this.settings = data;
      if (row.type == 0) {
        this.radarWaveSettingsOpen = true;
      } else if (row.type == 1) {
        this.watchSettingsOpen = true;
      } else {
        this.$modal.msgWarning("未知类型的设备");
      }
    },
    async settingsSubmit(form){
      await setSettings(this.settingsDeviceId,form)
      this.radarWaveSettingsOpen = false;
      this.watchSettingsOpen = false;
      this.$modal.msgSuccess("设备参数配置成功");
      this.getList();
    }

  }
}
</script>
