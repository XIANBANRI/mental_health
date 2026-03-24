<template>
  <el-card class="page-card">
    <template #header>
      <div class="card-header">
        <span>测试输入</span>
        <el-button type="primary" @click="openAddDialog">新增测试</el-button>
      </div>
    </template>

    <el-table :data="testList" border style="width: 100%">
      <el-table-column type="index" label="序号" width="70" align="center" />
      <el-table-column prop="scaleCode" label="量表编号" min-width="120" />
      <el-table-column prop="scaleName" label="量表名称" min-width="180" />
      <el-table-column prop="questionCount" label="题目数" width="100" align="center" />
      <el-table-column prop="description" label="量表说明" min-width="260" show-overflow-tooltip />
      <el-table-column label="状态" width="100" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? "启用" : "停用" }}
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
        :title="form.id ? '编辑测试' : '新增测试'"
        width="560px"
    >
      <el-form :model="form" label-width="95px">
        <el-form-item label="量表编号">
          <el-input v-model="form.scaleCode" placeholder="如 PHQ9" />
        </el-form-item>
        <el-form-item label="量表名称">
          <el-input v-model="form.scaleName" placeholder="请输入量表名称" />
        </el-form-item>
        <el-form-item label="题目数量">
          <el-input-number v-model="form.questionCount" :min="1" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="量表说明">
          <el-input
              v-model="form.description"
              type="textarea"
              :rows="4"
              placeholder="请输入量表说明"
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
import { reactive, ref } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"

const dialogVisible = ref(false)

const testList = ref([
  {
    id: 1,
    scaleCode: "PHQ9",
    scaleName: "PHQ-9 抑郁筛查量表",
    questionCount: 9,
    description: "用于抑郁情绪的初步筛查",
    status: 1
  },
  {
    id: 2,
    scaleCode: "GAD7",
    scaleName: "GAD-7 焦虑筛查量表",
    questionCount: 7,
    description: "用于焦虑症状的初步评估",
    status: 1
  },
  {
    id: 3,
    scaleCode: "SCL90",
    scaleName: "SCL-90 症状自评量表",
    questionCount: 90,
    description: "用于较全面的心理症状评估",
    status: 0
  }
])

const form = reactive({
  id: null,
  scaleCode: "",
  scaleName: "",
  questionCount: 1,
  description: "",
  status: 1
})

const resetForm = () => {
  form.id = null
  form.scaleCode = ""
  form.scaleName = ""
  form.questionCount = 1
  form.description = ""
  form.status = 1
}

const openAddDialog = () => {
  resetForm()
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  form.id = row.id
  form.scaleCode = row.scaleCode
  form.scaleName = row.scaleName
  form.questionCount = row.questionCount
  form.description = row.description
  form.status = row.status
  dialogVisible.value = true
}

const handleSubmit = () => {
  if (!form.scaleCode || !form.scaleName) {
    ElMessage.warning("请填写量表编号和量表名称")
    return
  }

  if (form.id) {
    const index = testList.value.findIndex(item => item.id === form.id)
    if (index !== -1) {
      testList.value[index] = { ...form }
    }
    ElMessage.success("测试信息修改成功")
  } else {
    testList.value.unshift({
      ...form,
      id: Date.now()
    })
    ElMessage.success("测试新增成功")
  }

  dialogVisible.value = false
}

const toggleStatus = (row) => {
  row.status = row.status === 1 ? 0 : 1
  ElMessage.success("状态修改成功")
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确认删除测试【${row.scaleName}】吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    })

    testList.value = testList.value.filter(item => item.id !== row.id)
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
</style>