<template>
  <el-card class="page-card">
    <template #header>
      <span>个人信息</span>
    </template>

    <el-descriptions title="辅导员档案" :column="2" border v-loading="loading">
      <el-descriptions-item label="账号">
        {{ profile.counselorId || "暂无" }}
      </el-descriptions-item>

      <el-descriptions-item label="姓名">
        {{ profile.name || "暂无" }}
      </el-descriptions-item>

      <el-descriptions-item label="学院">
        {{ profile.college || "暂无" }}
      </el-descriptions-item>

      <el-descriptions-item label="负责年级">
        {{ profile.grade || "暂无" }}
      </el-descriptions-item>

      <el-descriptions-item label="联系电话" :span="2">
        {{ profile.phone || "暂无" }}
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
  counselorId: "",
  name: "",
  college: "",
  grade: "",
  phone: ""
})

const loadProfile = async () => {
  const role = localStorage.getItem("role")
  const account =
      localStorage.getItem("counselorAccount") ||
      localStorage.getItem("username") ||
      localStorage.getItem("counselorId")

  if (role !== "counselor") {
    ElMessage.error("当前登录身份不是辅导员")
    return
  }

  if (!account) {
    ElMessage.error("未获取到辅导员账号，请重新登录")
    return
  }

  loading.value = true

  try {
    const result = await request.post("/counselor/profile/get", {
      account
    })

    const success = result?.code === 200 || result?.success === true

    if (success) {
      const data = result?.data || {}

      profile.counselorId = data.counselorId || account
      profile.name = data.name || ""
      profile.college = data.college || ""
      profile.grade = data.grade || ""
      profile.phone = data.phone || ""

      localStorage.setItem("counselorId", data.counselorId || account)
      localStorage.setItem("counselorAccount", data.counselorId || account)
      localStorage.setItem("counselorName", data.name || "")
      localStorage.setItem("college", data.college || "")
      localStorage.setItem("grade", data.grade || "")
      localStorage.setItem("phone", data.phone || "")
    } else {
      ElMessage.error(result?.message || "个人信息加载失败")
    }
  } catch (error) {
    ElMessage.error(
        error?.response?.data?.message ||
        error?.response?.data?.msg ||
        error?.message ||
        "个人信息加载失败"
    )
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