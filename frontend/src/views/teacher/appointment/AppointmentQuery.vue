<template>
  <div class="appointment-query-page">
    <el-card class="page-card">
      <template #header>
        <div class="card-header">
          <span>预约管理</span>
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

      <el-table
          :data="appointmentList"
          border
          style="width: 100%"
          v-loading="loading"
          empty-text="暂无预约数据"
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

        <el-table-column label="操作" width="320" fixed="right" align="center">
          <template #default="scope">
            <div class="action-group">
              <el-button
                  v-if="scope.row.status === 'PENDING'"
                  type="success"
                  size="small"
                  @click="approveAppointment(scope.row)"
              >
                通过
              </el-button>

              <el-button
                  v-if="scope.row.status === 'PENDING'"
                  type="danger"
                  size="small"
                  @click="rejectAppointment(scope.row)"
              >
                拒绝
              </el-button>

              <el-button
                  v-if="scope.row.status === 'APPROVED'"
                  type="primary"
                  size="small"
                  @click="openRecordDialog(scope.row)"
              >
                记录
              </el-button>

              <el-button
                  v-if="scope.row.status === 'APPROVED'"
                  type="success"
                  size="small"
                  @click="completeRecord(scope.row)"
              >
                完成
              </el-button>

              <el-button
                  v-if="scope.row.status === 'COMPLETED'"
                  type="info"
                  size="small"
                  disabled
              >
                已完成
              </el-button>

              <el-button size="small" @click="loadAssessmentRecord(scope.row)">
                测试记录
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
        v-model="recordDialogVisible"
        :title="recordDialogTitle"
        width="560px"
        destroy-on-close
    >
      <el-form :model="recordForm" label-width="100px">
        <el-form-item label="学生学号">
          <el-input v-model="recordForm.studentId" disabled />
        </el-form-item>

        <el-form-item label="学生姓名">
          <el-input v-model="recordForm.studentName" disabled />
        </el-form-item>

        <el-form-item label="会诊记录">
          <el-input
              v-model="recordForm.offlineRecord"
              type="textarea"
              :rows="6"
              maxlength="500"
              show-word-limit
              placeholder="请输入会诊记录"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="recordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRecord">保存记录</el-button>
      </template>
    </el-dialog>

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
import { ElMessage, ElMessageBox } from "element-plus"
import request from "@/utils/request"

const loading = ref(false)
const appointmentList = ref([])

const recordDialogVisible = ref(false)
const currentRow = ref(null)

const assessmentDialogVisible = ref(false)
const assessmentLoading = ref(false)
const assessmentRecords = ref([])

const queryForm = reactive({
  studentId: "",
  appointmentDate: "",
  status: ""
})

const recordForm = reactive({
  id: null,
  studentId: "",
  studentName: "",
  offlineRecord: ""
})

const teacherAccount = computed(() => {
  return localStorage.getItem("teacherAccount") || localStorage.getItem("username") || ""
})

const recordDialogTitle = computed(() => {
  return recordForm.offlineRecord ? "修改会诊记录" : "填写会诊记录"
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

const loadAppointmentList = async () => {
  if (!teacherAccount.value) {
    ElMessage.error("未获取到老师账号")
    return
  }

  loading.value = true
  try {
    const result = await request.post("/api/teacher/appointment/query", {
      teacherAccount: teacherAccount.value,
      studentId: queryForm.studentId,
      appointmentDate: queryForm.appointmentDate,
      status: queryForm.status
    })

    if (result?.code === 200 || result?.success === true) {
      appointmentList.value = result?.data || []
    } else {
      appointmentList.value = []
      ElMessage.error(result?.message || "查询失败")
    }
  } catch (error) {
    appointmentList.value = []
    ElMessage.error(error?.response?.data?.message || error?.message || "查询失败")
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

const approveAppointment = async (row) => {
  try {
    await ElMessageBox.confirm("确认通过该预约吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    })

    const result = await request.post("/api/teacher/appointment/updateStatus", {
      id: row.id,
      teacherAccount: teacherAccount.value,
      status: "APPROVED",
      offlineRecord: ""
    })

    if (result?.code === 200 || result?.success === true) {
      ElMessage.success(result?.message || "预约已通过")
      loadAppointmentList()
    } else {
      ElMessage.error(result?.message || "操作失败")
    }
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error(error?.response?.data?.message || error?.message || "操作失败")
    }
  }
}

const rejectAppointment = async (row) => {
  try {
    await ElMessageBox.confirm("确认拒绝该预约吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    })

    const result = await request.post("/api/teacher/appointment/updateStatus", {
      id: row.id,
      teacherAccount: teacherAccount.value,
      status: "REJECTED",
      offlineRecord: ""
    })

    if (result?.code === 200 || result?.success === true) {
      ElMessage.success(result?.message || "预约已拒绝")
      loadAppointmentList()
    } else {
      ElMessage.error(result?.message || "操作失败")
    }
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error(error?.response?.data?.message || error?.message || "操作失败")
    }
  }
}

const openRecordDialog = (row) => {
  if (row.status === "COMPLETED" || row.recordCompleted) {
    ElMessage.warning("该记录已完成，不能再修改")
    return
  }

  currentRow.value = row
  recordForm.id = row.id
  recordForm.studentId = row.studentId || ""
  recordForm.studentName = row.studentName || ""
  recordForm.offlineRecord = row.offlineRecord || ""
  recordDialogVisible.value = true
}

const saveRecord = async () => {
  if (!recordForm.id) {
    ElMessage.error("记录ID不能为空")
    return
  }

  if (!recordForm.offlineRecord.trim()) {
    ElMessage.warning("请填写会诊记录")
    return
  }

  try {
    const result = await request.post("/api/teacher/appointment/updateStatus", {
      id: recordForm.id,
      teacherAccount: teacherAccount.value,
      status: "APPROVED",
      offlineRecord: recordForm.offlineRecord.trim()
    })

    if (result?.code === 200 || result?.success === true) {
      ElMessage.success(result?.message || "记录保存成功")
      recordDialogVisible.value = false
      loadAppointmentList()
    } else {
      ElMessage.error(result?.message || "保存失败")
    }
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || error?.message || "保存失败")
  }
}

const completeRecord = async (row) => {
  const offlineRecord = row.offlineRecord || ""

  if (!String(offlineRecord).trim()) {
    ElMessage.warning("请先点击“记录”填写会诊记录，再点击完成")
    return
  }

  try {
    await ElMessageBox.confirm("完成后会诊记录将锁定，确认继续吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    })

    const result = await request.post("/api/teacher/appointment/updateStatus", {
      id: row.id,
      teacherAccount: teacherAccount.value,
      status: "COMPLETED",
      offlineRecord: offlineRecord.trim()
    })

    if (result?.code === 200 || result?.success === true) {
      ElMessage.success(result?.message || "记录已完成并锁定")
      loadAppointmentList()
    } else {
      ElMessage.error(result?.message || "操作失败")
    }
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error(error?.response?.data?.message || error?.message || "操作失败")
    }
  }
}

const loadAssessmentRecord = async (row) => {
  assessmentDialogVisible.value = true
  assessmentLoading.value = true
  assessmentRecords.value = []

  try {
    const result = await request.post("/api/teacher/appointment/assessmentRecord", {
      teacherAccount: teacherAccount.value,
      studentId: row.studentId
    })

    if (result?.code === 200 || result?.success === true) {
      assessmentRecords.value = result?.data || []
    } else {
      ElMessage.error(result?.message || "查询测试记录失败")
    }
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || error?.message || "查询测试记录失败")
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

.action-group {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
  flex-wrap: wrap;
}
</style>