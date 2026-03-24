<template>
  <el-card class="page-card">
    <template #header>
      <span>趋势报告</span>
    </template>

    <el-table :data="reportList" border style="width: 100%">
      <el-table-column prop="month" label="月份" width="120" />
      <el-table-column prop="testCount" label="测评人数" width="120" />
      <el-table-column prop="warningCount" label="预警人数" width="120" />
      <el-table-column prop="appointmentCount" label="预约人数" width="120" />
      <el-table-column prop="summary" label="趋势概述" />
    </el-table>
  </el-card>
</template>

<script setup>
import { ref } from "vue"

const reportList = ref([
  {
    month: "2026-01",
    testCount: 210,
    warningCount: 12,
    appointmentCount: 18,
    summary: "整体情况稳定，少数学生存在中高风险趋势。"
  },
  {
    month: "2026-02",
    testCount: 226,
    warningCount: 15,
    appointmentCount: 21,
    summary: "预警人数小幅上升，预约咨询需求增加。"
  },
  {
    month: "2026-03",
    testCount: 248,
    warningCount: 9,
    appointmentCount: 14,
    summary: "整体风险有所下降，干预效果较明显。"
  }
])
</script>

<style scoped>
.page-card {
  border-radius: 8px;
}
</style>