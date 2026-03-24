import axios from "axios"

const request = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 15000,
  headers: {
    "Content-Type": "application/json"
  }
})

request.interceptors.response.use(
    response => response,
    error => Promise.reject(error)
)

export default request