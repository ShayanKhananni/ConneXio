import { useMutation } from '@tanstack/react-query'
import { useAuthStore } from '../../store/authStore'
import { loginApi, registerApi } from '../../api/authApi'
import type { UseFormSetError } from 'react-hook-form'
import type { LoginCredentials, RegisterCredentials } from '@/types/authTypes'


export const useLogin = (setError: UseFormSetError<LoginCredentials>) => {
  
  const setToken = useAuthStore((state) => state.setToken)

  return useMutation({
    mutationFn: loginApi,

    onSuccess: (data) => {
      console.log(data);
      setToken(data.token);
    },
    
    onError: (error:any) => 
    {
      const message = error?.response?.data?.message || "Something went wrong"
      console.error("Login error:", message);
      setError("root", { message })  
    }
  })
}


export const useSignup = (setError: UseFormSetError<RegisterCredentials>) =>
{
  return useMutation({
    
    mutationFn: registerApi,

    onSuccess: (data) => {
      console.log("Signup successful:", data);
      // navigation
    },

    onError: (error:any) => 
      {
        const message = error?.response?.data?.message || "Something went wrong"
        console.error("Login error:", message);
        setError("root", { message })  
      }

  })
}
