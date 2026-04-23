// loginSchema.ts
import { z } from "zod";


export const loginSchema = z.object({
  email: z.string().email("Enter a valid email"),
  password: z
    .string()
    .min(6, "Password must be at least 6 characters")
    .max(100, "Password is too long"),
});

export type LoginSchema = z.infer<typeof loginSchema>;