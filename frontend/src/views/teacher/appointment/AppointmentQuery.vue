<template>
  <div class="appointment-query-page">
    <el-card class="page-card">
      <template #header>
        <span>预约查询</span>
      </template>

      <el-form :inline="true" class="search-form">
        <el-form-item label="学生学号">
          <el-input v-model="queryForm.studentId" placeholder="请输入学生学号" clearable style="width: 180px" />
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
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="filteredList" border style="width: 100%">
        <el-table-column prop="studentId" label="学生学号" width="130" />
        <el-table-column prop="studentName" label="学生姓名" width="120" />
        <el-table-column prop="appointmentDate" label="预约日期" width="140" />
        <el-table-column prop="startTime" label="开始时间" width="120" />
        <el-table-column prop="endTime" label="结束时间" width="120" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="180" />
        <el-table-column label="操作" width="240">
          <template #default="scope">
            <el-button
                size="small"
                type="success"
                @click="handleStatus(scope.row, 'APPROVED')"
                v-if="scope.row.status === 'PENDING'"
            >
              通过
            </el-button>
            <el-button
                size="small"
                type="danger"
                @click="handleStatus(scope.row, 'REJECTED')"
                v-if="scope.row.status === 'PENDING'"
            >
              拒绝
            </el-button>
            <el-button
                size="small"
                type="primary"
                @click="handleStatus(scope.row, 'COMPLETED')"
                v-if="scope.row.status === 'APPROVED'"
            >
              完成
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"

const queryForm = reactive({
  studentId: "",
  appointmentDate: "",
  status: ""
})

const appointmentList = ref([
  {
    id: 1,
    studentId: "20230001",
    studentName: "李明",
    appointmentDate: "2026-03-20",
    startTime: "09:00",
    endTime: "10:00",
    status: "PENDING",
    remark: "学习压力咨询"
  },
  {
    id: 2,
    studentId: "20230002",
    studentName: "王芳",
    appointmentDate: "2026-03-21",
    startTime: "14:00",
    endTime: "15:00",
    status: "APPROVED",
    remark: "情绪疏导"
  }
])

const filteredList = computed(() => {
  return appointmentList.value.filter(item => {
    const matchStudentId = !queryForm.studentId || item.studentId.includes(queryForm.studentId)
    const matchDate = !queryForm.appointmentDate || item.appointmentDate === queryForm.appointmentDate
    const matchStatus = !queryForm.status || item.status === queryForm.status
    return matchStudentId && matchDate && matchStatus
  })
})

const getStatusText = (status) => {
  const map = {
    PENDING: "待处理",
    APPROVED: "已通过",
    REJECTED: "已拒绝",
    COMPLETED: "已完成",
    CANCELLED: "已取消"
  }
  return map[status] || status
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

const handleSearch = () => {
  ElMessage.success("查询成功")
}

const resetQuery = () => {
  queryForm.studentId = ""
  queryForm.appointmentDate = ""
  queryForm.status = ""
}

const handleStatus = async (row, status) => {
  const textMap = {
    APPROVED: "通过",
    REJECTED: "拒绝",
    COMPLETED: "完成"
  }

  try {
    await ElMessageBox.confirm(`确认要${textMap[status]}该预约吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    })

    row.status = status
    ElMessage.success("操作成功")
  } catch (e) {
    // 取消
  }
}
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
</style>