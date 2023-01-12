<template>
  <div class="app-container">

  </div>
</template>

<script>
import {pageDeviceGroup, addDeviceGroup, updateDeviceGroup, arrangeUser, deviceArrange} from "@/api/org/deviceGroup"
import {getOrg} from "@/api/org/org";
import OrgInfoCard from "@/views/org/components/OrgInfoCard";

export default {
  name: "DeviceGroupArrangeUser",
  components:{OrgInfoCard},
  data() {
    return {
      orgId: undefined,
      org: undefined
    }
  },
  async created() {
    this.orgId = this.$route.query.orgId;
    this.initOrg();
  },
  methods: {
    initOrg() {
      if(this.orgId != undefined)
        getOrg(this.orgId).then(response => {
          this.org = response.data;
        })
    },
  }
}
</script>

<style scoped>

</style>
