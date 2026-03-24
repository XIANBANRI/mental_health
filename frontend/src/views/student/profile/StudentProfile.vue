<template>
  <el-card class="page-card">
    <template #header>
      <span>档案查询</span>
    </template>

    <el-descriptions title="学生档案" :column="2" border v-loading="loading">
      <el-descriptions-item label="学号">
        {{ profile.studentId || "暂无" }}
      </el-descriptions-item>

      <el-descriptions-item label="姓名">
        {{ profile.name || "暂无" }}
      </el-descriptions-item>

      <el-descriptions-item label="班级">
        {{ profile.className || "暂无" }}
      </el-descriptions-item>

      <el-descriptions-item label="学院">
        {{ profile.college || "暂无" }}
      </el-descriptions-item>

      <el-descriptions-item label="年级">
        {{ profile.grade || "暂无" }}
      </el-descriptions-item>

      <el-descriptions-item label="联系电话">
        {{ profile.phone || "暂无" }}
      </el-descriptions-item>

      <el-descriptions-item label="辅导员">
        {{ profile.counselorName || "暂无" }}
      </el-descriptions-item>

      <el-descriptions-item label="辅导员电话">
        {{ profile.counselorPhone || "暂无" }}
      </el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue"
import { ElMessage } from "element-plus"
import request from "@/utils/request"

const loading = ref(false)

const profile = reactive({
  studentId: "",
  name: "",
  className: "",
  college: "",
  grade: "",
  phone: "",
  counselorName: "",
  counselorPhone: ""
})

const loadProfile = async () => {
  const role = localStorage.getItem("role")
  const studentId = localStorage.getItem("studentId")

  if (role !== "student") {
    ElMessage.error("当前登录身份不是学生")
    return
  }

  if (!studentId) {
    ElMessage.error("未获取到学生学号，请重新登录")
    return
  }

  loading.value = true

  try {
    const result = await request.post("/api/student/profile", {
      studentId
    })

    if (result?.success) {
      const data = result.data || {}

      profile.studentId = data.studentId || ""
      profile.name = data.name || ""
      profile.className = data.className || ""
      profile.college = data.college || ""
      profile.grade = data.grade || ""
      profile.phone = data.phone || ""
      profile.counselorName = data.counselorName || ""
      profile.counselorPhone = data.counselorPhone || ""
    } else {
      ElMessage.error(result?.message || "档案查询失败")
    }
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || error?.message || "档案查询失败")
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadProfile()
})
</script>

<style scoped>
.page-card {
  border-radius: 12px;
}
</style>