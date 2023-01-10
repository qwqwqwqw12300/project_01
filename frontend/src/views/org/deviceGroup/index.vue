<template>
  <div class="app-container">
    orgId:{{this.orgId}}
  </div>
</template>

<script>

import { getUserProfile } from "@/api/system/user";

export default {
  name: "DeviceGroup",
  data(){
    return {
      userOrgId:undefined
    }
  },
  computed:{
    orgId(){
      return this.$route.query.orgId||this.userOrgId
    }
  },
  created(){
    this.initOrg();
  },
  methods:{
    initOrg() {
      getUserProfile().then(response => {
        this.userOrgId = response.data.orgId;
      });
    }
  }
}
</script>

<style scoped>

</style>
