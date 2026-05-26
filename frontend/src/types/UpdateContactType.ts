import { z } from "zod";

export const urlField = z
  .string()
  .trim()
  .url("Invalid URL")
  .optional()
  .or(z.literal(""));


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
    phone: z.string().min(5, "Invalid phone number").optional().or(z.literal("")),
    label: z.enum(["HOME", "WORK"]).optional(),
  });

  export const createContactSchema = z.object({
    firstName: z.string().nonempty("Firsname is required").min(2, "Too short").max(50, "Too long"),
    lastName: z.string().nonempty("Lastname is required").min(2, "Too short").max(50, "Too long"),

  
    linkedinUrl: urlField,
    instagramUrl: urlField,
    facebookUrl: urlField,
    profileImageUrl: urlField,
  
    emails: z.array(emailSchema),
    phones: z.array(phoneSchema)
  });


  export const updateContactSchema = z.object({
    firstName: z.string().min(2, "Too short").max(50, "Too long").optional(),
    lastName: z.string().min(2, "Too short").max(50, "Too long").optional(),
  
    linkedinUrl: urlField,
    instagramUrl: urlField,
    facebookUrl: urlField,
    profileImageUrl: urlField,
  
    emailUpdates: z.array(emailSchema).optional(),
    phoneUpdates: z.array(phoneSchema).optional(),
  });


  export type CreateContactForm = z.infer<typeof createContactSchema>
  export type UpdateContactForm = z.infer<typeof updateContactSchema>


