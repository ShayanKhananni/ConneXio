import { z } from "zod";
import {passwordUtil, phoneUtil } from "./schemaUtils";


export const userNameUtil = z
  .string()
  .nonempty("Username is required")
  .min(3, "Name must be at least 3 characters")
  .max(10, "Name must be at most 10 characters");

export const loginSchema = z.object({
  username: userNameUtil,
  password: passwordUtil,
});

export const registerSchema = z.object({
  username: userNameUtil,

  email: z.string().nonempty("Email is required")
    .email("Invalid email"),
    phone: z.string()
    .nonempty("Phone is required")
    .regex(
      /^(\+92|92|0)3\d{9}$/,
      "Phone must be a valid Pakistani number"),

  password: passwordUtil,
});


export type LoginCredentials = z.infer<typeof loginSchema>;
export type SignupCredentials = z.infer<typeof registerSchema>;