<template>
  <el-card class="page-card">
    <template #header>
      <div class="card-header">
        <span>测试记录</span>
        <el-button type="primary" plain size="small" @click="loadRecords">刷新</el-button>
      </div>
    </template>

    <el-table :data="recordList" style="width: 100%" v-loading="loading" border>
      <el-table-column prop="semester" label="学期" width="110" />

      <el-table-column label="K10" min-width="180">
        <template #default="scope">
          <div class="scale-cell">
            <el-tag :type="getStatusTagType(scope.row.k10Status)" size="small">
              {{ scope.row.k10Status || "未完成" }}
            </el-tag>
            <div class="scale-text">得分：{{ displayScore(scope.row.k10Score) }}</div>
            <div class="scale-text">等级：{{ scope.row.k10Level || "-" }}</div>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="WHO5" min-width="180">
        <template #default="scope">
          <div class="scale-cell">
            <el-tag :type="getStatusTagType(scope.row.who5Status)" size="small">
              {{ scope.row.who5Status || "未完成" }}
            </el-tag>
            <div class="scale-text">得分：{{ displayScore(scope.row.who5Score) }}</div>
            <div class="scale-text">等级：{{ scope.row.who5Level || "-" }}</div>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="PHQ9" min-width="180">
        <template #default="scope">
          <div class="scale-cell">
            <el-tag :type="getStatusTagType(scope.row.phq9Status)" size="small">
              {{ scope.row.phq9Status || "未完成" }}
            </el-tag>
            <div class="scale-text">得分：{{ displayScore(scope.row.phq9Score) }}</div>
            <div class="scale-text">等级：{{ scope.row.phq9Level || "-" }}</div>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="GAD7" min-width="180">
        <template #default="scope">
          <div class="scale-cell">
            <el-tag :type="getStatusTagType(scope.row.gad7Status)" size="small">
              {{ scope.row.gad7Status || "未完成" }}
            </el-tag>
            <div class="scale-text">得分：{{ displayScore(scope.row.gad7Score) }}</div>
            <div class="scale-text">等级：{{ scope.row.gad7Level || "-" }}</div>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="综合结果" min-width="240">
        <template #default="scope">
          <div class="scale-cell">
            <el-tag :type="getHealthTagType(scope.row.healthStatus)" size="small">
              {{ scope.row.healthStatus || "未完成" }}
            </el-tag>
            <div class="scale-text">
              综合总分：{{ displayScore(scope.row.healthTotalScore) }}
            </div>
            <div class="scale-summary">
              {{ scope.row.healthSummary || "四项量表未全部完成，暂不生成综合总分" }}
            </div>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="submittedAt" label="提交时间" width="180" />
    </el-table>

    <el-empty v-if="!loading && recordList.length === 0" description="暂无测试记录" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { ElMessage } from "element-plus"
import request from "@/utils/request"

const loading = ref(false)
const recordList = ref([])

const displayScore = (score) => {
  return score === null || score === undefined ? "未完成" : score
}

const getStatusTagType = (status) => {
  if (status === "已完成") return "success"
  return "info"
}

const getHealthTagType = (status) => {
  if (status === "健康") return "success"
  if (status === "预警") return "warning"
  if (status === "风险较高") return "danger"
  if (status === "高风险") return "danger"
  return "info"
}

const loadRecords = async () => {
  const studentId = localStorage.getItem("studentId")

  if (!studentId) {
    ElMessage.error("未获取到学生学号，请重新登录")
    return
  }

  loading.value = true
  try {
    const res = await request.get(`/api/student/assessment/records/${studentId}`)
    const result = res.data || {}

    if (result.success) {
      recordList.value = result.data || []
    } else {
      ElMessage.error(result.message || "测试记录加载失败")
    }
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || "测试记录加载失败")
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadRecords()
})
</script>

<style scoped>
.page-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.scale-cell {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.scale-text {
  color: #606266;
  font-size: 13px;
  line-height: 1.5;
}

.scale-summary {
  color: #303133;
  font-size: 13px;
  line-height: 1.6;
}
</style>
