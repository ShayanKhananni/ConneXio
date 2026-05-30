import type { UserDetailsDTO } from '@/types/authTypes'
import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface AuthState {
  token: string | null
  user: UserDetailsDTO | null

  setToken: (token: string) => void
  setUser: (user: UserDetailsDTO) => void
  logout: () => void
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      token: null,
      user: null,

      setToken: (token) => set({ token }),
      setUser: (user) => set({ user }),

      logout: () => {
        console.log("logout Called")
        set({ token: null, user: null })},
    }),
    { name: 'auth-storage' }
  )
)