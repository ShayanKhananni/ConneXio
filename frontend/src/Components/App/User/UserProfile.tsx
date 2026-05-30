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

import { Mail, Phone, User } from "lucide-react";
import { FaFacebookSquare, FaInstagram, FaLinkedin } from "react-icons/fa";

import { useEffect } from "react";
import { useForm, type SubmitHandler } from "react-hook-form";

import {
  updateProfileSchema,
  type UpdateProfileForm,
} from "@/zod/userProfileSchema";

import { zodResolver } from "@hookform/resolvers/zod";
import { useUpdateUser } from "@/hooks/user/users";

type UserProfileProps = {
  open: boolean;
  setActiveDialog: (open: any) => void;
  title: string;
  defaultValues: UpdateProfileForm;
};

const inputClass = "h-5 placeholder:text-[12px] leading-none px-1";
const iconClass = "h-4 w-4 text-gray-400";
const labelClass = "text-[14px]";
const errorClass = "text-[12px] text-red-500 leading-none";

const UserProfile = ({
  open,
  setActiveDialog,
  title,
  defaultValues,
}: UserProfileProps) => {
  const {
    register,
    handleSubmit,
    setError,
    reset,
    formState: { errors, isDirty, dirtyFields },
  } = useForm<UpdateProfileForm>({
    defaultValues,
    resolver: zodResolver(updateProfileSchema),
  });

  const { mutate: updateUser, isPending } = useUpdateUser();

  useEffect(() => {
    reset(defaultValues);
  }, [defaultValues, reset]);

  const getInitials = (name: string) => {
    return name
      .split(" ")
      .map((n) => n[0])
      .slice(0, 2)
      .join("")
      .toUpperCase();
  };

  const handleUpdateProfile: SubmitHandler<UpdateProfileForm> = (data) => {
    const updatedFields = Object.keys(dirtyFields).reduce((acc, key) => {
      acc[key as keyof UpdateProfileForm] =
        data[key as keyof UpdateProfileForm];
      return acc;
    }, {} as Partial<UpdateProfileForm>);

    updateUser(updatedFields, {
      onSuccess: (updated) => {
        reset(updated);
        setActiveDialog(null);
      },

      onError: (error: any) => {
        setError("root", {
          type: "server",
          message:
            error?.response?.data?.message || "Something went wrong",
        });
      },
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
          <div className="flex items-center gap-3">
            <div className="w-14 h-14 rounded-full overflow-hidden bg-gray-200 flex items-center justify-center text-sm font-semibold text-gray-700">
              {defaultValues.profileImageUrl ? (
                <img
                  src={defaultValues.profileImageUrl}
                  alt="Profile"
                  className="w-full h-full object-cover"
                />
              ) : (
                getInitials(defaultValues.username || "User")
              )}
            </div>

            <DialogTitle className="text-sm font-semibold">
              {title}
            </DialogTitle>
          </div>
        </DialogHeader>

        <form
          onSubmit={handleSubmit(handleUpdateProfile)}
          className="space-y-2"
        >
          {/* USERNAME */}
          <Field>
            <FieldLabel className={labelClass}>Username</FieldLabel>
            <InputGroup>
              <InputGroupAddon>
                <User className={iconClass} />
              </InputGroupAddon>
              <InputGroupInput
                placeholder="Enter username"
                className={inputClass}
                {...register("username")}
              />
            </InputGroup>
            {errors.username && (
              <p className={errorClass}>{errors.username.message}</p>
            )}
          </Field>

          {/* EMAIL + PHONE */}
          <div className="grid sm:grid-cols-2 grid-cols-1 gap-2">
            <Field>
              <FieldLabel className={labelClass}>Email</FieldLabel>
              <InputGroup>
                <InputGroupAddon>
                  <Mail className={iconClass} />
                </InputGroupAddon>
                <InputGroupInput
                  placeholder="Enter email"
                  className={inputClass}
                  {...register("email")}
                />
              </InputGroup>
              {errors.email && (
                <p className={errorClass}>{errors.email.message}</p>
              )}
            </Field>

            <Field>
              <FieldLabel className={labelClass}>Phone</FieldLabel>
              <InputGroup>
                <InputGroupAddon>
                  <Phone className={iconClass} />
                </InputGroupAddon>
                <InputGroupInput
                  placeholder="Enter phone"
                  className={inputClass}
                  {...register("phone")}
                />
              </InputGroup>
              {errors.phone && (
                <p className={errorClass}>{errors.phone.message}</p>
              )}
            </Field>
          </div>

          {/* SOCIAL LINKS */}
          <div className="grid sm:grid-cols-2 grid-cols-1 gap-2">
            <Field>
              <FieldLabel className={labelClass}>LinkedIn</FieldLabel>
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
                <p className={errorClass}>{errors.linkedinUrl.message}</p>
              )}
            </Field>

            <Field>
              <FieldLabel className={labelClass}>Instagram</FieldLabel>
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
                <p className={errorClass}>{errors.instagramUrl.message}</p>
              )}
            </Field>

            <Field className="sm:col-span-2">
              <FieldLabel className={labelClass}>Facebook</FieldLabel>
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
                <p className={errorClass}>{errors.facebookUrl.message}</p>
              )}
            </Field>
          </div>

          {/* ROOT ERROR (FIXED) */}
          {errors.root && (
            <div className="bg-red-50 border border-red-200 text-red-600 text-[12px] px-2 py-1 rounded">
              {errors.root.message}
            </div>
          )}

          {/* FOOTER */}
          <div className="flex justify-end gap-2 pt-2">
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
      </DialogContent>
    </Dialog>
  );
};

export default UserProfile;