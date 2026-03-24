<template>
  <div class="teacher-profile-page">
    <el-card class="page-card">
      <template #header>
        <span>个人信息查询</span>
      </template>

      <el-descriptions :column="2" border v-loading="loading">
        <el-descriptions-item label="老师账号">
          {{ profile.teacherAccount || "暂无" }}
        </el-descriptions-item>
        <el-descriptions-item label="姓名">
          {{ profile.teacherName || "暂无" }}
        </el-descriptions-item>
        <el-descriptions-item label="手机号">
          {{ profile.phone || "暂无" }}
        </el-descriptions-item>
        <el-descriptions-item label="办公地点">
          {{ profile.officeLocation || "暂无" }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue"
import { ElMessage } from "element-plus"
import request from "@/utils/request"

const loading = ref(false)

const profile = reactive({
  teacherAccount: localStorage.getItem("teacherAccount") || localStorage.getItem("username") || "",
  teacherName: localStorage.getItem("teacherName") || "",
  phone: localStorage.getItem("teacherPhone") || "",
  officeLocation: localStorage.getItem("officeLocation") || ""
})

const loadProfile = async () => {
  const teacherAccount =
      localStorage.getItem("teacherAccount") || localStorage.getItem("username")

  if (!teacherAccount) {
    ElMessage.error("未获取到老师账号")
    return
  }

  loading.value = true
  try {
    const result = await request.post("/api/teacher/profile", {
      teacherAccount
    })

    const success = result?.code === 200 || result?.success === true

    if (success) {
      const data = result?.data || {}
      profile.teacherAccount = data.teacherAccount || teacherAccount
      profile.teacherName = data.teacherName || data.name || ""
      profile.phone = data.phone || ""
      profile.officeLocation = data.officeLocation || ""

      localStorage.setItem("teacherAccount", data.teacherAccount || teacherAccount)
      localStorage.setItem("teacherName", data.teacherName || data.name || "")
      localStorage.setItem("teacherPhone", data.phone || "")
      localStorage.setItem("officeLocation", data.officeLocation || "")
    } else {
      ElMessage.error(result?.message || "查询失败")
    }
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || error?.message || "查询失败")
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadProfile()
})
</script>

<style scoped>
.teacher-profile-page {
  min-height: 100%;
}

.page-card {
  border-radius: 10px;
}
</style>