import { z } from "zod";
import { passwordUtil } from "./schemaUtils";

export const updatePasswordSchema = z.object({
  oldPassword: passwordUtil,
  newPassword: passwordUtil
}).refine((data) => data.oldPassword !== data.newPassword, {
  path: ["newPassword"],
  message: "New password must be different from old password",
});



export type UpdatePasswordForm = z.infer<typeof updatePasswordSchema>