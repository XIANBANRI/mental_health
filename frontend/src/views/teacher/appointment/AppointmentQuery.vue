<template>
  <div class="appointment-query-page">
    <el-card class="page-card">
      <template #header>
        <span>预约查询</span>
      </template>

      <el-form :inline="true" class="search-form">
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
              placeholder="请选择日期"
              value-format="YYYY-MM-DD"
              style="width: 180px"
          />
        </el-form-item>

        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable style="width: 180px">
            <el-option label="待处理" value="PENDING" />
            <el-option label="已通过" value="APPROVED" />
            <el-option label="已拒绝" value="REJECTED" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="loadAppointmentList">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="appointmentList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="appointmentNo" label="预约单号" width="160" />
        <el-table-column prop="studentId" label="学生学号" width="130" />
        <el-table-column prop="studentName" label="学生姓名" width="120" />
        <el-table-column prop="appointmentDate" label="预约日期" width="140" />
        <el-table-column prop="startTime" label="开始时间" width="120">
          <template #default="scope">
            {{ formatTime(scope.row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="120">
          <template #default="scope">
            {{ formatTime(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="purpose" label="预约原因" min-width="180" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="teacherReply" label="老师回复" min-width="180" show-overflow-tooltip />
        <el-table-column label="操作" width="280" fixed="right" align="center">
          <template #default="scope">
            <div class="action-group">
              <el-button
                  size="small"
                  type="success"
                  @click="openAuditDialog(scope.row, 'APPROVED')"
                  v-if="scope.row.status === 'PENDING'"
              >
                通过
              </el-button>

              <el-button
                  size="small"
                  type="danger"
                  @click="openAuditDialog(scope.row, 'REJECTED')"
                  v-if="scope.row.status === 'PENDING'"
              >
                拒绝
              </el-button>

              <el-button
                  size="small"
                  type="primary"
                  @click="openAuditDialog(scope.row, 'COMPLETED')"
                  v-if="scope.row.status === 'APPROVED'"
              >
                完成
              </el-button>

              <el-button
                  size="small"
                  @click="loadAssessmentRecord(scope.row)"
              >
                测试记录
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
        v-model="auditDialogVisible"
        :title="auditDialogTitle"
        width="520px"
    >
      <el-form :model="auditForm" label-width="90px">
        <el-form-item label="学生学号">
          <el-input v-model="auditForm.studentId" disabled />
        </el-form-item>

        <el-form-item label="学生姓名">
          <el-input v-model="auditForm.studentName" disabled />
        </el-form-item>

        <el-form-item label="老师回复">
          <el-input
              v-model="auditForm.teacherReply"
              type="textarea"
              :rows="4"
              :placeholder="auditPlaceholder"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
        v-model="assessmentDialogVisible"
        title="学生心理测试记录"
        width="1000px"
    >
      <div class="student-info" v-if="assessmentRecords.length > 0">
        <span>学号：{{ assessmentRecords[0].studentId }}</span>
        <span>姓名：{{ assessmentRecords[0].studentName }}</span>
        <span>学院：{{ assessmentRecords[0].college || '暂无' }}</span>
        <span>班级：{{ assessmentRecords[0].className || '暂无' }}</span>
      </div>

      <el-empty
          v-if="assessmentRecords.length === 0"
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
        <el-table-column prop="healthStatus" label="健康状态" width="140" />
        <el-table-column prop="submittedAt" label="提交时间" min-width="180" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import request from "@/utils/request"

const loading = ref(false)
const appointmentList = ref([])

const assessmentLoading = ref(false)
const assessmentDialogVisible = ref(false)
const assessmentRecords = ref([])

const auditDialogVisible = ref(false)
const auditType = ref("")
const currentRow = ref(null)

const queryForm = reactive({
  studentId: "",
  appointmentDate: "",
  status: ""
})

const auditForm = reactive({
  id: null,
  studentId: "",
  studentName: "",
  teacherReply: ""
})

const auditDialogTitle = computed(() => {
  const map = {
    APPROVED: "通过预约",
    REJECTED: "拒绝预约",
    COMPLETED: "完成预约"
  }
  return map[auditType.value] || "处理预约"
})

const auditPlaceholder = computed(() => {
  const map = {
    APPROVED: "请输入通过说明，可不填",
    REJECTED: "请输入拒绝原因，建议填写",
    COMPLETED: "请输入完成说明，可不填"
  }
  return map[auditType.value] || "请输入老师回复"
})

const teacherAccount = () =>
    localStorage.getItem("teacherAccount") || localStorage.getItem("username")

const formatTime = (time) => {
  if (!time) return ""
  return String(time).slice(0, 5)
}

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

const loadAppointmentList = async () => {
  if (!teacherAccount()) {
    ElMessage.error("未获取到老师账号")
    return
  }

  loading.value = true
  try {
    const res = await request.post("/api/teacher/appointment/query", {
      teacherAccount: teacherAccount(),
      studentId: queryForm.studentId,
      appointmentDate: queryForm.appointmentDate,
      status: queryForm.status
    })

    const result = res.data || {}
    const success = result.code === 200 || result.success === true

    if (success) {
      appointmentList.value = result.data || []
    } else {
      ElMessage.error(result.message || "查询失败")
    }
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || "查询失败")
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  queryForm.studentId = ""
  queryForm.appointmentDate = ""
  queryForm.status = ""
  loadAppointmentList()
}

const openAuditDialog = (row, type) => {
  currentRow.value = row
  auditType.value = type
  auditForm.id = row.id
  auditForm.studentId = row.studentId
  auditForm.studentName = row.studentName
  auditForm.teacherReply = row.teacherReply || ""
  auditDialogVisible.value = true
}

const submitAudit = async () => {
  if (!currentRow.value) return

  try {
    await ElMessageBox.confirm(`确认要${auditDialogTitle.value}吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    })

    const res = await request.post("/api/teacher/appointment/updateStatus", {
      id: auditForm.id,
      teacherAccount: teacherAccount(),
      status: auditType.value,
      teacherReply: auditForm.teacherReply
    })

    const result = res.data || {}
    const success = result.code === 200 || result.success === true

    if (success) {
      ElMessage.success(result.message || "操作成功")
      auditDialogVisible.value = false
      loadAppointmentList()
    } else {
      ElMessage.error(result.message || "操作失败")
    }
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error(error?.response?.data?.message || "操作失败")
    }
  }
}

const loadAssessmentRecord = async (row) => {
  assessmentDialogVisible.value = true
  assessmentLoading.value = true
  assessmentRecords.value = []

  try {
    const res = await request.post("/api/teacher/appointment/assessmentRecord", {
      teacherAccount: teacherAccount(),
      studentId: row.studentId
    })

    const result = res.data || {}
    const success = result.code === 200 || result.success === true

    if (success) {
      assessmentRecords.value = result.data || []
    } else {
      ElMessage.error(result.message || "查询心理测试记录失败")
    }
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || "查询心理测试记录失败")
  } finally {
    assessmentLoading.value = false
  }
}

onMounted(() => {
  loadAppointmentList()
})
</script>

<style scoped>
.appointment-query-page {
  min-height: 100%;
}

.page-card {
  border-radius: 10px;
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

.action-group {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
  flex-wrap: wrap;
}
</style>