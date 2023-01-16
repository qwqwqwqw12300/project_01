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

  <el-transfer
    filterable
    :filter-method="filterMethod"
    filter-placeholder="请输入城市拼音"
    v-model="value"
    :data="data">
  </el-transfer>

          <el-row type="flex" justify="center">
            <el-button type="primary" @click="submitDeviceArrange">提 交</el-button>
            <el-button @click="memberArrangeOpen = false">取 消</el-button>
          </el-row>
    <!-- </form-panel> -->

      </el-col>
    </el-row>
  </div>
</template>

<script>
// export default {
//   name: "Index",
//   data() {
//     return {
//       // 版本号
//       version: "3.8.4",

//       /** 分配会员相关 */
//       arrangeMemberListLoading:false,
//       notArrangeMemberListLoading:false,
//       arrangeMemberList: [],
//       notArrangeMemberList: [],
//       arrangeMemberIds: []

//     };
//   },
export default {
    data() {
      const generateData = _ => {
        const data = [];
        const cities = ['上海', '北京', '广州', '深圳', '南京', '西安', '成都'];
        const pinyin = ['shanghai', 'beijing', 'guangzhou', 'shenzhen', 'nanjing', 'xian', 'chengdu'];
        cities.forEach((city, index) => {
          data.push({
            label: city,
            key: index,
            pinyin: pinyin[index]
          });
        });
        return data;
      };
      return {
        data: generateData(),
        value: [],
        filterMethod(query, item) {
          return item.pinyin.indexOf(query) > -1;
        }
      };
    },

  computed: {
    // orgId() {
    //   return this.$route.query.orgId || this.userOrgId
    // },
    /** 分配相关 */
    membersData() {
      return [...this.arrangeMemberList, ...this.notArrangeMemberList].map(x => {
        return {key: x.memberId, label: x.memberName}
      });
    }
  },

  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
  },
};
</script>


