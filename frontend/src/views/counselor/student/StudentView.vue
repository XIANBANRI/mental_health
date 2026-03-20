<template>
  <el-card class="page-card">
    <template #header>
      <div class="header-bar">
        <span>学生查看</span>
        <el-input
            v-model="keyword"
            placeholder="请输入学号/姓名/班级"
            clearable
            class="search-input"
        />
      </div>
    </template>

    <el-table :data="filteredList" border style="width: 100%">
      <el-table-column prop="studentId" label="学号" width="120" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="gender" label="性别" width="80" />
      <el-table-column prop="className" label="班级" />
      <el-table-column prop="college" label="学院" />
      <el-table-column prop="grade" label="年级" width="100" />
      <el-table-column prop="phone" label="联系电话" width="140" />
      <el-table-column prop="riskLevel" label="风险等级" width="100">
        <template #default="scope">
          <el-tag
              :type="
              scope.row.riskLevel === '高'
                ? 'danger'
                : scope.row.riskLevel === '中'
                ? 'warning'
                : 'success'
            "
          >
            {{ scope.row.riskLevel }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, computed } from "vue"

const keyword = ref("")

const studentList = ref([
  {
    studentId: "20230001",
    name: "张三",
    gender: "男",
    className: "计科2301",
    college: "计算机学院",
    grade: "2023级",
    phone: "13800000001",
    riskLevel: "低"
  },
  {
    studentId: "20230002",
    name: "李四",
    gender: "女",
    className: "计科2301",
    college: "计算机学院",
    grade: "2023级",
    phone: "13800000002",
    riskLevel: "中"
  },
  {
    studentId: "20230003",
    name: "王五",
    gender: "男",
    className: "软工2302",
    college: "计算机学院",
    grade: "2023级",
    phone: "13800000003",
    riskLevel: "高"
  },
  {
    studentId: "20220015",
    name: "赵六",
    gender: "女",
    className: "网工2201",
    college: "计算机学院",
    grade: "2022级",
    phone: "13800000004",
    riskLevel: "低"
  }
])

const filteredList = computed(() => {
  const key = keyword.value.trim()
  if (!key) return studentList.value

  return studentList.value.filter(
      item =>
          item.studentId.includes(key) ||
          item.name.includes(key) ||
          item.className.includes(key)
  )
})
</script>

<style scoped>
.page-card {
  border-radius: 8px;
}

.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-input {
  width: 260px;
}
</style>