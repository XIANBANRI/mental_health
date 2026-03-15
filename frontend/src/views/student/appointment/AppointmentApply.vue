<template>
  <el-card class="page-card">
    <template #header>
      <span>心理预约</span>
    </template>

    <el-form :model="form" label-width="100px" class="appointment-form">
      <el-form-item label="预约老师">
        <el-select v-model="form.teacher" placeholder="请选择心理老师" style="width: 100%">
          <el-option label="李老师" value="李老师" />
          <el-option label="王老师" value="王老师" />
          <el-option label="周老师" value="周老师" />
        </el-select>
      </el-form-item>

      <el-form-item label="预约日期">
        <el-date-picker
            v-model="form.date"
            type="date"
            placeholder="请选择日期"
            style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="预约时间">
        <el-time-picker
            v-model="form.time"
            placeholder="请选择时间"
            style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="预约说明">
        <el-input
            v-model="form.remark"
            type="textarea"
            :rows="4"
            placeholder="请输入预约说明"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitAppointment">提交预约</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { reactive } from "vue"
import { ElMessage } from "element-plus"

const form = reactive({
  teacher: "",
  date: "",
  time: "",
  remark: ""
})

const submitAppointment = () => {
  if (!form.teacher || !form.date || !form.time) {
    ElMessage.error("请填写完整预约信息")
    return
  }

  ElMessage.success("预约提交成功（当前为静态页面）")
}
</script>

<style scoped>
.page-card {
  border-radius: 12px;
}

.appointment-form {
  max-width: 700px;
}
</style>