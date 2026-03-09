<template>
  <div class="forget-container">
    <el-card class="forget-card">
      <h2>找回密码</h2>

      <el-form :model="form" class="reset-form">
        <el-form-item label="身份选择">
          <el-radio-group v-model="form.role" class="role-group">
            <el-radio label="student">学生</el-radio>
            <el-radio label="teacher">心理老师</el-radio>
            <el-radio label="counselor">辅导员</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="账号">
          <el-input v-model="form.username" />
        </el-form-item>

        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>

        <el-form-item label="新密码">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>

        <!-- 按钮专属的form-item，清除默认内边距 -->
        <el-form-item class="btn-form-item">
          <div class="btn-group">
            <el-button type="primary" class="full-btn" @click="reset">
              重置密码
            </el-button>
            <el-button class="full-btn" @click="backLogin">
              返回登录
            </el-button>
          </div>
        </el-form-item>
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
  phone: "",
  password: ""
})

const reset = () => {
  if (!form.role || !form.username || !form.phone || !form.password) {
    ElMessage.error("请填写完整信息")
    return
  }
  ElMessage.success("页面校验通过，后端接口暂未接入")
}

const backLogin = () => {
  router.push("/")
}
</script>

<style scoped>
.forget-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(120deg, #89f7fe, #66a6ff);
}

.forget-card {
  width: 450px;
  padding: 20px !important; /* 统一卡片内边距 */
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

/* 表单整体样式，统一输入框和按钮的基准 */
.reset-form {
  width: 100%;
}

/* 清除按钮所在form-item的默认内边距 */
.btn-form-item {
  margin: 0 !important;
  padding: 0 !important;
}

/* 按钮容器：垂直排列，间距10px，宽度100% */
.btn-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
  margin-top: 10px; /* 与上一个表单项保持间距 */
}

/* 强制按钮盒模型，清除默认margin，宽度100% */
.full-btn {
  width: 100% !important;
  box-sizing: border-box !important;
  margin: 0 !important; /* 清除按钮默认margin */
  padding: 12px 0 !important; /* 可选：统一按钮内边距，视觉更协调 */
}
</style>