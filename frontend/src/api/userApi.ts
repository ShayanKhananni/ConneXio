import type { UserDetails } from "@/types/userTypes";
import api from "./axiosInstance";
import type { UpdateProfileForm } from "@/zod/userProfileSchema";
import type { UpdatePasswordForm } from "@/zod/changePasswordSchema";


export const getUserDetailsApi = async (): Promise<UserDetails> => {
  const response = await api.get("/user");
  return response.data;
};

export const updateUserDetailsApi = async (
  data: Partial<UpdateProfileForm>
): Promise<UserDetails> => {
  const response = await api.patch("/user", data);
  return response.data;
};

export const updatePasswordApi = async (data: UpdatePasswordForm) => {
  const response = await api.put("/user/password", data);
  return response.data;
};