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
          <el-form-item label="设备名称" prop="orgId">
            <el-input
              v-model="queryParams.name"
              placeholder="设备名称"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="设备类型" prop="status">
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
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

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
              >设备配对
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-circle-check"
                @click="handleGroupArrange(scope.row)"
                v-hasPermi="['device:groupArrange']"
              >分配设备组
              </el-button>
              <el-button
                v-if="scope.row.status != '1'"
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
                @click="offLine(scope.row)"
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
    <el-dialog title="设备配对" :visible.sync="associateOpen" center>
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
  </div>
</template>

<script>
import {orgTreeSelect} from "@/api/system/user";
import {pageDevice, associateDevice, groupArrange,active,offline} from "@/api/device/device";
import {pageDeviceGroup} from "@/api/org/deviceGroup"
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Device",
  dicts: ['device_type'],
  components: {Treeselect},
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
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        no: undefined,
        name: undefined,
        type: undefined,
        orgId: undefined,
        distributeFlag: undefined
      },
      columns: [
        {key: 0, label: `设备名称`, prop: `name`, visible: true},
        {key: 1, label: `设备编号`, prop: `no`, visible: true},
        {key: 2, label: `位置`, prop: `location`, visible: true},
        {key: 3, label: `设备分组`, prop: `deviceGroupName`, visible: true},
        {
          key: 4,
          label: `是否已分配`,
          prop: `distributeFlag`,
          visible: true,
          formatter: (row, column, cellValue) => this.distributeFlagDict.find(x => cellValue == x.value)?.label
        },
        {key: 5, label: `设备登记时间`, prop: `registerTime`, visible: true},
        {
          key: 6,
          label: `设备类型`,
          prop: `type`,
          visible: true,
          formatter: (row, column, cellValue) => this.dict.type.device_type.find(x => cellValue == x.value)?.label
        },
        {key: 7, label: `机构名称`, prop: `orgName`, visible: true},
      ],
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
      currentOrgId:undefined,

    }
  },
  created() {
    this.currentDistributeFlag = this.$route.query.distributeFlag==undefined?undefined:Number(this.$route.query.distributeFlag)
    this.currentOrgId = this.$route.query.orgId==undefined?undefined:Number(this.$route.query.orgId)
    this.getDeptTree();
    this.resetQuery();
  },
  watch: {
    "$route.query.distributeFlag": {
      immediate: true,
      handler: function (val) {
        if (this.$route.name == "Device") {
          if (this.currentDistributeFlag != val) {
            this.currentDistributeFlag = val==undefined?undefined:Number(val);
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
        distributeFlag: this.currentDistributeFlag
      }
      // this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 设备配对相关 */
    handleAssociate(row) {
      this.associateOpen = true;
      // this.associateForm = row;
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
    offLine(row){
      offline(row.deviceId).then(response=>{
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
    handleSettings(row) {

    }

  }
}
</script>
