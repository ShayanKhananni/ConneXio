import { z } from "zod";
import {urlField} from "./schemaUtils"
import { userNameUtil } from "./authSchema";

export const updateProfileSchema = z.object({

  username: userNameUtil,
  linkedinUrl: urlField,
  instagramUrl: urlField,
  facebookUrl: urlField,
  profileImageUrl: urlField,
  email: z.string().nonempty("Email is required")
  .email("Invalid email"),
  phone: z.string()
  .nonempty()
  .regex(
    /^(\+92|92|0)3\d{9}$/,
    "Phone must be a valid Pakistani number")
});


export type UpdateProfileForm = z.infer<typeof updateProfileSchema>


