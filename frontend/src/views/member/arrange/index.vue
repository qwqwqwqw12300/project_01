<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h1>运营分配</h1>
        <!-- 配合按钮弹窗 -->
        <!-- <form-panel title="设备分配设备组" :visible.sync="deviceArrangeOpen"> -->
          <!-- <el-row type="flex" justify="center">
            <device-group-info-card :value="deviceGroupId"></device-group-info-card>
          </el-row> -->
          <!-- <el-row type="flex" justify="center">
            <el-transfer v-loading="notArrangeMemberListLoading||arrangeMemberListLoading" v-model="arrangeMemberIds" :titles="['待分配会员','已分配会员']" :data="membersData"></el-transfer>
          </el-row> -->

  <!-- <el-transfer
    filterable
    :filter-method="filterMethod"
    filter-placeholder="请输入城市拼音"
    v-model="value"
    :data="data">
  </el-transfer> -->
  <el-row type="flex" justify="center">
        <el-transfer v-loading="notArrangeMemberListLoading||arrangeMemberListLoading" v-model="arrangeMemberIds" :titles="['待分配会员','已分配会员']" :data="membersData"></el-transfer>
      </el-row>

          <el-row type="flex" justify="center">
            <el-button type="primary" @click="submitMemberArrange">提 交</el-button>
            <el-button @click="memberArrangeOpen = false">取 消</el-button>
          </el-row>

      </el-col>
    </el-row>
  </div>
</template>

<script>
//会员
import {memberArrange,listMember} from "@/api/member/member"
//运营人员
import {getUserProfile, getUser, pageUser} from "@/api/system/user";





export default {
    name: "MemberArrange",
    // dicts: ['sys_normal_disable', 'sys_user_sex'],

    data() {
      return {

      form: {},
      rules: {
      },

      // 用户列信息
      // userColumns: [
      //   {key: 0, label: `用户编号`, prop: `userId`, visible: false},
      //   {key: 1, label: `用户昵称`, prop: `nickName`, visible: false},
      //   {key: 2, label: `姓名`, prop: `name`, visible: true},
      //   {
      //     key: 3,
      //     label: `性别`,
      //     prop: `sex`,
      //     visible: true,
      //     formatter: (row, column, cellValue) => this.dict.type.sys_user_sex?.find(x => x.value == cellValue)?.label
      //   },
      //   {key: 4, label: `所属机构`, prop: `org.orgName`, visible: true},
      //   {key: 5, label: `登录账号`, prop: `userName`, visible: true},
      //   {key: 6, label: `手机`, prop: `mobilePhone`, visible: true},
      //   {
      //     key: 7,
      //     label: `角色`,
      //     prop: `roles`,
      //     visible: true,
      //     formatter: (row, column, cellValue) => cellValue?.map(x => x.roleName).join(",")
      //   },
      //   {
      //     key: 8,
      //     label: `状态`,
      //     prop: `status`,
      //     visible: true,
      //     formatter: (row, column, cellValue) => this.dict.type.sys_normal_disable?.find(x => x.value == cellValue)?.label
      //   },
      //   {
      //     key: 9,
      //     label: `创建时间`,
      //     prop: `createTime`,
      //     visible: false,
      //     formatter: (row, column, cellValue) => this.parseTime(cellValue)
      //   }
      // ],
      /** 分配会员相关 */
      arrangeMemberListLoading:false,
      notArrangeMemberListLoading:false,
      arrangeMemberList: [],
      notArrangeMemberList: [],
      arrangeMemberIds: []
    }
  },
  computed: {

    /** 分配会员相关 */
    membersData() {
      return [...this.arrangeMemberList, ...this.notArrangeMemberList].map(x => {
        return {key: x.memberId, label: x.memberId}
      });
    }
  },

  watch: {
  },
  methods: {

    reset() {

    },
    submitForm() {

    },
    getList() {

    },

    /* 分配会员相关 */
    memberArrange(row) {
      this.memberArrangeOpen = true;
      this.userId = row.userId;
      this.loadArrangeMemberList()
      this.loadNotArrangeMemberList()
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

    /** 分配会员相关 */
    loadArrangeMemberList() {
      let queryParams = {
        pageNum: 1,
        pageSize: 10000,
        userId: 1
        // userId: this.userId

      }
      this.arrangedMemberListLoading = true;
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
          arrangeMemberGroup: false
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
      if(removeMemberIds.length>0){
        await memberArrange(null,removeMemberIds)
      }
      // 再移入
      if(this.arrangeMemberIds.length>0){
        await memberArrange(this.userId,this.arrangeMemberIds)
      }
      this.memberArrangeOpen = false
      this.$modal.msgSuccess("分配会员成功");
    }
  }
}
</script>


