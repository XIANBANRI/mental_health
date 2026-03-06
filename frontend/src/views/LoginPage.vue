<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>心理健康管理系统</h2>
      <el-form :model="form">
        <!-- 身份选择 -->
        <el-form-item label="身份选择">
          <el-radio-group v-model="form.role" class="role-group">
            <el-radio label="student">学生</el-radio>
            <el-radio label="teacher">心理老师</el-radio>
            <el-radio label="counselor">辅导员</el-radio>
            <el-radio label="admin">管理员</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="账号">
          <el-input v-model="form.username" />
        </el-form-item>

        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" />
        </el-form-item>

        <!-- 按钮 -->
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

const router = useRouter()

const form = reactive({
  role: "",
  username: "",
  password: ""
})

const login = () => {
  if (!form.role || !form.username || !form.password) {
    ElMessage.error("请填写完整信息")
    return
  }

  ElMessage.success("登录成功")
  router.push("/" + form.role)
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

/* 确保每个单选框项也是行内元素 */
.role-group .el-radio {
  display: inline-flex !important;
  margin: 0 !important; /* 清除默认外边距 */
}

/* 按钮布局 */
.btn-group {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.login-btn {
  width: 70%;
}
</style>