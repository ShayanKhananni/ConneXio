import { useAuthStore } from '@/store/authStore'
import axios from 'axios'


const api = axios.create({
  baseURL: 'http://localhost:8080/api',
})

// Attach token to every request
api.interceptors.request.use((config) => {
  const token = useAuthStore.getState().token
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Auto logout on 401
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      useAuthStore.getState().logout()
      // window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)



export default api