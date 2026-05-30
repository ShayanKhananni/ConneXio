import { z } from "zod";

export const urlField = z
  .string()
  .trim()
  .url("Invalid URL")
  .optional()
  .or(z.literal(""));


export const phoneUtil = z
  .string()
  .regex(
    /^(\+92|92|0)3\d{9}$/,
    "Phone must be a valid Pakistani number"
  );

  
  export const passwordUtil = z
  .string()
  .nonempty("Password is requried")
  .min(8, "Password must be at least 8 characters")
  .max(10, "Password must be at most 10 characters")
  .regex(
    /^(?=.*[0-9])(?=.*[!@#$%^&*(),.?":{}|<>]).{8,10}$/,
    "Password must contain at least 1 number and 1 special character"
  )

