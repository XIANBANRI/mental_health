<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>心理健康管理系统</h2>
      <el-form :model="form" @keyup.enter="login">
        <el-form-item label="身份选择">
          <el-radio-group v-model="form.role" class="role-group">
            <el-radio value="student">学生</el-radio>
            <el-radio value="teacher">心理老师</el-radio>
            <el-radio value="counselor">辅导员</el-radio>
            <el-radio value="admin">管理员</el-radio>
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
          <el-button type="primary" class="login-btn" @click="login">
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
import { reactive } from "vue"
import { useRouter } from "vue-router"
import { ElMessage } from "element-plus"
import request from "@/utils/request"

const router = useRouter()

const form = reactive({
  role: "",
  username: "",
  password: ""
})

const login = async () => {
  if (!form.role || !form.username || !form.password) {
    ElMessage.error("请填写完整信息")
    return
  }

  try {
    const res = await request.post("/api/auth/login", {
      role: form.role,
      username: form.username,
      password: form.password
    })

    if (res.data.code === 200) {
      ElMessage.success(res.data.message || "登录成功")

      localStorage.setItem(
          "userInfo",
          JSON.stringify({
            role: res.data.data.role,
            username: res.data.data.username
          })
      )

      router.push(res.data.data.redirectPath || `/${form.role}`)
    } else {
      ElMessage.error(res.data.message || "登录失败")
    }
  } catch (error) {
    console.error(error)
    ElMessage.error("无法连接后端，请检查后端是否启动")
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