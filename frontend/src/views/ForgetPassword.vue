<template>

  <div class="forget-container">

    <el-card class="forget-card">

      <h2>找回密码</h2>

      <el-form :model="form">

        <el-form-item label="账号">
          <el-input v-model="form.username"/>
        </el-form-item>

        <el-form-item label="手机号">
          <el-input v-model="form.phone"/>
        </el-form-item>

        <el-form-item label="新密码">
          <el-input v-model="form.password" type="password"/>
        </el-form-item>

        <el-button type="primary" style="width:100%" @click="reset">
          重置密码
        </el-button>

        <el-button style="width:100%;margin-top:10px" @click="backLogin">
          返回登录
        </el-button>

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
  username:"",
  phone:"",
  password:""
})

const reset = () => {

  ElMessage.success("密码重置成功")

  router.push("/")

}

const backLogin = () => {

  router.push("/")

}

</script>

<style scoped>

.forget-container{
  height:100vh;
  display:flex;
  justify-content:center;
  align-items:center;
  background:linear-gradient(120deg,#89f7fe,#66a6ff);
}

.forget-card{
  width:420px;
}

h2{
  text-align:center;
  margin-bottom:20px;
}

</style>