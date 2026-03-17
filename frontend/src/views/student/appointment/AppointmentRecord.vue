<template>
  <div class="appointment-record-page">
    <el-card class="page-card">
      <template #header>
        <div class="card-header">
          <span>我的预约记录</span>
          <div class="header-actions">
            <el-select v-model="statusFilter" placeholder="状态筛选" clearable style="width: 160px">
              <el-option label="待审核" value="PENDING" />
              <el-option label="已通过" value="APPROVED" />
              <el-option label="已拒绝" value="REJECTED" />
              <el-option label="已取消" value="CANCELLED" />
              <el-option label="已完成" value="COMPLETED" />
            </el-select>

            <el-date-picker
                v-model="dateFilter"
                type="date"
                value-format="YYYY-MM-DD"
                format="YYYY-MM-DD"
                placeholder="预约日期"
                clearable
            />

            <el-button type="primary" @click="loadRecordList">刷新</el-button>
          </div>
        </div>
      </template>

      <el-table
          :data="filteredList"
          border
          style="width: 100%"
          v-loading="loading"
          empty-text="暂无预约记录"
      >
        <el-table-column prop="appointmentNo" label="预约编号" min-width="180" />
        <el-table-column prop="teacherName" label="老师姓名" min-width="110" />
        <el-table-column prop="officeLocation" label="办公地点" min-width="160" />
        <el-table-column prop="appointmentDate" label="预约日期" width="120" />
        <el-table-column label="咨询时间" min-width="150">
          <template #default="scope">
            {{ scope.row.startTime }} - {{ scope.row.endTime }}
          </template>
        </el-table-column>
        <el-table-column prop="purpose" label="预约目的" min-width="180" show-overflow-tooltip />
        <el-table-column label="状态" width="110">
          <template #default="scope">
            <el-tag :type="statusTagType(scope.row.status)">
              {{ statusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="teacherReply" label="老师回复" min-width="180" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="提交时间" min-width="170" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button type="primary" link @click="showDetail(scope.row)">
              详情
            </el-button>
            <el-button
                v-if="canCancel(scope.row.status)"
                type="danger"
                link
                @click="cancelAppointment(scope.row)"
            >
              取消预约
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
        v-model="detailDialogVisible"
        title="预约详情"
        width="650px"
        destroy-on-close
    >
      <el-descriptions :column="1" border v-if="currentRecord.id">
        <el-descriptions-item label="预约编号">
          {{ currentRecord.appointmentNo || "-" }}
        </el-descriptions-item>
        <el-descriptions-item label="老师姓名">
          {{ currentRecord.teacherName || "-" }}
        </el-descriptions-item>
        <el-descriptions-item label="办公地点">
          {{ currentRecord.officeLocation || "-" }}
        </el-descriptions-item>
        <el-descriptions-item label="预约日期">
          {{ currentRecord.appointmentDate || "-" }}
        </el-descriptions-item>
        <el-descriptions-item label="咨询时间">
          {{ currentRecord.startTime || "-" }} - {{ currentRecord.endTime || "-" }}
        </el-descriptions-item>
        <el-descriptions-item label="预约目的">
          {{ currentRecord.purpose || "-" }}
        </el-descriptions-item>
        <el-descriptions-item label="备注">
          {{ currentRecord.remark || "-" }}
        </el-descriptions-item>
        <el-descriptions-item label="老师回复">
          {{ currentRecord.teacherReply || "-" }}
        </el-descriptions-item>
        <el-descriptions-item label="当前状态">
          {{ statusText(currentRecord.status) }}
        </el-descriptions-item>
        <el-descriptions-item label="提交时间">
          {{ currentRecord.createdAt || "-" }}
        </el-descriptions-item>
      </el-descriptions>

      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "AppointmentRecord",
  data() {
    return {
      loading: false,
      recordList: [],
      statusFilter: "",
      dateFilter: "",
      detailDialogVisible: false,
      currentRecord: {}
    };
  },
  computed: {
    filteredList() {
      return (this.recordList || []).filter((item) => {
        const statusMatch = !this.statusFilter || item.status === this.statusFilter;
        const dateMatch = !this.dateFilter || item.appointmentDate === this.dateFilter;
        return statusMatch && dateMatch;
      });
    }
  },
  mounted() {
    this.loadRecordList();
  },
  methods: {
    normalizeResult(res) {
      if (res && typeof res === "object" && Object.prototype.hasOwnProperty.call(res, "code")) {
        return res;
      }
      if (
          res &&
          typeof res === "object" &&
          Object.prototype.hasOwnProperty.call(res, "data") &&
          res.data &&
          typeof res.data === "object" &&
          Object.prototype.hasOwnProperty.call(res.data, "code")
      ) {
        return res.data;
      }
      return res;
    },

    getStudentId() {
      const directId =
          localStorage.getItem("studentId") ||
          localStorage.getItem("student_id") ||
          localStorage.getItem("account");

      if (directId) return directId;

      const userText = localStorage.getItem("user");
      if (userText) {
        try {
          const user = JSON.parse(userText);
          return (
              user.studentId ||
              user.student_id ||
              user.account ||
              user.username ||
              ""
          );
        } catch (e) {
          return "";
        }
      }
      return "";
    },

    statusText(status) {
      const map = {
        PENDING: "待审核",
        APPROVED: "已通过",
        REJECTED: "已拒绝",
        CANCELLED: "已取消",
        COMPLETED: "已完成"
      };
      return map[status] || status || "-";
    },

    statusTagType(status) {
      const map = {
        PENDING: "warning",
        APPROVED: "success",
        REJECTED: "danger",
        CANCELLED: "info",
        COMPLETED: ""
      };
      return map[status] || "info";
    },

    canCancel(status) {
      return status === "PENDING" || status === "APPROVED";
    },

    async loadRecordList() {
      const studentId = this.getStudentId();
      if (!studentId) {
        this.$message.warning("未获取到当前学生学号，请检查登录信息存储");
        return;
      }

      this.loading = true;
      try {
        const res = await request.get("/appointment/my", {
          params: {
            studentId
          }
        });

        const result = this.normalizeResult(res);
        if (result.code === 200) {
          this.recordList = result.data || [];
        } else {
          this.$message.error(result.message || "查询失败");
        }
      } catch (e) {
        this.$message.error("查询失败，请稍后重试");
      } finally {
        this.loading = false;
      }
    },

    showDetail(row) {
      this.currentRecord = { ...row };
      this.detailDialogVisible = true;
    },

    async cancelAppointment(row) {
      try {
        await this.$confirm("确认取消该预约吗？", "提示", {
          type: "warning"
        });
      } catch (e) {
        return;
      }

      const studentId = this.getStudentId();
      if (!studentId) {
        this.$message.warning("未获取到当前学生学号，请检查登录信息存储");
        return;
      }

      try {
        const res = await request.post("/appointment/cancel", {
          appointmentId: row.id,
          studentId
        });

        const result = this.normalizeResult(res);
        if (result.code === 200) {
          this.$message.success(result.message || "取消成功");
          this.loadRecordList();
        } else {
          this.$message.error(result.message || "取消失败");
        }
      } catch (e) {
        this.$message.error("取消失败，请稍后重试");
      }
    }
  }
};
</script>

<style scoped>
.appointment-record-page {
  padding: 20px;
}

.page-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}
</style>