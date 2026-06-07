import { z } from "zod";

const LabelSchema = z.preprocess(
  (val) => {
    if (typeof val !== "string") return val;
    const cleaned = val.trim();
    return cleaned === "" ? undefined : cleaned;
  },
  z.enum(["WORK", "HOME"]).optional()
);

export const csvRowSchema = z
  .object({
    firstName: z.string().min(1, "First name is required"),
    lastName: z.string().min(1, "Last name is required"),

    email_1: z
      .string()
      .nonempty("Email is required")
      .email("Must be a valid email"),

    email_label_1: LabelSchema,

    phone_1: z
      .string()
      .nonempty("Phone is required")
      .regex(
        /^(\+92|92|0)3\d{9}$/,
        "Phone must be a valid Pakistani number"
      ),

    phone_label_1: LabelSchema,

    email_2: z.string().optional(),
    email_label_2: LabelSchema.optional(),

    phone_2: z
  .string()
  .transform((val) => val.trim())
  .refine((val) => val === "" || /^(\+92|92|0)3\d{9}$/.test(val), {
    message: "Phone must be a valid Pakistani number",
  })
  .optional(),

    phone_label_2: LabelSchema.optional(),
  })
  .superRefine((data, ctx) => {
    if (data.email_2 && !data.email_label_2) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        path: ["email_label_2"],
        message: "email_label_2 is required when email_2 is present",
      });
    }

    if (data.phone_2 && !data.phone_label_2) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        path: ["phone_label_2"],
        message: "phone_label_2 is required when phone_2 is present",
      });
    }
  });