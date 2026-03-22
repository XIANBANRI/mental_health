<template>
  <el-card class="page-card">
    <template #header>
      <span>个人信息</span>
    </template>

    <el-descriptions title="管理员信息" :column="2" border>
      <el-descriptions-item label="账号">
        {{ profile.username }}
      </el-descriptions-item>

      <el-descriptions-item label="姓名">
        {{ profile.name }}
      </el-descriptions-item>

      <el-descriptions-item label="角色">
        {{ profile.role }}
      </el-descriptions-item>

      <el-descriptions-item label="联系电话">
        {{ profile.phone }}
      </el-descriptions-item>

      <el-descriptions-item label="邮箱">
        {{ profile.email }}
      </el-descriptions-item>

      <el-descriptions-item label="状态">
        {{ profile.status }}
      </el-descriptions-item>

      <el-descriptions-item label="备注" :span="2">
        {{ profile.remark }}
      </el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<script setup>
import { computed } from "vue"

const getAdminDisplayName = (username) => {
  const storageName = localStorage.getItem("adminName")
  if (storageName && storageName.trim()) {
    return storageName
  }

  if (username === "admin") {
    return "管理员1"
  }

  if (username === "operator") {
    return "管理员2"
  }

  return "系统管理员"
}

const profile = computed(() => {
  const username =
      localStorage.getItem("adminAccount") ||
      localStorage.getItem("username") ||
      "admin"

  return {
    username,
    name: getAdminDisplayName(username),
    role: "系统管理员",
    phone: "暂无",
    email: "暂无",
    status: "启用",
    remark: "当前页面为管理员个人信息展示页"
  }
})
</script>

<style scoped>
.page-card {
  border-radius: 12px;
}
</style>