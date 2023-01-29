<template>
  <div class="app-container">
    <el-row>
      <org-info-card :value="orgId"></org-info-card>
    </el-row>
    <el-row>
      <el-card class="box-card">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-position="left">
              <el-form-item label="设备组名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入设备组名称" maxlength="50"/>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="12">
            <el-button type="primary" @click="submitForm">添加</el-button>
          </el-col>
        </el-row>
      </el-card>
    </el-row>
    <el-row>
      <water-fall v-loading="loading"
                  :value="deviceGroupList">
        <template slot-scope="{item}">
          <el-card shadow="hover" class="card-item" >
            {{item.name}} | <el-tooltip :content="item.no" placement="top"><span>{{subString(item)}}</span></el-tooltip>
            <br>
           运营人员：{{item.user.name}}
            <br>
            <el-row :gutter="10">
              <el-col :span="12">
                <el-button
                  type="primary"
                  @click="arrangeUser(item)"
                  v-hasPermi="['org:deviceGroup:arrangeUser']"
                >修改运营人员
                </el-button>
              </el-col>
              <el-col :span="12">
                <el-button
                  type="primary"
                  @click="deviceArrange(item)"
                  v-hasPermi="['org:deviceGroup:deviceArrange']"
                >分配设备
                </el-button>
              </el-col>
            </el-row>
          </el-card>
        </template>
      </water-fall>
<!--      <el-card class="box-card">-->
<!--        <el-table-->
<!--          v-loading="loading"-->
<!--          :data="deviceGroupList"-->
<!--          row-key="deviceGroupId"-->
<!--        >-->
<!--          <el-table-column label="设备组名称" key="0" prop="name" align="center"></el-table-column>-->
<!--          <el-table-column label="设备组编号" key="1" prop="no" align="center"></el-table-column>-->
<!--          <el-table-column label="运营人员" key="2" prop="user.name" align="center"></el-table-column>-->
<!--          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--            <template slot-scope="scope">-->

<!--            </template>-->
<!--          </el-table-column>-->
<!--        </el-table>-->
<!--      </el-card>-->
    </el-row>


    <form-panel title="修改运营人员" :visible.sync="arrangeUserOpen">
      <el-row>
        <device-group-info-card :value="deviceGroupId"></device-group-info-card>
      </el-row>
      <el-row>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>选择设备组的运营人员</span>
          </div>
          <el-table ref="userTable" v-loading="userLoading" highlight-current-row
                    :data="userList" @current-change="handleSelectionChange">
            <el-table-column label="序号" align="center" width="65">
              <template slot-scope="scope">
                <el-radio :label="scope.row.userId" v-model="deviceGroupUserId"
                          @change.native="$refs.userTable.setCurrentRow(scope.row)">{{ scope.$index + 1 }}
                </el-radio>
              </template>
            </el-table-column>
            <el-table-column v-for="column in userColumns" :label="column.label" :key="column.key" :prop="column.prop"
                             v-if="column.visible" :formatter="column.formatter"
                             :show-overflow-tooltip="column.showOverFlowToolTip == undefined?true:column.showOverFlowToolTip">
            </el-table-column>
          </el-table>

          <pagination
            v-show="total>0"
            :total="total"
            :page.sync="userQueryParams.pageNum"
            :limit.sync="userQueryParams.pageSize"
            @pagination="getList"
          />
          <el-row type="flex" justify="center">
            <el-button type="primary" @click="submitArrangeUser">提 交</el-button>
            <el-button @click="arrangeUserOpen = false">取 消</el-button>
          </el-row>
        </el-card>
      </el-row>
    </form-panel>
    <form-panel title="设备分配设备组" :visible.sync="deviceArrangeOpen">
      <el-row type="flex" justify="center">
        <device-group-info-card :value="deviceGroupId"></device-group-info-card>
      </el-row>
      <el-row type="flex" justify="center">
        <el-transfer v-loading="notArrangeDeviceListLoading||arrangedDeviceListLoading" v-model="arrangedDeviceIds" :titles="['待分配设备','已分配设备']" :data="devicesData"></el-transfer>
      </el-row>
      <el-row type="flex" justify="center">
        <el-button type="primary" @click="submitDeviceArrange">提 交</el-button>
        <el-button @click="deviceArrangeOpen = false">取 消</el-button>
      </el-row>
    </form-panel>
  </div>
</template>

<script>
import {pageDeviceGroup, addDeviceGroup, updateDeviceGroup, arrangeUser, deviceArrange} from "@/api/org/deviceGroup"
import {pageDevice} from "@/api/device/device"
import {getUserProfile, getUser, pageUser} from "@/api/system/user";
import OrgInfoCard from "@/views/org/components/OrgInfoCard";
import DeviceGroupInfoCard from "@/views/org/components/DeviceGroupInfoCard";

export default {
  components: {OrgInfoCard, DeviceGroupInfoCard},
  name: "DeviceGroup",
  dicts: ['sys_normal_disable', 'sys_user_sex'],
  data() {
    return {
      userOrgId: undefined,
      currentOrgId: undefined,
      form: {},
      rules: {
        name: [
          {required: true, message: "设备组名称不能为空", trigger: "blur"}
        ]
      },
      deviceGroupList: [],
      total: 0,
      loading: false,
      arrangeUserOpen: false,
      deviceArrangeOpen: false,
      deviceGroupId: undefined,

      /**修改运营人员相关属性*/
      userLoading:false,
      userList: [],
      userTotal: 0,
      deviceGroupUserId: undefined,
      userQueryParams: {
        pageNum: 1,
        pageSize: 10,
        orgId: undefined,
        params:{
          roleKey:"biz",
          orgIdStrict:true
        }
      },
      // 用户列信息
      userColumns: [
        {key: 0, label: `用户编号`, prop: `userId`, visible: false},
        {key: 1, label: `用户昵称`, prop: `nickName`, visible: false},
        {key: 2, label: `姓名`, prop: `name`, visible: true},
        {
          key: 3,
          label: `性别`,
          prop: `sex`,
          visible: true,
          formatter: (row, column, cellValue) => this.dict.type.sys_user_sex?.find(x => x.value == cellValue)?.label
        },
        {key: 4, label: `所属机构`, prop: `org.orgName`, visible: true},
        {key: 5, label: `登录账号`, prop: `userName`, visible: true},
        {key: 6, label: `手机`, prop: `mobilePhone`, visible: true},
        {
          key: 7,
          label: `角色`,
          prop: `roles`,
          visible: true,
          formatter: (row, column, cellValue) => cellValue?.map(x => x.roleName).join(",")
        },
        {
          key: 8,
          label: `状态`,
          prop: `status`,
          visible: true,
          formatter: (row, column, cellValue) => this.dict.type.sys_normal_disable?.find(x => x.value == cellValue)?.label
        },
        {
          key: 9,
          label: `创建时间`,
          prop: `createTime`,
          visible: false,
          formatter: (row, column, cellValue) => this.parseTime(cellValue)
        }
      ],
      /** 分配设备相关 */
      arrangedDeviceListLoading:false,
      notArrangeDeviceListLoading:false,
      arrangedDeviceList: [],
      notArrangeDeviceList: [],
      arrangedDeviceIds: []
    }
  },
  computed: {
    orgId() {
      return this.$route.query.orgId || this.userOrgId
    },
    /** 分配设备相关 */
    devicesData() {
      return [...this.arrangedDeviceList, ...this.notArrangeDeviceList].map(x => {
        return {key: x.deviceId, label: x.status == 0?(x.name+'(未激活)'):x.name,disabled:x.status == 0}
      });
    }
  },
  async created() {
    await this.initUserOrg();
    this.getList();
    this.reset();
  },
  watch: {
    "$route.query.orgId": {
      immediate: true,
      handler: function (val) {
        if (this.$route.name == "DeviceGroup") {
          if (this.currentOrgId != this.orgId) {
            this.currentOrgId = this.orgId
            this.getList();
            this.reset();
            this.arrangeUserOpen = false;
            this.deviceArrangeOpen = false;
          }
        }
      }
    }
  },
  methods: {
    async initUserOrg() {
      const response = await getUserProfile();
      this.userOrgId = response.data.orgId;
    },
    reset() {
      this.form = {
        orgId: this.orgId,
        name: undefined
      }
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addDeviceGroup(this.form).then(response => {
            this.reset();
            this.$modal.msgSuccess("新增成功");
            this.getList()
          })
        }
      })
    },
    getList() {
      this.loading = true;
      const queryParams = {
        orgId: this.orgId,
        pageSize: 10000,
        params:{}
      }
      if(queryParams.orgId == null){
        queryParams.params.orgIdIsNull = true;
      }
      pageDeviceGroup(queryParams).then(response => {
        this.deviceGroupList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /* 分配运营人员*/
    arrangeUser(row) {
      this.arrangeUserOpen = true;
      this.deviceGroupId = row.deviceGroupId;
      this.deviceGroupUserId = row.userId;
      this.userQueryParams.orgId = row.orgId;
      this.getUserList()
    },
    /* 分配设备 */
    deviceArrange(row) {
      this.deviceArrangeOpen = true;
      this.deviceGroupId = row.deviceGroupId;
      this.loadArrangedDeviceList()
      this.loadNotArrangeDeviceList()
    },
    /** 分配用户方法 */
    getUserList() {
      this.userLoading = true;
      pageUser(this.userQueryParams).then(response => {
          this.userList = response.rows;
          this.userTotal = response.total;
          this.userLoading = false;
        }
      );
    },
    handleSelectionChange(currentRow) {
      this.deviceGroupUserId = currentRow.userId;
    },
    submitArrangeUser() {
      arrangeUser([this.deviceGroupId], this.deviceGroupUserId)
        .then(response => {
          this.arrangeUserOpen = false;
          this.getList();
          this.$modal.msgSuccess("修改运营人员成功");
        })
    },
    /** 分配设备相关 */
    loadArrangedDeviceList() {
      let queryParams = {
        pageNum: 1,
        pageSize: 10000,
        devicegroupId: this.deviceGroupId
      }
      this.arrangedDeviceListLoading = true;
      pageDevice(queryParams).then(response => {
        this.arrangedDeviceListLoading = false;
        this.arrangedDeviceList = response.rows;
        this.arrangedDeviceIds = response.rows.map(x => x.deviceId);
      })
    },
    loadNotArrangeDeviceList() {
      let queryParams = {
        pageNum: 1,
        pageSize: 10000,
        params: {
          arrangeDeviceGroup: false
        },
        orgId:this.orgId
      }
      this.notArrangeDeviceListLoading = true;
      pageDevice(queryParams).then(response => {
        this.notArrangeDeviceListLoading = false
        this.notArrangeDeviceList = response.rows;
      })
    },
    async submitDeviceArrange() {
      // 先移出
      const removeDeviceIds = this.arrangedDeviceList
        .map(x => x.deviceId)
        .filter(x => !this.arrangedDeviceIds.includes(x))
      if(removeDeviceIds.length>0){
        await deviceArrange(null,removeDeviceIds)
      }
      // 再移入
      if(this.arrangedDeviceIds.length>0){
        await deviceArrange(this.deviceGroupId,this.arrangedDeviceIds)
      }
      this.deviceArrangeOpen = false
      this.$modal.msgSuccess("分配设备成功");
    },
    subString(item){
      const length = 11;
      let result = item?.no || ""
      if(result.length>length){
        return (result).slice(0,8)+"..."
      }
      return result
    }
  }
}
</script>
<style scoped>
.el-row {
  margin-bottom: 20px;

&
:last-child {
  margin-bottom: 0;
}

}
</style>
