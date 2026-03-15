<template>
  <div class="assessment-page">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="page-card">
          <template #header>
            <div class="card-header">
              <span>心理测试列表</span>
            </div>
          </template>

          <div class="semester-box">
            <span class="semester-label">当前学期：</span>
            <el-select v-model="selectedSemester" placeholder="请选择学期" style="width: 180px">
              <el-option
                  v-for="item in semesterOptions"
                  :key="item"
                  :label="item"
                  :value="item"
              />
            </el-select>
          </div>

          <el-table :data="scaleList" style="width: 100%" v-loading="listLoading">
            <el-table-column prop="scaleName" label="量表名称" min-width="160" />
            <el-table-column prop="questionCount" label="题数" width="80" />
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button type="primary" size="small" @click="loadDetail(scope.row)">
                  开始测试
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card class="page-card" v-if="currentScale">
          <template #header>
            <div class="card-header">
              <span>{{ currentScale.scaleName }}</span>
              <el-tag type="info">{{ selectedSemester }}</el-tag>
            </div>
          </template>

          <div class="scale-desc">{{ currentScale.description }}</div>

          <div
              v-for="question in currentScale.questions"
              :key="question.id"
              class="question-block"
          >
            <div class="question-title">
              {{ question.questionNo }}. {{ question.questionText }}
            </div>

            <el-radio-group v-model="answerMap[question.id]">
              <el-radio
                  v-for="option in question.options"
                  :key="option.id"
                  :label="option.id"
                  class="option-item"
              >
                {{ option.optionText }}
              </el-radio>
            </el-radio-group>
          </div>

          <div class="submit-area">
            <el-button @click="resetAnswers">重置作答</el-button>
            <el-button type="primary" :loading="submitLoading" @click="submitAssessment">
              提交测试
            </el-button>
          </div>
        </el-card>

        <el-empty v-else description="请选择左侧量表开始测试" />
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import request from "@/utils/request"

const listLoading = ref(false)
const submitLoading = ref(false)

const scaleList = ref([])
const currentScale = ref(null)
const currentScaleId = ref(null)
const answerMap = reactive({})
const selectedSemester = ref("第1学期")
const semesterOptions = [
  "第1学期",
  "第2学期",
  "第3学期",
  "第4学期",
  "第5学期",
  "第6学期",
  "第7学期",
  "第8学期"
]

const clearAnswerMap = () => {
  Object.keys(answerMap).forEach((key) => delete answerMap[key])
}

const resetAnswers = () => {
  clearAnswerMap()
  ElMessage.success("已重置当前作答")
}

const loadScaleList = async () => {
  listLoading.value = true
  try {
    const res = await request.get("/api/student/assessment/scales")
    const result = res.data || {}

    if (result.success) {
      scaleList.value = result.data || []
    } else {
      ElMessage.error(result.message || "量表列表加载失败")
    }
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || "量表列表加载失败")
  } finally {
    listLoading.value = false
  }
}

const loadDetail = async (scale) => {
  try {
    const res = await request.get(`/api/student/assessment/detail/${scale.id}`)
    const result = res.data || {}

    if (result.success) {
      currentScale.value = result.data
      currentScaleId.value = scale.id
      clearAnswerMap()
    } else {
      ElMessage.error(result.message || "量表详情加载失败")
    }
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || "量表详情加载失败")
  }
}

const buildResultMessage = (data) => {
  const totalScoreText =
      data.healthTotalScore === null || data.healthTotalScore === undefined
          ? "未完成"
          : data.healthTotalScore

  return [
    `量表：${data.scaleName || ""}`,
    `学期：${data.semester || selectedSemester.value}`,
    `当前量表得分：${data.scaleScore ?? 0}`,
    `当前量表等级：${data.scaleResultLevel || "未分级"}`,
    `当前量表说明：${data.scaleResultSummary || "暂无说明"}`,
    `已完成进度：${data.completedCount || 0}/${data.totalScaleCount || 4}`,
    `综合总分：${totalScoreText}`,
    `综合状态：${data.healthStatus || "未完成"}`,
    `综合说明：${data.healthSummary || "四项量表未全部完成，暂不生成综合总分"}`
  ].join("\n")
}

const submitAssessment = async () => {
  const studentId = localStorage.getItem("studentId")

  if (!studentId) {
    ElMessage.error("未获取到学生学号，请重新登录")
    return
  }

  if (!currentScale.value || !currentScaleId.value) {
    ElMessage.error("请先选择量表")
    return
  }

  const questions = currentScale.value.questions || []
  const answers = []

  for (const question of questions) {
    const selectedOptionId = answerMap[question.id]
    if (!selectedOptionId) {
      ElMessage.error(`请完成第 ${question.questionNo} 题`)
      return
    }

    answers.push({
      questionId: question.id,
      optionId: selectedOptionId
    })
  }

  submitLoading.value = true
  try {
    const res = await request.post("/api/student/assessment/submit", {
      studentId,
      semester: selectedSemester.value,
      scaleId: currentScaleId.value,
      answers
    })

    const result = res.data || {}

    if (result.success) {
      const data = result.data || {}

      await ElMessageBox.alert(buildResultMessage(data), "测试完成", {
        confirmButtonText: "确定"
      })

      currentScale.value = null
      currentScaleId.value = null
      clearAnswerMap()
      loadScaleList()
    } else {
      ElMessage.error(result.message || "提交失败")
    }
  } catch (error) {
    ElMessage.error(error?.response?.data?.message || "提交失败")
  } finally {
    submitLoading.value = false
  }
}

onMounted(() => {
  loadScaleList()
})
</script>

<style scoped>
.assessment-page {
  width: 100%;
}

.page-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.semester-box {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.semester-label {
  margin-right: 10px;
  color: #606266;
}

.scale-desc {
  margin-bottom: 20px;
  color: #606266;
  line-height: 1.8;
}

.question-block {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.question-title {
  margin-bottom: 12px;
  font-weight: 600;
  color: #303133;
  line-height: 1.6;
}

.option-item {
  display: block;
  margin-bottom: 10px;
}

.submit-area {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 20px;
}
</style>
