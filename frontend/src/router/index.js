import { createRouter, createWebHistory } from "vue-router"

import LoginPage from "@/views/LoginPage.vue"
import ForgetPassword from "@/views/ForgetPassword.vue"

import AdminHome from "@/views/admin/AdminHome.vue"
import CounselorHome from "@/views/counselor/CounselorHome.vue"
import StudentHome from "@/views/student/StudentHome.vue"
import TeacherHome from "@/views/teacher/TeacherHome.vue"

const routes = [
  {
    path: "/",
    redirect: "/login"
  },
  {
    path: "/login",
    name: "login",
    component: LoginPage
  },
  {
    path: "/forget",
    name: "forget",
    component: ForgetPassword
  },
  {
    path: "/student",
    name: "student",
    component: StudentHome
  },
  {
    path: "/teacher",
    name: "teacher",
    component: TeacherHome
  },
  {
    path: "/counselor",
    name: "counselor",
    component: CounselorHome
  },
  {
    path: "/admin",
    name: "admin",
    component: AdminHome
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router