<template>
  <div class="teacher-welcome">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="welcome-card">
          <template #header>
            <span>欢迎使用心理老师端</span>
          </template>

          <div class="welcome-content">
            <h2>您好，{{ teacherName || "心理老师" }}</h2>
            <p>您可以在此页面进行工作时间管理、预约查询、预约记录查看以及个人信息查询。</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="card-row">
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-title">工作时间管理</div>
          <div class="stat-desc">维护每周可预约时间段，并以日历方式查看每日安排。</div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-title">预约管理</div>
          <div class="stat-desc">查看当前预约信息和历史预约记录，便于管理学生咨询安排。</div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-title">个人信息查询</div>
          <div class="stat-desc">查看您的老师账号、手机号、姓名和办公地点等资料。</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue"

const teacherName = ref("")

onMounted(() => {
  teacherName.value = localStorage.getItem("teacherName") || ""
})
</script>

<style scoped>
.teacher-welcome {
  min-height: 100%;
}

.welcome-card {
  border-radius: 10px;
}

.welcome-content h2 {
  margin: 0 0 12px;
  color: #303133;
}

.welcome-content p {
  margin: 0;
  color: #606266;
  line-height: 1.8;
}

.card-row {
  margin-top: 20px;
}

.stat-card {
  min-height: 140px;
  border-radius: 10px;
}

.stat-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 14px;
}

.stat-desc {
  font-size: 14px;
  color: #606266;
  line-height: 1.8;
}
</style>