import type { LoginCredentials, LoginResponse } from '@/types/authTypes'
import api from './axiosInstance'

export const loginApi = async (credentials:LoginCredentials) : Promise<LoginResponse> => {
  const response = await api.post('/login', credentials)
  return response.data 
}

export const registerApi = async (data) => {
  const response = await api.post('/signup', data)
  return response.data
}