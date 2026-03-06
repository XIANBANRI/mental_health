import { createRouter, createWebHistory } from "vue-router"

import LoginPage from "../views/LoginPage.vue"
import ForgetPassword from "../views/ForgetPassword.vue"

import StudentHome from "../views/student/StudentHome.vue"
import TeacherHome from "../views/teacher/TeacherHome.vue"
import CounselorHome from "../views/counselor/CounselorHome.vue"
import AdminHome from "../views/admin/AdminHome.vue"

const routes = [

  {
    path: "/",
    component: LoginPage
  },

  {
    path: "/forget",
    component: ForgetPassword
  },

  {
    path: "/student",
    component: StudentHome
  },

  {
    path: "/teacher",
    component: TeacherHome
  },

  {
    path: "/counselor",
    component: CounselorHome
  },

  {
    path: "/admin",
    component: AdminHome
  }

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router