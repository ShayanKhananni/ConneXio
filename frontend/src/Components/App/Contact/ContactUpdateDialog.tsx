import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/Components/ui/dialog";

import { Button } from "@/Components/ui/button";
import { Field, FieldLabel } from "@/Components/ui/field";
import {
  InputGroup,
  InputGroupAddon,
  InputGroupInput,
} from "@/Components/ui/input-group";

import { User, Mail, Phone } from "lucide-react";
import { FaFacebookSquare, FaInstagram, FaLinkedin } from "react-icons/fa";

import { useEffect } from "react";
import { useForm, useFieldArray, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { type Contact } from "@/types/contactTypes";


import {
  updateContactSchema,
  type UpdateContactForm 
} from "@/zod/contactSchema";


import { useUpdateContact } from "@/hooks/contact/useContact";


type ContactUpdateDialogProps = {
  contact: Contact | null;
  open: boolean;
  setActiveDialog: (open: any) => void;
  title: string;
};

const inputClass = "h-5  placeholder:text-[12px] leading-none px-1";
const iconClass = "h-4 w-4 text-gray-400";
const labelClass = "text-[14px]";
const errorClass = "text-[12px] text-red-500 leading-none";


const ContactUpdateDialog = ({
  contact,
  open,
  setActiveDialog,
  title,
}: ContactUpdateDialogProps) => {
  const {
    register,
    handleSubmit,
    reset,
    control,
    setError,
    formState: { errors, dirtyFields, isDirty },
  } = useForm<UpdateContactForm>({
    resolver: zodResolver(updateContactSchema),
    defaultValues: {
      firstName: "",
      lastName: "",
      linkedinUrl: "",
      instagramUrl: "",
      facebookUrl: "",
      profileImageUrl: "",
      emailUpdates: [],
      phoneUpdates: [],
    },
  });


  // Mutatation Function for updating Contact
  const {
    mutate: updateContact,
    isPending,
    isSuccess,
  } = useUpdateContact();

  
  // Method to prepare exactly 2 inputs for phone and email  
  const normalizeFields = (fields: any[] = [], key: "email" | "phone") => {
    const result = [];
    for (let i = 0; i < 2; i++) {
      result.push(
        fields[i]
          ? {
              id: fields[i].id,
              [key]: fields[i][key],
              label: fields[i]["label"],
            }
          : {
              [key]: "",
              label: "HOME",
            }
      );
    }
    return result;
  };


  // Loading contact data into form when contact changes or dialog opens, and also close dialog on successful update

  
  useEffect(() => {
    if (!contact) return;
    reset({
      firstName: contact.firstName ?? "",
      lastName: contact.lastName ?? "",
      linkedinUrl: contact.linkedinUrl ?? "",
      instagramUrl: contact.instagramUrl ?? "",
      facebookUrl: contact.facebookUrl ?? "",
      profileImageUrl: contact.profileImageUrl ?? "",
      emailUpdates: normalizeFields(contact.emails, "email"),
      phoneUpdates: normalizeFields(contact.phones, "phone"),
    });

    if (isSuccess) {
      setActiveDialog(null);
      reset();
    }

  }, [contact, reset, isSuccess, setActiveDialog]);



    // getting the feild array to itterate over fields in the form 
    const { fields: emailFields } = useFieldArray({
      control,
      name: "emailUpdates",
    });
  
    const { fields: phoneFields } = useFieldArray({
      control,
      name: "phoneUpdates",
    });
  


  /// Utility function to construct the update payload by comparing dirty fields with form data, and only including changed fields in the payload. This ensures that we only send updated data to the server.
  function getUpdatedData(dirtyFields: any, data: any) {
    const result: any = {};

    for (const key of Object.keys(dirtyFields)) {
      const value = dirtyFields[key];

      if (value !== true && typeof value === "object") {
        result[key] = [];

        for (const i in value) {
          const index = Number(i);

          if (value[i] !== undefined && data[key]?.[index] !== undefined) {
            result[key].push(data[key][index]);
          }
        }

        continue;
      }

      result[key] = data[key];
    }

    return result;
  }


  /// Checking Duplicate Emails and Phones in the form before submitting, and setting form errors if duplicates are found
  const checkDuplicates = (data: UpdateContactForm) => {
    const emails =
      data.emailUpdates?.map((e) => e.email).filter(Boolean) || [];
    const phones =
      data.phoneUpdates?.map((p) => p.phone).filter(Boolean) || [];

    return {
      hasDuplicateEmails: new Set(emails).size !== emails.length,
      hasDuplicatePhones: new Set(phones).size !== phones.length,
    };
  };




  const getValidEmails = (data: UpdateContactForm) =>
    data.emailUpdates?.filter((e) => e.email?.trim() !== "") || [];
  
  const getValidPhones = (data: UpdateContactForm) =>
    data.phoneUpdates?.filter((e) => e.phone?.trim() !== "") || [];
  
  const handleUpdateContact: SubmitHandler<UpdateContactForm> = (data) => {
    const { hasDuplicateEmails, hasDuplicatePhones } = checkDuplicates(data);
  
    if (hasDuplicateEmails) {
      setError("emailUpdates", {
        type: "manual",
        message: "Duplicate emails are not allowed",
      });
      return;
    }
  
    if (hasDuplicatePhones) {
      setError("phoneUpdates", {
        type: "manual",
        message: "Duplicate phone numbers are not allowed",
      });
      return;
    }
  
    let cleanedData = getUpdatedData(dirtyFields, data);
  
    cleanedData = {
      ...cleanedData,
      emailUpdates: getValidEmails(cleanedData),
      phoneUpdates: getValidPhones(cleanedData),
    };
  
    updateContact({
      id: contact?.contactId,
      data: cleanedData,
    });
  };



  return (
    <Dialog
      open={open}
      onOpenChange={(isOpen) => {
        if (!isOpen) setActiveDialog(null);
      }}
    >
      <DialogContent className="sm:max-w-xl p-4 select-none">

        <DialogHeader className="mb-2">
          <div className="flex items-center gap-2">
            <div className="w-10 h-10 rounded-full bg-gray-200 flex items-center justify-center">
              {contact?.firstName?.[0] ?? "U"}
            </div>

            <DialogTitle className="text-sm font-semibold">
              {title}
            </DialogTitle>
          </div>
        </DialogHeader>


        {!contact ? (
          <div className="text-[10px] text-muted-foreground">Loading...</div>
        ) : (

          <form
            onSubmit={handleSubmit(handleUpdateContact)}
            className="space-y-1.5"
          >
            {/* NAME */}
            <div className="grid grid-cols-2 gap-1">
              <Field>
                <FieldLabel className={labelClass}>First Name</FieldLabel>
                <InputGroup>
                  <InputGroupAddon>
                    <User className={iconClass} />
                  </InputGroupAddon>
                  <InputGroupInput
                    placeholder="e.g. John"
                    className={inputClass}
                    {...register("firstName")}
                  />
                </InputGroup>
                {errors.firstName && (
                  <p className={errorClass}>{errors.firstName.message}</p>
                )}
              </Field>

              <Field>
                <FieldLabel className={labelClass}>Last Name</FieldLabel>
                <InputGroup>
                  <InputGroupAddon>
                    <User className={iconClass} />
                  </InputGroupAddon>
                  <InputGroupInput
                    placeholder="e.g. Doe"
                    className={inputClass}
                    {...register("lastName")}
                  />
                </InputGroup>
                {errors.lastName && (
                  <p className={errorClass}>{errors.lastName.message}</p>
                )}
              </Field>
            </div>

            {/* EMAILS */}
            <div className="grid sm:grid-cols-2 grid-cols-1 gap-1">
              {emailFields.map((field, index) => (
                <Field key={field.id}>
                  <FieldLabel>Email {index + 1}</FieldLabel>

                  <div className="flex gap-1">
                    <select
                      className="border rounded px-1"
                      {...register(`emailUpdates.${index}.label`)}
                    >
                      <option value="HOME">Home</option>
                      <option value="WORK">Work</option>
                    </select>

                    <InputGroup>
                      <InputGroupAddon>
                        <Mail className={iconClass} />
                      </InputGroupAddon>

                      <InputGroupInput
                        style={{ fontSize: "12px" }}
                        placeholder={
                          index === 0 ? "Primary email" : "Secondary email"
                        }
                        className={inputClass}
                        {...register(`emailUpdates.${index}.email`)}
                      />
                    </InputGroup>
                  </div>

                  {errors.emailUpdates?.[index]?.email && (
                    <p className={errorClass}>
                      {errors.emailUpdates[index]?.email?.message}
                    </p>
                  )}

                  {errors.emailUpdates?.[index]?.label && (
                    <p className={errorClass}>
                      {errors.emailUpdates[index]?.label?.message}
                    </p>
                  )}

                  {errors.emailUpdates?.message && (
                    <div className="col-span-2">
                      <p className={errorClass}>
                        {errors.emailUpdates.message}
                      </p>
                    </div>
                  )}
                </Field>
              ))}
            </div>

            {/* PHONES */}
            <div className="grid sm:grid-cols-2 grid-cols-1 gap-1">
              {phoneFields.map((field, index) => (
                <Field key={field.id}>
                  <FieldLabel>Phone {index + 1}</FieldLabel>

                  <div className="flex gap-1">
                    <select
                      className="border rounded px-1 text-[12px] w-[80px]"
                      {...register(`phoneUpdates.${index}.label`)}
                      defaultValue={field.label || "HOME"}
                    >
                      <option value="HOME">Home</option>
                      <option value="WORK">Work</option>
                    </select>

                    <InputGroup>
                      <InputGroupAddon>
                        <Phone className={iconClass} />
                      </InputGroupAddon>

                      <InputGroupInput
                        placeholder={
                          index === 0 ? "Primary phone" : "Secondary phone"
                        }
                        className={inputClass}
                        {...register(`phoneUpdates.${index}.phone`)}
                        defaultValue={field.phone}
                      />
                    </InputGroup>
                  </div>

                  {errors.phoneUpdates?.[index]?.phone && (
                    <p className={errorClass}>
                      {errors.phoneUpdates[index]?.phone?.message}
                    </p>
                  )}

                  {errors.phoneUpdates?.[index]?.label && (
                    <p className={errorClass}>
                      {errors.phoneUpdates[index]?.label?.message}
                    </p>
                  )}

                  {errors.phoneUpdates?.message && (
                    <div className="col-span-2">
                      <p className={errorClass}>
                        {errors.phoneUpdates.message}
                      </p>
                    </div>
                  )}
                </Field>
              ))}
            </div>

            {/* SOCIALS (UNCHANGED) */}
            <div className="grid grid-cols-2 gap-1">
              <Field>
                <FieldLabel>LinkedIn</FieldLabel>
                <InputGroup>
                  <InputGroupAddon>
                    <FaLinkedin className={iconClass} />
                  </InputGroupAddon>
                  <InputGroupInput
                    placeholder="linkedin.com/in/username"
                    className={inputClass}
                    {...register("linkedinUrl")}
                  />
                </InputGroup>
                {errors.linkedinUrl && (
                  <p className="text-red-500 text-xs mt-1">
                    {errors.linkedinUrl.message}
                  </p>
                )}
              </Field>

              <Field>
                <FieldLabel>Instagram</FieldLabel>
                <InputGroup>
                  <InputGroupAddon>
                    <FaInstagram className={iconClass} />
                  </InputGroupAddon>
                  <InputGroupInput
                    placeholder="instagram.com/username"
                    className={inputClass}
                    {...register("instagramUrl")}
                  />
                </InputGroup>
                {errors.instagramUrl && (
                  <p className="text-red-500 text-xs mt-1">
                    {errors.instagramUrl.message}
                  </p>
                )}
              </Field>

              <Field>
                <FieldLabel>Facebook</FieldLabel>
                <InputGroup>
                  <InputGroupAddon>
                    <FaFacebookSquare className={iconClass} />
                  </InputGroupAddon>
                  <InputGroupInput
                    placeholder="facebook.com/username"
                    className={inputClass}
                    {...register("facebookUrl")}
                  />
                </InputGroup>
                {errors.facebookUrl && (
                  <p className="text-red-500 text-xs mt-1">
                    {errors.facebookUrl.message}
                  </p>
                )}
              </Field>
            </div>

            {/* FOOTER */}
            <div className="flex justify-end gap-1 pt-1">
              <Button
                type="button"
                variant="outline"
                className="px-3 py-3 bg-red-600 text-white text-xs font-bold"
                onClick={() => setActiveDialog(null)}
              >
                Cancel
              </Button>

              <Button
                disabled={!isDirty || isPending}
                type="submit"
                className="px-3 py-3 bg-green-600 text-white text-xs font-bold"
              >
                {isPending ? "Updating..." : "Update"}
              </Button>
            </div>
          </form>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default ContactUpdateDialog;