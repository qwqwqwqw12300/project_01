<template>
  <div class="app-container">
    <el-row>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>运营人员列表</span>
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
          v-show="userTotal>0"
          :total="userTotal"
          :page.sync="userQueryParams.pageNum"
          :limit.sync="userQueryParams.pageSize"
          @pagination="getUserList"
        />
      </el-card>
    </el-row>
    <el-row type="flex" justify="center">
      <el-transfer v-loading="notArrangeDeviceGroupListLoading||arrangedDeviceGroupListLoading" v-model="arrangedDeviceGroupIds" :titles="['待分配设备组','已分配设备组']" :data="deviceGroupsData"></el-transfer>
    </el-row>
    <el-row type="flex" justify="center">
      <el-button type="primary" @click="submitDeviceGroupArrange" :disabled="this.deviceGroupUserOrgId == undefined">提 交</el-button>
      <el-button @click="close">取 消</el-button>
    </el-row>
  </div>
</template>

<script>
import {pageDeviceGroup, addDeviceGroup, updateDeviceGroup, arrangeUser, deviceArrange} from "@/api/org/deviceGroup"
import {getOrg} from "@/api/org/org";
import OrgInfoCard from "@/views/org/components/OrgInfoCard";
import {getUserProfile, pageUser} from "@/api/system/user";
import {pageDevice} from "@/api/device/device";

export default {
  name: "DeviceGroupArrangeUser",
  components: {OrgInfoCard},
  dicts: ['sys_user_sex','sys_normal_disable'],
  data() {
    return {
      userOrgId: undefined,
      currentOrgId: undefined,
      org: undefined,
      userQueryParams: {
        pageNum: 1,
        pageSize: 10,
        orgId: undefined,
        params: {
          roleKey: "biz",
          orgIdStrict: true
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
          visible: false,
          formatter: (row, column, cellValue) => this.dict.type.sys_user_sex?.find(x => x.value == cellValue)?.label
        },
        {key: 4, label: `所属机构`, prop: `org.orgName`, visible: true},
        {key: 5, label: `登录账号`, prop: `userName`, visible: true},
        {key: 6, label: `手机`, prop: `mobilePhone`, visible: true},
        {
          key: 7,
          label: `角色`,
          prop: `roles`,
          visible: false,
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
      userLoading: false,
      userList: [],
      userTotal: 0,
      deviceGroupUserId: undefined,
      deviceGroupUserOrgId: undefined,
      /** 分配设备组相关 */
      arrangedDeviceGroupListLoading:false,
      notArrangeDeviceGroupListLoading:false,
      arrangedDeviceGroupList: [],
      notArrangeDeviceGroupList: [],
      arrangedDeviceGroupIds: []
    }
  },
  async created() {
    await this.initUserOrg();
    await this.initOrg();
    if(this.orgId == null || this.orgId == undefined){
      this.userQueryParams.params.orgIdIsNull = true;
    }else{
      delete this.userQueryParams.params.orgIdIsNull;
    }
    await this.getUserList();
    this.deviceGroupUserId = this.userList[0]?.userId;
    this.deviceGroupUserOrgId = this.userList[0]?.orgId;
    this.userQueryParams.orgId = this.orgId;
    this.loadArrangedDeviceGroupList();
    this.loadNotArrangeDeviceList();
  },
  computed: {
    orgId() {
      return this.$route.query.orgId || this.userOrgId
    },
    /** 分配设备组相关 */
    deviceGroupsData() {
      return [...this.arrangedDeviceGroupList, ...this.notArrangeDeviceGroupList].map(x => {
        return {key: x.deviceGroupId, label: x.name,disabled:false}
      });
    }
  },
  watch: {
    "$route.query.orgId": {
      immediate: true,
      handler: async function (val) {
        if (this.$route.name == "DeviceGroupArrangeUser") {
          if (this.currentOrgId != this.orgId) {
            this.currentOrgId = this.orgId
            this.userQueryParams.orgId = this.orgId;
            if(this.orgId == null || this.orgId == undefined){
              this.userQueryParams.params.orgIdIsNull = true;
            }else{
              delete this.userQueryParams.params.orgIdIsNull;
            }
            await this.getUserList();
            this.deviceGroupUserId = this.userList[0]?.userId;
            this.deviceGroupUserOrgId = this.userList[0]?.orgId;
            this.loadArrangedDeviceGroupList();
            this.loadNotArrangeDeviceList();
          }
        }
      }
    }
  },
  methods: {
    async initOrg() {
      if (this.orgId != undefined) {
        const response = await getOrg(this.orgId);
        this.org = response.data;
      }
    },
    async initUserOrg() {
      const response = await getUserProfile();
      this.userOrgId = response.data.orgId;
    },
    /** 分配用户方法 */
    async getUserList() {
      this.userLoading = true;
      const response = await pageUser(this.userQueryParams);
      this.userList = response.rows;
      this.userTotal = response.total;
      this.userLoading = false;
    },
    handleSelectionChange(currentRow) {
      this.deviceGroupUserId = currentRow.userId;
      this.deviceGroupUserOrgId = currentRow.orgId;
    },
    close() {
      this.$tab.closePage();
    },
    /** 分配设备组相关 */
    loadArrangedDeviceGroupList() {
      let queryParams = {
        pageNum: 1,
        pageSize: 10000,
        userId: this.deviceGroupUserId
      }
      if(this.deviceGroupUserId == undefined){
        this.arrangedDeviceGroupList = [];
        this.arrangedDeviceGroupIds = [];
      }else{
        this.arrangedDeviceGroupListLoading = true;
        pageDeviceGroup(queryParams).then(response => {
          this.arrangedDeviceGroupListLoading = false;
          this.arrangedDeviceGroupList = response.rows;
          this.arrangedDeviceGroupIds = response.rows.map(x => x.deviceGroupId);
        })}

    },
    loadNotArrangeDeviceList() {
      let queryParams = {
        pageNum: 1,
        pageSize: 10000,
        params: {
          arrangeUser: false,
          orgIdIsNull:false
        },
        orgId:this.orgId
      }
      if(this.orgId == undefined || this.orgId == null){
        queryParams.params.orgIdIsNull = true;
      }
      this.notArrangeDeviceGroupListLoading = true;
      pageDeviceGroup(queryParams).then(response => {
        this.notArrangeDeviceGroupListLoading = false
        this.notArrangeDeviceGroupList = response.rows;
      })
    },
    async submitDeviceGroupArrange() {
      // 先移出
      const removeDeviceGroupIds = this.arrangedDeviceGroupList
        .map(x => x.deviceGroupId)
        .filter(x => !this.arrangedDeviceGroupIds.includes(x))
      if(removeDeviceGroupIds.length>0){
        await arrangeUser(removeDeviceGroupIds,null)
      }
      // 再移入
      if(this.arrangedDeviceGroupIds.length>0){
        await arrangeUser(this.arrangedDeviceGroupIds,this.deviceGroupUserId)
      }
      this.$modal.msgSuccess("分配组设备成功");
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
