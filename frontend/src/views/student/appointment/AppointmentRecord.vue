<template>
  <el-card class="page-card">
    <template #header>
      <span>预约记录</span>
    </template>

    <el-table :data="appointmentList" style="width: 100%">
      <el-table-column prop="teacher" label="心理老师" />
      <el-table-column prop="date" label="预约日期" />
      <el-table-column prop="time" label="预约时间" />
      <el-table-column prop="status" label="状态" />
    </el-table>
  </el-card>
</template>

<script setup>
import { reactive } from "vue"

const appointmentList = reactive([
  {
    teacher: "李老师",
    date: "2026-03-15",
    time: "14:30",
    status: "待处理"
  },
  {
    teacher: "王老师",
    date: "2026-03-01",
    time: "10:00",
    status: "已完成"
  }
])
</script>

<style scoped>
.page-card {
  border-radius: 12px;
}
</style>