<template>
  <el-card class="page-card">
    <template #header>
      <div class="card-header">
        <span>辅导员管理</span>
        <el-button type="primary" @click="openAddDialog">新增辅导员</el-button>
      </div>
    </template>

    <div class="toolbar">
      <el-input
          v-model="keyword"
          placeholder="请输入辅导员账号或姓名搜索"
          clearable
          class="search-input"
      />
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%">
      <el-table-column type="index" label="序号" width="70" align="center" />
      <el-table-column prop="counselorAccount" label="辅导员账号" min-width="140" />
      <el-table-column prop="counselorName" label="辅导员姓名" min-width="120" />
      <el-table-column prop="gender" label="性别" width="80" align="center" />
      <el-table-column prop="phone" label="联系电话" min-width="140" />
      <el-table-column prop="college" label="所属学院" min-width="160" />
      <el-table-column label="状态" width="100" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? "在岗" : "停用" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" @click="openEditDialog(scope.row)">
            编辑
          </el-button>
          <el-button
              :type="scope.row.status === 1 ? 'warning' : 'success'"
              size="small"
              @click="toggleStatus(scope.row)"
          >
            {{ scope.row.status === 1 ? "停用" : "启用" }}
          </el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
        v-model="dialogVisible"
        :title="form.id ? '编辑辅导员' : '新增辅导员'"
        width="560px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="辅导员账号">
          <el-input v-model="form.counselorAccount" placeholder="请输入辅导员账号" />
        </el-form-item>
        <el-form-item label="辅导员姓名">
          <el-input v-model="form.counselorName" placeholder="请输入辅导员姓名" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="所属学院">
          <el-input v-model="form.college" placeholder="请输入所属学院" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">在岗</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
              v-model="form.remark"
              type="textarea"
              :rows="3"
              placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { computed, reactive, ref } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"

const keyword = ref("")
const dialogVisible = ref(false)

const list = ref([
  {
    id: 1,
    counselorAccount: "counselor001",
    counselorName: "张辅导员",
    gender: "女",
    phone: "13800001001",
    college: "信息工程学院",
    status: 1,
    remark: "负责2022级学生工作"
  },
  {
    id: 2,
    counselorAccount: "counselor002",
    counselorName: "李辅导员",
    gender: "男",
    phone: "13800001002",
    college: "经济管理学院",
    status: 1,
    remark: "负责2023级学生工作"
  },
  {
    id: 3,
    counselorAccount: "counselor003",
    counselorName: "王辅导员",
    gender: "女",
    phone: "13800001003",
    college: "外国语学院",
    status: 0,
    remark: "测试账号"
  }
])

const form = reactive({
  id: null,
  counselorAccount: "",
  counselorName: "",
  gender: "女",
  phone: "",
  college: "",
  status: 1,
  remark: ""
})

const tableData = computed(() => {
  if (!keyword.value) return list.value
  return list.value.filter(item =>
      item.counselorAccount.includes(keyword.value) ||
      item.counselorName.includes(keyword.value)
  )
})

const resetForm = () => {
  form.id = null
  form.counselorAccount = ""
  form.counselorName = ""
  form.gender = "女"
  form.phone = ""
  form.college = ""
  form.status = 1
  form.remark = ""
}

const handleSearch = () => {
  ElMessage.success("查询完成")
}

const handleReset = () => {
  keyword.value = ""
}

const openAddDialog = () => {
  resetForm()
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  form.id = row.id
  form.counselorAccount = row.counselorAccount
  form.counselorName = row.counselorName
  form.gender = row.gender
  form.phone = row.phone
  form.college = row.college
  form.status = row.status
  form.remark = row.remark
  dialogVisible.value = true
}

const handleSubmit = () => {
  if (!form.counselorAccount || !form.counselorName) {
    ElMessage.warning("请填写完整的辅导员账号和姓名")
    return
  }

  if (form.id) {
    const index = list.value.findIndex(item => item.id === form.id)
    if (index !== -1) {
      list.value[index] = { ...form }
    }
    ElMessage.success("辅导员信息修改成功")
  } else {
    list.value.unshift({
      ...form,
      id: Date.now()
    })
    ElMessage.success("辅导员新增成功")
  }

  dialogVisible.value = false
}

const toggleStatus = (row) => {
  row.status = row.status === 1 ? 0 : 1
  ElMessage.success("状态修改成功")
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确认删除辅导员【${row.counselorName}】吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    })

    list.value = list.value.filter(item => item.id !== row.id)
    ElMessage.success("删除成功")
  } catch (e) {
    return
  }
}
</script>

<style scoped>
.page-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 18px;
}

.search-input {
  width: 300px;
}
</style>