import { createRouter, createWebHistory } from "vue-router"

import LoginPage from "@/views/LoginPage.vue"
import ForgetPassword from "@/views/ForgetPassword.vue"

import StudentHome from "@/views/student/StudentHome.vue"
import StudentWelcome from "@/views/student/StudentWelcome.vue"
import PsychologyTest from "@/views/student/assessment/PsychologyTest.vue"
import TestRecord from "@/views/student/assessment/TestRecord.vue"
import AppointmentApply from "@/views/student/appointment/AppointmentApply.vue"
import AppointmentRecord from "@/views/student/appointment/AppointmentRecord.vue"
import StudentProfile from "@/views/student/profile/StudentProfile.vue"

import TeacherHome from "@/views/teacher/TeacherHome.vue"
import CounselorHome from "@/views/counselor/CounselorHome.vue"
import AdminHome from "@/views/admin/AdminHome.vue"

const routes = [
  {
    path: "/",
    name: "Login",
    component: LoginPage
  },
  {
    path: "/forget",
    name: "ForgetPassword",
    component: ForgetPassword
  },
  {
    path: "/student",
    component: StudentHome,
    children: [
      {
        path: "",
        name: "StudentWelcome",
        component: StudentWelcome
      },
      {
        path: "assessment/test",
        name: "PsychologyTest",
        component: PsychologyTest
      },
      {
        path: "assessment/record",
        name: "TestRecord",
        component: TestRecord
      },
      {
        path: "appointment/apply",
        name: "AppointmentApply",
        component: AppointmentApply
      },
      {
        path: "appointment/record",
        name: "AppointmentRecord",
        component: AppointmentRecord
      },
      {
        path: "profile",
        name: "StudentProfile",
        component: StudentProfile
      }
    ]
  },
  {
    path: "/teacher",
    name: "TeacherHome",
    component: TeacherHome
  },
  {
    path: "/counselor",
    name: "CounselorHome",
    component: CounselorHome
  },
  {
    path: "/admin",
    name: "AdminHome",
    component: AdminHome
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router