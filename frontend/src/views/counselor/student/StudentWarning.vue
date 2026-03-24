<template>
  <el-card class="page-card">
    <template #header>
      <span>学生预警</span>
    </template>

    <el-table :data="warningList" border style="width: 100%">
      <el-table-column prop="studentId" label="学号" width="120" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="className" label="班级" width="140" />
      <el-table-column prop="warningType" label="预警类型" width="140" />
      <el-table-column prop="warningLevel" label="预警等级" width="100">
        <template #default="scope">
          <el-tag
              :type="
              scope.row.warningLevel === '高'
                ? 'danger'
                : scope.row.warningLevel === '中'
                ? 'warning'
                : 'info'
            "
          >
            {{ scope.row.warningLevel }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reason" label="预警原因" />
      <el-table-column prop="warningTime" label="预警时间" width="160" />
      <el-table-column prop="status" label="处理状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === '已处理' ? 'success' : 'danger'">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref } from "vue"

const warningList = ref([
  {
    studentId: "20230003",
    name: "王五",
    className: "软工2302",
    warningType: "测评异常",
    warningLevel: "高",
    reason: "连续两次心理测评结果偏高风险",
    warningTime: "2026-03-20 10:30:00",
    status: "未处理"
  },
  {
    studentId: "20230002",
    name: "李四",
    className: "计科2301",
    warningType: "预约异常",
    warningLevel: "中",
    reason: "短期内多次预约心理咨询",
    warningTime: "2026-03-18 15:20:00",
    status: "已处理"
  }
])
</script>

<style scoped>
.page-card {
  border-radius: 8px;
}
</style>