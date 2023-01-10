<template>
  <div class="app-container">
    <el-row>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>机构基本信息</span>
        </div>
        <el-descriptions :column="2">
          <el-descriptions-item label="机构名称">{{ org.orgName }}</el-descriptions-item>
          <el-descriptions-item label="上级机构">{{ org.parentName }}</el-descriptions-item>
          <el-descriptions-item label="机构地址">{{ org.address }}</el-descriptions-item>
          <el-descriptions-item label="机构负责人">{{ org.leader }}</el-descriptions-item>
          <el-descriptions-item label="机构负责人电话">{{ org.leaderPhone }}</el-descriptions-item>
          <el-descriptions-item label="第一服务电话">{{ org.phone1 }}({{ org.attendantName1 }})</el-descriptions-item>
          <el-descriptions-item label="第二服务电话">{{ org.phone2 }}({{ org.attendantName2 }})</el-descriptions-item>
          <el-descriptions-item label="第三服务电话">{{ org.phone3 }}({{ org.attendantName3 }})</el-descriptions-item>
        </el-descriptions>
      </el-card>
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
      <el-card class="box-card">
        <el-table
          v-loading="loading"
          :data="deviceGroupList"
          row-key="deviceGroupId"
        >
          <el-table-column label="设备组名称" key="0" prop="name" align="center"></el-table-column>
          <el-table-column label="设备组编号" key="1" prop="no" align="center"></el-table-column>
          <el-table-column label="运营人员" key="2" prop="userName" align="center"></el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="arrangeUser(scope.row)"
                v-hasPermi="['org:deviceGroup:arrangeUser']"
              >修改运营人员
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="deviceArrange(scope.row)"
                v-hasPermi="['org:deviceGroup:deviceArrange']"
              >分配设备
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-row>


    <form-panel title="设备组分配运营人员" :visible.sync="arrangeUserOpen">

    </form-panel>
    <form-panel title="设备分配设备组" :visible.sync="deviceArrangeOpen">

    </form-panel>
  </div>
</template>

<script>
import {pageDeviceGroup, addDeviceGroup, updateDeviceGroup, arrangeUser, deviceArrange} from "@/api/org/deviceGroup"
import {getUserProfile} from "@/api/system/user";
import {getOrg} from "@/api/org/org";

export default {
  name: "DeviceGroup",
  data() {
    return {
      userOrgId: undefined,
      org: {},
      form: {},
      rules: {
        name: [
          {required: true, message: "设备组名称不能为空", trigger: "blur"}
        ]
      },
      deviceGroupList: [],
      total: 0,
      loading: false,
      arrangeUserOpen:false,
      deviceArrangeOpen:false,
    }
  },
  computed: {
    orgId() {
      return this.$route.query.orgId || this.userOrgId
    }
  },
  async created() {
    await this.initUserOrg();
    this.initOrg();
    this.getList();
    this.reset();
  },
  watch: {
    "$route.query.orgId": {
      immediate: true,
      handler: function (val) {
        if (this.$route.name == "DeviceGroup") {
          if (this.org.orgId != this.orgId) {
            this.initOrg();
            this.getList();
            this.reset();
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
    initOrg() {
      getOrg(this.orgId).then(response => {
        this.org = response.data;
      })
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
        pageSize:10000
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
    },
    /* 分配设备 */
    deviceArrange(row) {
      this.deviceArrangeOpen = true;
    }
  }
}
</script>
<style scoped>
.el-row {
  margin-bottom: 20px;
  &:last-child {
   margin-bottom: 0;
 }
}
</style>
