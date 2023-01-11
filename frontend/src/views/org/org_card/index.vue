<template>
  <div class="app-container">
  <el-row>
    <!-- 卡片表单展示 -->
    <el-col :span="24" class="card-box">
          <el-card>
            <div slot="header">
              <span>机构基本信息</span>
            </div>
            <div class="el-table el-table--enable-row-hover el-table--medium">
              <table cellspacing="0" style="width: 100%;">
                <tbody>
                  <tr>
                    <td class="el-table__cell is-leaf"><div class="cell">机构名称：</div></td>
                    <td class="el-table__cell is-leaf"><div class="cell" >马尾养老院</div></td>
                    <td class="el-table__cell is-leaf"><div class="cell">机构编号：</div></td>
                    <td class="el-table__cell is-leaf"><div class="cell" >100000123001</div></td>
                  </tr>
                  <tr>
                    <td class="el-table__cell is-leaf"><div class="cell">上级机构：</div></td>
                    <td class="el-table__cell is-leaf"><div class="cell" >爱康公司</div></td>
                    <td class="el-table__cell is-leaf"><div class="cell">机构地址：</div></td>
                    <td class="el-table__cell is-leaf"><div class="cell" >福州市马尾江滨路1号</div></td>
                  </tr>
          <tr>
                    <td class="el-table__cell is-leaf"><div class="cell">机构负责人：</div></td>
                    <td class="el-table__cell is-leaf"><div class="cell" >张三</div></td>
                    <td class="el-table__cell is-leaf"><div class="cell">机构负责人电话：</div></td>
                    <td class="el-table__cell is-leaf"><div class="cell" >13300001221</div></td>
                  </tr>
          <tr>
                    <td class="el-table__cell is-leaf"><div class="cell">第一服务电话：</div></td>
                    <td class="el-table__cell is-leaf"><div class="cell" >13300001221</div></td>
                    <td class="el-table__cell is-leaf"><div class="cell">第二服务电话：</div></td>
                    <td class="el-table__cell is-leaf"><div class="cell" >13300001221</div></td>
                  </tr>
          <tr>
                    <td class="el-table__cell is-leaf"><div class="cell">第三服务电话：</div></td>
                    <td class="el-table__cell is-leaf"><div class="cell" >13300001221</div></td>

                  </tr>
                </tbody>
              </table>
            </div>
          </el-card>
        </el-col>
    </el-row>
  </div>
</template>

<script>
import { getServer } from "@/api/monitor/server";

export default {
  name: "Server",
  data() {
    return {
      // 服务器信息
      server: []
    };
  },
  created() {
    this.getList();
    this.openLoading();
  },
  methods: {
    /** 查询服务器信息 */
    getList() {
      getServer().then(response => {
        this.server = response.data;
        this.$modal.closeLoading();
      });
    },
    // 打开加载层
    openLoading() {
      this.$modal.loading("正在加载服务监控数据，请稍候！");
    }
  }
};
</script>