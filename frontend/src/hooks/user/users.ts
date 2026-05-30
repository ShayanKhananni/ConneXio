// hooks/useCurrentUser.ts

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useAuthStore } from "@/store/authStore";
import { getUserDetailsApi, updatePasswordApi, updateUserDetailsApi } from "@/api/userApi";
import type { UpdateProfileForm } from "@/zod/userProfileSchema";
import type { UpdatePasswordForm } from "@/zod/changePasswordSchema";

export const useCurrentUser = () => {
  const token = useAuthStore((state) => state.token);

  return useQuery({
    queryKey: ["profile"],
    queryFn: getUserDetailsApi,
    enabled: !!token,
    staleTime: 1000 * 60 * 60 * 3, // 3 hours
  });
};


export const useUpdateUser = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: Partial<UpdateProfileForm>) =>
      updateUserDetailsApi(data),

    onSuccess: (updatedUser) => {
      queryClient.setQueryData(["profile"], updatedUser);
    },
  })}


  export const useUpdatePassword = () => {
    return useMutation({
      mutationFn: (data: UpdatePasswordForm) =>
        updatePasswordApi(data),  
    })
  }