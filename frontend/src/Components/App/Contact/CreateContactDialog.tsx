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

import { useForm, useFieldArray, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import {
  createContactSchema,
  type CreateContactForm,
} from "@/zod/contactSchema";

import { useAddContact } from "@/hooks/contact/useContact";


type Props = {
  open: boolean;
  setActiveDialog: (open: any) => void;
  title: string;
};

const inputClass = "h-5 text-[8px] placeholder:text-[12px] leading-none px-1";
const iconClass = "h-4 w-4 text-gray-400";
const errorClass = "text-[12px] text-red-500 leading-none";

const CreateContactDialog = ({ open, setActiveDialog, title }: Props) => {



  const {
    mutate: addContact,
    isPending,
    isSuccess,
  } = useAddContact();


  const {
    register,
    handleSubmit,
    control,
    setError,
    formState: { errors, isDirty },
  } = useForm<CreateContactForm>({
    resolver: zodResolver(createContactSchema),
    defaultValues: {
      emails: [
        { email: "", label: "HOME" },
        { email: "", label: "WORK" },
      ],
      phones: [
        { phone: "", label: "HOME" },
        { phone: "", label: "WORK" },
      ],
    },
  });

  const { fields: emailFields } = useFieldArray({
    control,
    name: "emails",
  });

  const { fields: phoneFields } = useFieldArray({
    control,
    name: "phones",
  });


  const getValidEmails = (data: CreateContactForm) =>
    data.emails?.filter((e) => e.email?.trim() !== "") || [];

  const getValidPhones = (data: CreateContactForm) =>
    data.phones?.filter((e) => e.phone?.trim() !== "") || [];

  

  const checkDuplicates = (data: any, type: string) => {
    const filteredData = data
      .map((e: any) => e[type])
      .filter(Boolean);
  
    return new Set(filteredData).size !== filteredData.length;
  };




  const onSubmit: SubmitHandler<CreateContactForm> = async (data) => {

    const emails = getValidEmails(data);
    const phones = getValidPhones(data);
  
  
    if (!emails.length) {
      setError("emails", { message: "At least one email is required" });
      return;
    }
  
    if (!phones.length) {
      setError("phones", { message: "At least one phone is required" });
      return;
    }
  
    if (checkDuplicates(emails,'email')) {
      setError("emails", { message: "Duplicate emails not allowed" });
      return;
    }
  
    if (checkDuplicates(phones,'phone')) {
      setError("phones", { message: "Duplicate phones not allowed" });
      return;
    }
  
    let payload = Object.fromEntries(
      Object.entries(data).filter(
        ([_, value]) =>
          value !== "" &&
          typeof value !== "object"
      )
    );
    payload = {...payload, emails, phones}
    addContact(payload);
    
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
          <DialogTitle className="text-sm font-semibold">
            {title}
          </DialogTitle>
        </DialogHeader>

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-2">


          <div className="grid grid-cols-2 gap-2">
            <Field>
              <FieldLabel>First Name</FieldLabel>
              <InputGroup>
                <InputGroupAddon>
                  <User className={iconClass} />
                </InputGroupAddon>
                <InputGroupInput
                  placeholder="John"
                  className={inputClass}
                  {...register("firstName")}
                />
              </InputGroup>
              {errors.firstName && (
                <p className={errorClass}>
                  {errors.firstName.message}
                </p>
              )}
            </Field>

            <Field>
              <FieldLabel>Last Name</FieldLabel>
              <InputGroup>
                <InputGroupAddon>
                  <User className={iconClass} />
                </InputGroupAddon>
                <InputGroupInput
                  placeholder="Doe"
                  className={inputClass}
                  {...register("lastName")}
                />
              </InputGroup>
              {errors.lastName && (
                <p className={errorClass}>
                  {errors.lastName.message}
                </p>
              )}
            </Field>
          </div>

          {/* EMAILS */}
          <div className="grid grid-cols-1 gap-2">
            {emailFields.map((field, index) => (
              <Field key={field.id}>
                <FieldLabel>Email {index + 1}</FieldLabel>

                <div className="flex gap-1">
                  <select
                    className="border rounded px-1 text-[12px] w-[80px]"
                    {...register(`emails.${index}.label`)}
                  >
                    <option value="HOME">Home</option>
                    <option value="WORK">Work</option>
                  </select>

                  <InputGroup>
                    <InputGroupAddon>
                      <Mail className={iconClass} />
                    </InputGroupAddon>

                    <InputGroupInput
                      placeholder="Email"
                      className={inputClass}
                      {...register(`emails.${index}.email`)}
                    />
                  </InputGroup>
                </div>

                {errors.emails?.[index]?.email && (
                    <p className={errorClass}>
                      {errors.emails[index]?.email?.message}
                    </p>
                  )}

                  {errors.emails?.message && (
                    <div className="col-span-2">
                      <p className={errorClass}>
                        {errors.emails.message}
                      </p>
                    </div>
                  )}


                
              </Field>
            ))}
          </div>


          {/* PHONES */}
          <div className="grid grid-cols-1 gap-2">
            {phoneFields.map((field, index) => (
              <Field key={field.id}>
                <FieldLabel>Phone {index + 1}</FieldLabel>

                <div className="flex gap-1">
                  <select
                    className="border rounded px-1 text-[12px] w-[80px]"
                    {...register(`phones.${index}.label`)}
                  >
                    <option value="HOME">Home</option>
                    <option value="WORK">Work</option>
                  </select>

                  <InputGroup>
                    <InputGroupAddon>
                      <Phone className={iconClass} />
                    </InputGroupAddon>

                    <InputGroupInput
                      placeholder="Phone"
                      className={inputClass}
                      {...register(`phones.${index}.phone`)}
                    />
                  </InputGroup>
                </div>
                

                
                {errors.phones?.[index]?.phone && (
                    <p className={errorClass}>
                      {errors.phones[index]?.phone?.message}
                    </p>
                  )}

                  {errors.phones?.message && (
                    <div className="col-span-2">
                      <p className={errorClass}>
                        {errors.phones.message}
                      </p>
                    </div>
                  )}

              </Field>
            ))}
          </div>


          {/* SOCIALS */}
          <div className="grid grid-cols-2 gap-2">
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
            </Field>
          </div>

          {/* FOOTER */}
          <div className="flex justify-end gap-2 pt-2">
            <Button
              type="button"
              variant="outline"
              onClick={() => setActiveDialog(null)}
            >
              Cancel
            </Button>

            <Button
              type="submit"
              disabled={!isDirty || isPending}
            >
              {isPending ? "Creating..." : "Create"}
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default CreateContactDialog;