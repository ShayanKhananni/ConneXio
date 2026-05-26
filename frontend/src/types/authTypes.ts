import { z } from 'zod'


export const loginSchema = z.object({
  username: z.string().nonempty("Username is required").min(3, "Username must be at least 5 characters"),
  password: z.string().nonempty("Password is required").min(3, "Password must be at least 3 characters"),
})

export const registerSchema = z.object({
  username: z.string().nonempty("Username is required").min(5, "Username must be at least 5 characters"),
  email: z.string().email("Invalid email"),
  password: z.string().nonempty("Password is required").min(6, "Min 6 characters"),
})

export type LoginCredentials = z.infer<typeof loginSchema>
export type RegisterCredentials = z.infer<typeof registerSchema>


export interface AuthUser {
  id: string
  username: string
  email: string
}

export interface LoginResponse {
  token: string
}