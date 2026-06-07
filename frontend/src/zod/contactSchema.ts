import { z } from "zod";
import { phoneUtil, urlField } from "./schemaUtils";

export const emailSchema = z.object({
  id: z.number().optional(),
  email: z.string().email("Invalid email").optional().or(z.literal("")),
  label: z.enum(["HOME", "WORK"]).optional(),
});

export const createEmailSchema = z.object({
  id: z.number().optional(),
  email: z.string().email("Invalid email").optional().or(z.literal("")),
  label: z.enum(["HOME", "WORK"]).optional(),
});

export const phoneSchema = z.object({
  id: z.number().optional(),
  phone: phoneUtil.optional().or(z.literal("")),
  label: z.enum(["HOME", "WORK"]).optional(),
});

export const createContactSchema = z.object({
  firstName: z
  .string()
  .nonempty("Firstname is required")
  .min(3, "At 3 Least Characters are required")
  .max(20, "Max 10 cahracters are allowed"),
lastName: z
  .string()
  .nonempty("Lastname is required")
  .min(3, "At 3 Least Characters are required")
  .max(11, "Max 10 cahracters are allowed"),


  linkedinUrl: urlField,
  instagramUrl: urlField,
  facebookUrl: urlField,
  profileImageUrl: urlField,

  emails: z.array(emailSchema),
  phones: z.array(phoneSchema),
});

export const updateContactSchema = z.object({
  firstName: z
    .string()
    .nonempty("Firstname is required")
    .min(3, "At 3 Least Characters are required")
    .max(20, "Max 10 cahracters are allowed"),
  lastName: z
    .string()
    .nonempty("Lastname is required")
    .min(3, "At 3 Least Characters are required")
    .max(11, "Max 10 cahracters are allowed"),

  linkedinUrl: urlField,
  instagramUrl: urlField,
  facebookUrl: urlField,
  profileImageUrl: urlField,

  emailUpdates: z.array(emailSchema).optional(),
  phoneUpdates: z.array(phoneSchema).optional(),
});

export type CreateContactForm = z.infer<typeof createContactSchema>;
export type UpdateContactForm = z.infer<typeof updateContactSchema>;
