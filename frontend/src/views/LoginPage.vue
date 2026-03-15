<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>心理健康管理系统</h2>

      <el-form :model="form" @keyup.enter="login">
        <el-form-item label="身份选择">
          <el-radio-group v-model="form.role" class="role-group">
            <el-radio label="student">学生</el-radio>
            <el-radio label="teacher">心理老师</el-radio>
            <el-radio label="counselor">辅导员</el-radio>
            <el-radio label="admin">管理员</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="账号">
          <el-input v-model="form.username" placeholder="请输入账号" />
        </el-form-item>

        <el-form-item label="密码">
          <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              show-password
          />
        </el-form-item>

        <div class="btn-group">
          <el-button
              type="primary"
              class="login-btn"
              :loading="loading"
              @click="login"
          >
            登录
          </el-button>

          <el-button type="text" @click="goForget">
            忘记密码
          </el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue"
import { useRouter } from "vue-router"
import { ElMessage } from "element-plus"
import request from "@/utils/request"

const router = useRouter()
const loading = ref(false)

const form = reactive({
  role: "",
  username: "",
  password: ""
})

const clearLoginCache = () => {
  localStorage.removeItem("token")
  localStorage.removeItem("role")
  localStorage.removeItem("username")
  localStorage.removeItem("userInfo")

  localStorage.removeItem("studentId")
  localStorage.removeItem("studentName")
  localStorage.removeItem("className")
  localStorage.removeItem("college")
  localStorage.removeItem("phone")
}

const login = async () => {
  if (!form.role || !form.username || !form.password) {
    ElMessage.error("请填写完整信息")
    return
  }

  loading.value = true

  try {
    const res = await request.post("/api/auth/login", {
      role: form.role,
      username: form.username,
      password: form.password
    })

    const result = res.data || {}
    const success = result.code === 200 || result.success === true
    const data = result.data || {}

    if (success) {
      clearLoginCache()

      const role = data.role || form.role
      const username = data.username || form.username

      localStorage.setItem("token", "mock-token")
      localStorage.setItem("role", role)
      localStorage.setItem("username", username)

      localStorage.setItem(
          "userInfo",
          JSON.stringify({
            role,
            username
          })
      )

      // 只有学生登录时，才保存 studentId
      if (role === "student") {
        localStorage.setItem("studentId", username)

        if (data.name) {
          localStorage.setItem("studentName", data.name)
        }
        if (data.className) {
          localStorage.setItem("className", data.className)
        }
        if (data.college) {
          localStorage.setItem("college", data.college)
        }
        if (data.phone) {
          localStorage.setItem("phone", data.phone)
        }
      }

      ElMessage.success(result.message || "登录成功")
      router.push(data.redirectPath || `/${role}`)
    } else {
      ElMessage.error(result.message || "登录失败")
    }
  } catch (error) {
    console.error(error)
    ElMessage.error(
        error?.response?.data?.message ||
        error?.response?.data?.msg ||
        "无法连接后端，请检查后端是否启动"
    )
  } finally {
    loading.value = false
  }
}

const goForget = () => {
  router.push("/forget")
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(120deg, #89f7fe, #66a6ff);
}

.login-card {
  width: 450px;
}

h2 {
  text-align: center;
  margin-bottom: 20px;
}

.role-group {
  display: flex !important;
  flex-wrap: nowrap !important;
  gap: 15px !important;
  align-items: center;
  width: 100%;
}

.role-group .el-radio {
  display: inline-flex !important;
  margin: 0 !important;
}

.btn-group {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.login-btn {
  width: 70%;
}
</style>