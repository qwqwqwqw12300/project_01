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
            <treeselect style="width: 240px" v-model="queryParams.orgId" :options="orgOptions" :show-count="true" placeholder="请选择归属机构" />
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
              </el-button><el-button
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
                size="mini"
                type="text"
                icon="el-icon-circle-check"
                @click="handleSettings(scope.row)"
                v-hasPermi="['device:settings']"
              >设备设置
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
  </div>
</template>

<script>
import {orgTreeSelect} from "@/api/system/user";
import {pageDevice} from "@/api/device/device";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Device",
  dicts: ['device_type'],
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      showSearch: true,
      distributeFlagDict: [{label: '已分配', value: 1}, {label: '未分配', value: 0}],
      // 机构树选项
      orgOptions: undefined,
      total: 0,
      deviceList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        no: undefined,
        name: undefined,
        type: undefined,
        orgId: undefined,
        distributeFlag: undefined,

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
      ]
    }
  },
  created() {
    this.getList();
    this.getDeptTree();
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
    getDeptTree() {
      orgTreeSelect().then(response => {
        this.orgOptions = response.data;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleAssociate(row) {

    },
    handleGroupArrange(row) {

    },
    handleSettings(row) {

    }

  }
}
</script>
