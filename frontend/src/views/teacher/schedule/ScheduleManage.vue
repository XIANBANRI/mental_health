<template>
  <div class="schedule-manage-page">
    <el-card class="page-card">
      <template #header>
        <span>工作时间管理</span>
      </template>

      <div class="toolbar">
        <el-button type="primary" @click="openAddDialog">新增工作时间</el-button>
      </div>

      <el-table :data="scheduleList" border style="width: 100%">
        <el-table-column prop="weekDay" label="星期" width="120">
          <template #default="scope">
            {{ formatWeekDay(scope.row.weekDay) }}
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="150" />
        <el-table-column prop="endTime" label="结束时间" width="150" />
        <el-table-column prop="maxAppointments" label="最大预约人数" width="140" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-switch
                v-model="scope.row.status"
                :active-value="1"
                :inactive-value="0"
            />
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="180" />
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button size="small" type="primary" @click="openEditDialog(scope.row)">
              编辑
            </el-button>
            <el-button size="small" type="danger" @click="deleteSchedule(scope.$index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
        v-model="dialogVisible"
        :title="dialogType === 'add' ? '新增工作时间' : '编辑工作时间'"
        width="520px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="110px">
        <el-form-item label="星期" prop="weekDay">
          <el-select v-model="form.weekDay" placeholder="请选择星期" style="width: 100%">
            <el-option label="星期一" :value="1" />
            <el-option label="星期二" :value="2" />
            <el-option label="星期三" :value="3" />
            <el-option label="星期四" :value="4" />
            <el-option label="星期五" :value="5" />
            <el-option label="星期六" :value="6" />
            <el-option label="星期日" :value="7" />
          </el-select>
        </el-form-item>

        <el-form-item label="开始时间" prop="startTime">
          <el-time-select
              v-model="form.startTime"
              start="08:00"
              step="00:30"
              end="20:00"
              placeholder="选择开始时间"
              style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="结束时间" prop="endTime">
          <el-time-select
              v-model="form.endTime"
              start="08:30"
              step="00:30"
              end="20:30"
              placeholder="选择结束时间"
              style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="最大预约人数" prop="maxAppointments">
          <el-input-number v-model="form.maxAppointments" :min="1" :max="20" style="width: 100%" />
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
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
        <el-button type="primary" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"

const dialogVisible = ref(false)
const dialogType = ref("add")
const formRef = ref()
const editIndex = ref(-1)

const scheduleList = ref([
  {
    id: 1,
    weekDay: 1,
    startTime: "08:00",
    endTime: "10:00",
    maxAppointments: 2,
    status: 1,
    remark: "周一上午值班"
  },
  {
    id: 2,
    weekDay: 3,
    startTime: "14:00",
    endTime: "16:00",
    maxAppointments: 2,
    status: 1,
    remark: "周三下午咨询"
  }
])

const form = reactive({
  id: "",
  weekDay: "",
  startTime: "",
  endTime: "",
  maxAppointments: 1,
  status: 1,
  remark: ""
})

const rules = {
  weekDay: [{ required: true, message: "请选择星期", trigger: "change" }],
  startTime: [{ required: true, message: "请选择开始时间", trigger: "change" }],
  endTime: [{ required: true, message: "请选择结束时间", trigger: "change" }],
  maxAppointments: [{ required: true, message: "请输入最大预约人数", trigger: "change" }]
}

const formatWeekDay = (value) => {
  const map = {
    1: "星期一",
    2: "星期二",
    3: "星期三",
    4: "星期四",
    5: "星期五",
    6: "星期六",
    7: "星期日"
  }
  return map[value] || "未知"
}

const resetForm = () => {
  form.id = ""
  form.weekDay = ""
  form.startTime = ""
  form.endTime = ""
  form.maxAppointments = 1
  form.status = 1
  form.remark = ""
}

const openAddDialog = () => {
  dialogType.value = "add"
  editIndex.value = -1
  resetForm()
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  dialogType.value = "edit"
  editIndex.value = scheduleList.value.findIndex(item => item.id === row.id)
  form.id = row.id
  form.weekDay = row.weekDay
  form.startTime = row.startTime
  form.endTime = row.endTime
  form.maxAppointments = row.maxAppointments
  form.status = row.status
  form.remark = row.remark || ""
  dialogVisible.value = true
}

const submitForm = async () => {
  if (!formRef.value) return

  await formRef.value.validate((valid) => {
    if (!valid) return

    const payload = {
      id: form.id || Date.now(),
      weekDay: form.weekDay,
      startTime: form.startTime,
      endTime: form.endTime,
      maxAppointments: form.maxAppointments,
      status: form.status,
      remark: form.remark
    }

    if (dialogType.value === "add") {
      scheduleList.value.push(payload)
      ElMessage.success("新增成功")
    } else if (editIndex.value > -1) {
      scheduleList.value[editIndex.value] = payload
      ElMessage.success("修改成功")
    }

    dialogVisible.value = false
  })
}

const deleteSchedule = async (index) => {
  try {
    await ElMessageBox.confirm("确认删除该工作时间吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    })

    scheduleList.value.splice(index, 1)
    ElMessage.success("删除成功")
  } catch (e) {
    // 取消
  }
}
</script>

<style scoped>
.schedule-manage-page {
  min-height: 100%;
}

.page-card {
  border-radius: 10px;
}

.toolbar {
  margin-bottom: 18px;
}
</style>