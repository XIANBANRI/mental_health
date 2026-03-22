<template>
  <div class="appointment-record-page">
    <el-card class="page-card">
      <template #header>
        <div class="card-header">
          <span>预约记录</span>
        </div>
      </template>

      <el-form :model="queryForm" inline class="search-form">
        <el-form-item label="学生学号">
          <el-input
              v-model="queryForm.studentId"
              placeholder="请输入学生学号"
              clearable
              style="width: 180px"
          />
        </el-form-item>

        <el-form-item label="预约日期">
          <el-date-picker
              v-model="queryForm.appointmentDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择预约日期"
              clearable
              style="width: 180px"
          />
        </el-form-item>

        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="全部" clearable style="width: 160px">
            <el-option label="已通过" value="APPROVED" />
            <el-option label="已拒绝" value="REJECTED" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="loadRecordList">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table
          :data="recordList"
          border
          style="width: 100%"
          v-loading="loading"
          empty-text="暂无预约记录"
      >
        <el-table-column prop="appointmentNo" label="预约编号" min-width="160" />
        <el-table-column prop="studentId" label="学生学号" width="120" />
        <el-table-column prop="studentName" label="学生姓名" width="100" />
        <el-table-column prop="appointmentDate" label="预约日期" width="120" />
        <el-table-column prop="startTime" label="开始时间" width="90" />
        <el-table-column prop="endTime" label="结束时间" width="90" />
        <el-table-column prop="purpose" label="预约原因" min-width="140" show-overflow-tooltip />
        <el-table-column prop="remark" label="学生备注" min-width="140" show-overflow-tooltip />

        <el-table-column label="会诊记录" min-width="220" show-overflow-tooltip>
          <template #default="scope">
            {{ scope.row.offlineRecord || "暂无" }}
          </template>
        </el-table-column>

        <el-table-column label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="记录状态" width="110" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.recordCompleted ? 'success' : 'warning'">
              {{ scope.row.recordCompleted ? "已完成" : "未完成" }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="approvedAt" label="通过时间" min-width="170" />
        <el-table-column prop="completedAt" label="完成时间" min-width="170" />

        <el-table-column label="操作" width="120" fixed="right" align="center">
          <template #default="scope">
            <el-button size="small" @click="loadAssessmentRecord(scope.row)">
              测试记录
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
        v-model="assessmentDialogVisible"
        title="学生心理测试记录"
        width="1100px"
        destroy-on-close
    >
      <div class="student-info" v-if="assessmentRecords.length > 0">
        <span>学号：{{ assessmentRecords[0].studentId }}</span>
        <span>姓名：{{ assessmentRecords[0].studentName }}</span>
        <span>学院：{{ assessmentRecords[0].college || "暂无" }}</span>
        <span>班级：{{ assessmentRecords[0].className || "暂无" }}</span>
      </div>

      <el-empty
          v-if="!assessmentLoading && assessmentRecords.length === 0"
          description="暂无心理测试记录"
      />

      <el-table
          v-else
          :data="assessmentRecords"
          border
          style="width: 100%"
          v-loading="assessmentLoading"
      >
        <el-table-column prop="semester" label="学期" width="110" />
        <el-table-column prop="k10Score" label="K10分数" width="100" />
        <el-table-column prop="k10Status" label="K10状态" width="120" />
        <el-table-column prop="who5Score" label="WHO-5分数" width="110" />
        <el-table-column prop="who5Status" label="WHO-5状态" width="130" />
        <el-table-column prop="phq9Score" label="PHQ-9分数" width="110" />
        <el-table-column prop="phq9Status" label="PHQ-9状态" width="130" />
        <el-table-column prop="gad7Score" label="GAD-7分数" width="110" />
        <el-table-column prop="gad7Status" label="GAD-7状态" width="130" />
        <el-table-column prop="healthTotalScore" label="总分" width="90" />
        <el-table-column prop="healthStatus" label="健康状态" width="120" />
        <el-table-column prop="healthSummary" label="测评结论" min-width="180" show-overflow-tooltip />
        <el-table-column prop="submittedAt" label="提交时间" min-width="180" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from "vue"
import { ElMessage } from "element-plus"
import request from "@/utils/request"

const loading = ref(false)
const recordList = ref([])

const assessmentDialogVisible = ref(false)
const assessmentLoading = ref(false)
const assessmentRecords = ref([])

const queryForm = reactive({
  studentId: "",
  appointmentDate: "",
  status: ""
})

const teacherAccount = computed(() => {
  return localStorage.getItem("teacherAccount") || localStorage.getItem("username") || ""
})

const getStatusText = (status) => {
  const map = {
    PENDING: "待处理",
    APPROVED: "已通过",
    REJECTED: "已拒绝",
    COMPLETED: "已完成",
    CANCELLED: "已取消"
  }
  return map[status] || status || "未知"
}

const getStatusTag = (status) => {
  const map = {
    PENDING: "warning",
    APPROVED: "success",
    REJECTED: "danger",
    COMPLETED: "primary",
    CANCELLED: "info"
  }
  return map[status] || "info"
}

const loadRecordList = async () => {
  if (!teacherAccount.value) {
    ElMessage.error("未获取到老师账号")
    return
  }

  loading.value = true
  try {
    const res = await request.post("/api/teacher/appointment/record", {
      teacherAccount: teacherAccount.value,
      studentId: queryForm.studentId,
      appointmentDate: queryForm.appointmentDate,
      status: queryForm.status
    })

    const result = res.data || {}
    if (result.code === 200 || result.success === true) {
      recordList.value = result.data || []
    } else {
      recordList.value = []
      ElMessage.error(result.message || "查询失败")
    }
  } catch (error) {
    recordList.value = []
    ElMessage.error(error?.response?.data?.message || error?.message || "查询失败")
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  queryForm.studentId = ""
  queryForm.appointmentDate = ""
  queryForm.status = ""
  loadRecordList()
}

const loadAssessmentRecord = async (row) => {
  assessmentDialogVisible.value = true
  assessmentLoading.value = true
  assessmentRecords.value = []

  try {
    const res = await request.post("/api/teacher/appointment/assessmentRecord", {
      teacherAccount: teacherAccount.value,
      studentId: row.studentId
    })

    const result = res.data || {}
    if (result.code === 200 || result.success === true) {
      assessmentRecords.value = result.data || []
    } else {
      ElMessage.error(result.message || "查询测试记录失败")
    }
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || error?.message || "查询测试记录失败")
  } finally {
    assessmentLoading.value = false
  }
}

onMounted(() => {
  loadRecordList()
})
</script>

<style scoped>
.appointment-record-page {
  min-height: 100%;
}

.page-card {
  border-radius: 10px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.search-form {
  margin-bottom: 18px;
}

.student-info {
  margin-bottom: 16px;
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
  color: #606266;
  font-size: 14px;
}
</style>