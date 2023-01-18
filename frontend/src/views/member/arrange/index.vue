<template>
  <div class="app-container home">
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
      <el-transfer v-loading="notArrangeMemberListLoading||arrangeMemberListLoading" v-model="arrangeMemberIds"
                   :titles="['待分配会员','已分配会员']" :data="membersData"></el-transfer>
    </el-row>
    <el-row type="flex" justify="center">
      <el-button type="primary" @click="submitMemberArrange">提 交</el-button>
      <el-button @click="close">取 消</el-button>
    </el-row>
  </div>
</template>

<script>
//会员
import {memberArrange, listMember} from "@/api/member/member"
//运营人员
import {getUserProfile, getUser, pageUser} from "@/api/system/user";
import {getOrg} from "@/api/org/org";


export default {
  name: "MemberArrange",
  dicts: ['sys_normal_disable', 'sys_user_sex'],
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
          roleKey: "biz"
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
      /** 分配会员相关 */
      arrangeMemberListLoading: false,
      notArrangeMemberListLoading: false,
      arrangeMemberList: [],
      notArrangeMemberList: [],
      arrangeMemberIds: []
    }
  },
  computed: {
    orgId() {
      return this.$route.query.orgId || this.userOrgId
    },
    /** 会员相关 */
    membersData() {
      return [...this.arrangeMemberList, ...this.notArrangeMemberList].map(x => {
        return {key: x.memberId, label: x.phone, disabled: false}
      });
    }
  },
  async created() {
    await this.initUserOrg();
    await this.initOrg();
    this.currentOrgId = this.orgId
    this.userQueryParams.orgId = this.orgId;
    if(this.orgId == null || this.orgId == undefined){
      this.userQueryParams.params.orgIdIsNull = true;
    }else{
      delete this.userQueryParams.params.orgIdIsNull;
    }
    await this.getUserList();
    this.deviceGroupUserId = this.userList[0]?.userId;

    this.loadArrangeMemberList();
    this.loadNotArrangeMemberList();
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
            this.loadArrangeMemberList();
            this.loadNotArrangeMemberList();
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

      this.loadArrangeMemberList();
      this.loadNotArrangeMemberList();
    },
    close() {
      this.$tab.closePage();
    },
    /** 分配会员相关 */
    loadArrangeMemberList() {
      let queryParams = {
        pageNum: 1,
        pageSize: 10000,
        userId: this.deviceGroupUserId,
        params: {
          arrangeUser: true
        }
      }
      this.arrangeMemberListLoading = true;
      listMember(queryParams).then(response => {
        this.arrangeMemberListLoading = false;
        this.arrangeMemberList = response.rows;
        this.arrangeMemberIds = response.rows.map(x => x.memberId);
      })
    },
    loadNotArrangeMemberList() {
      let queryParams = {
        pageNum: 1,
        pageSize: 10000,
        params: {
          arrangeUser: false
        },
        // orgId:this.orgId
      }
      this.notArrangeMemberListLoading = true;
      listMember(queryParams).then(response => {
        this.notArrangeMemberListLoading = false
        this.notArrangeMemberList = response.rows;
      })
    },
    async submitMemberArrange() {
      // 先移出
      const removeMemberIds = this.arrangeMemberList
        .map(x => x.memberId)
        .filter(x => !this.arrangeMemberIds.includes(x))
      if (removeMemberIds.length > 0) {
        await memberArrange(null, removeMemberIds)
      }
      // 再移入
      if (this.arrangeMemberIds.length > 0) {
        await memberArrange(this.deviceGroupUserId, this.arrangeMemberIds)
      }
      this.memberArrangeOpen = false
      this.$modal.msgSuccess("分配会员成功");
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
