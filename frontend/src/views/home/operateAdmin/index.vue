<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>运营首页</h2>

      </el-col>
    </el-row>
<!-- 测试 -->
    <el-row>
      <member-info-card :value="100"></member-info-card>
    </el-row>

    <el-row :gutter="20">
      <device-info-card  :value="100"></device-info-card>
    </el-row>

  </div>
</template>

<script>

// import {getMemberId} from "@/api/member/member";
import memberInfoCard from "@/views/member/components/memberInfoCard";
import DeviceInfoCard from "@/views/device/components/DeviceInfoCard";


export default {
  name: "Index",
  components:{memberInfoCard,DeviceInfoCard},

  data() {
    return {
      // 版本号
      version: "3.8.4",
    };
  },
  // computed: {
  //   memberId() {
  //     // return this.$route.query.orgId || this.userOrgId
  //   }
  // },
  // async created() {
  //   await this.initUserOrg();
  //   this.initOrg();
  //   this.getList();
  //   this.reset();
  // },
  // watch: {
  //   "$route.query.orgId": {
  //     immediate: true,
  //     handler: function (val) {
  //       if (this.$route.name == "DeviceGroup") {
  //         if (this.org.orgId != this.orgId) {
  //           this.initOrg();
  //           this.getList();
  //           this.reset();
  //         }
  //       }
  //     }
  //   }
  // },
  methods: {
    // async initUserOrg() {
    //   const response = await getUserProfile();
    //   this.userOrgId = response.data.orgId;
    // },
    initMember() {
      if(this.membergId != undefined)
        getOrg(this.membergId).then(response => {
          this.member = response.data;
        })
    },
    // reset() {
    //   this.form = {
    //     orgId: this.orgId,
    //     name: undefined
    //   }
    // },
    // goTarget(href) {
    //   window.open(href, "_blank");
    // },
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

