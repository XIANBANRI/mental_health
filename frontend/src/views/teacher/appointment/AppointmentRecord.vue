<template>
  <div class="appointment-record-page">
    <el-card class="page-card">
      <template #header>
        <span>预约记录</span>
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
        <el-table-column prop="createdAt" label="创建时间" width="180" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from "vue"
import { ElMessage } from "element-plus"

const queryForm = reactive({
  studentId: "",
  appointmentDate: "",
  status: ""
})

const recordList = ref([
  {
    id: 1,
    studentId: "20230002",
    studentName: "王芳",
    appointmentDate: "2026-03-21",
    startTime: "14:00",
    endTime: "15:00",
    status: "COMPLETED",
    remark: "情绪疏导",
    createdAt: "2026-03-18 10:30:00"
  },
  {
    id: 2,
    studentId: "20230003",
    studentName: "赵强",
    appointmentDate: "2026-03-22",
    startTime: "10:00",
    endTime: "11:00",
    status: "REJECTED",
    remark: "时间冲突",
    createdAt: "2026-03-18 14:20:00"
  }
])

const filteredList = computed(() => {
  return recordList.value.filter(item => {
    const matchStudentId = !queryForm.studentId || item.studentId.includes(queryForm.studentId)
    const matchDate = !queryForm.appointmentDate || item.appointmentDate === queryForm.appointmentDate
    const matchStatus = !queryForm.status || item.status === queryForm.status
    return matchStudentId && matchDate && matchStatus
  })
})

const getStatusText = (status) => {
  const map = {
    APPROVED: "已通过",
    REJECTED: "已拒绝",
    COMPLETED: "已完成",
    CANCELLED: "已取消"
  }
  return map[status] || status
}

const getStatusTag = (status) => {
  const map = {
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
</script>

<style scoped>
.appointment-record-page {
  min-height: 100%;
}

.page-card {
  border-radius: 10px;
}

.search-form {
  margin-bottom: 18px;
}
</style>