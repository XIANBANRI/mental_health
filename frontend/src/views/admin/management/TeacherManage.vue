<template>
  <el-card class="page-card">
    <template #header>
      <div class="card-header">
        <span>心理老师管理</span>
        <el-button type="primary" @click="openAddDialog">新增心理老师</el-button>
      </div>
    </template>

    <div class="toolbar">
      <el-input
          v-model="keyword"
          placeholder="请输入老师账号或姓名搜索"
          clearable
          class="search-input"
      />
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%">
      <el-table-column type="index" label="序号" width="70" align="center" />
      <el-table-column prop="teacherAccount" label="老师账号" min-width="130" />
      <el-table-column prop="teacherName" label="老师姓名" min-width="120" />
      <el-table-column prop="gender" label="性别" width="80" align="center" />
      <el-table-column prop="phone" label="联系电话" min-width="140" />
      <el-table-column prop="specialty" label="擅长方向" min-width="180" show-overflow-tooltip />
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
        :title="form.id ? '编辑心理老师' : '新增心理老师'"
        width="560px"
    >
      <el-form :model="form" label-width="95px">
        <el-form-item label="老师账号">
          <el-input v-model="form.teacherAccount" placeholder="请输入老师账号" />
        </el-form-item>
        <el-form-item label="老师姓名">
          <el-input v-model="form.teacherName" placeholder="请输入老师姓名" />
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
        <el-form-item label="擅长方向">
          <el-input v-model="form.specialty" placeholder="请输入擅长方向" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">在岗</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="简介">
          <el-input
              v-model="form.introduction"
              type="textarea"
              :rows="3"
              placeholder="请输入老师简介"
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
    teacherAccount: "teacher001",
    teacherName: "陈老师",
    gender: "女",
    phone: "13900000001",
    specialty: "情绪疏导、压力管理",
    status: 1,
    introduction: "擅长大学生常见心理问题咨询"
  },
  {
    id: 2,
    teacherAccount: "teacher002",
    teacherName: "周老师",
    gender: "男",
    phone: "13900000002",
    specialty: "人际关系、学业焦虑",
    status: 1,
    introduction: "长期从事高校心理咨询工作"
  },
  {
    id: 3,
    teacherAccount: "teacher003",
    teacherName: "吴老师",
    gender: "女",
    phone: "13900000003",
    specialty: "睡眠问题、情绪调节",
    status: 0,
    introduction: "当前为停用状态"
  }
])

const form = reactive({
  id: null,
  teacherAccount: "",
  teacherName: "",
  gender: "女",
  phone: "",
  specialty: "",
  status: 1,
  introduction: ""
})

const tableData = computed(() => {
  if (!keyword.value) return list.value
  return list.value.filter(item =>
      item.teacherAccount.includes(keyword.value) ||
      item.teacherName.includes(keyword.value)
  )
})

const resetForm = () => {
  form.id = null
  form.teacherAccount = ""
  form.teacherName = ""
  form.gender = "女"
  form.phone = ""
  form.specialty = ""
  form.status = 1
  form.introduction = ""
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
  form.teacherAccount = row.teacherAccount
  form.teacherName = row.teacherName
  form.gender = row.gender
  form.phone = row.phone
  form.specialty = row.specialty
  form.status = row.status
  form.introduction = row.introduction
  dialogVisible.value = true
}

const handleSubmit = () => {
  if (!form.teacherAccount || !form.teacherName) {
    ElMessage.warning("请填写完整的老师账号和姓名")
    return
  }

  if (form.id) {
    const index = list.value.findIndex(item => item.id === form.id)
    if (index !== -1) {
      list.value[index] = { ...form }
    }
    ElMessage.success("心理老师信息修改成功")
  } else {
    list.value.unshift({
      ...form,
      id: Date.now()
    })
    ElMessage.success("心理老师新增成功")
  }

  dialogVisible.value = false
}

const toggleStatus = (row) => {
  row.status = row.status === 1 ? 0 : 1
  ElMessage.success("状态修改成功")
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确认删除心理老师【${row.teacherName}】吗？`, "提示", {
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