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

import { Lock, Eye, EyeOff } from "lucide-react";

import { useState } from "react";
import { useForm, type SubmitHandler } from "react-hook-form";
import {
  updatePasswordSchema,
  type UpdatePasswordForm,
} from "@/zod/changePasswordSchema";
import { zodResolver } from "@hookform/resolvers/zod";
import { useUpdatePassword } from "@/hooks/user/users";

type ChangePasswordDialogProps = {
  open: boolean;
  setActiveDialog: (open: any) => void;
  title: string;
};

const inputClass = "h-5 placeholder:text-[12px] leading-none px-1";
const iconClass = "h-4 w-4 text-gray-400";
const labelClass = "text-[14px]";
const errorClass = "text-[12px] text-red-500 leading-none";

const ChangePasswordDialog = ({
  open,
  setActiveDialog,
  title,
}: ChangePasswordDialogProps) => {
  const [showOldPassword, setShowOldPassword] = useState(false);
  const [showNewPassword, setShowNewPassword] = useState(false);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);



  const {
    register,
    handleSubmit,
    setError, 
    clearErrors,
    formState: { errors, isDirty },
    reset,
  } = useForm<UpdatePasswordForm>({
    resolver: zodResolver(updatePasswordSchema),
    defaultValues: {
      oldPassword: "",
      newPassword: "",
    },
  });

  const {
    mutate,
    isPending: isUpdating,
  } = useUpdatePassword();


  const handleChangePassword: SubmitHandler<UpdatePasswordForm> = (formData) => {
    clearErrors("root");
    setSuccessMessage(null);

    mutate(formData, {
      onSuccess: (res: any) => {
        setSuccessMessage(res?.message || "Password updated successfully");
        reset();

        setTimeout(() => {
          setActiveDialog(false);
          setSuccessMessage(null)
        }, 2000);
      },


      onError: (err: any) => {
        const message =
          err?.response?.data?.message ||
          err?.message ||
          "Something went wrong";

        setError("root", {
          type: "server",
          message,
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
      <DialogContent className="sm:max-w-md p-4 select-none">
        <DialogHeader className="mb-2">
          <DialogTitle className="text-sm font-semibold">
            {title}
          </DialogTitle>
        </DialogHeader>

        <form
          onSubmit={handleSubmit(handleChangePassword)}
          className="space-y-3"
        >
          {/* OLD PASSWORD */}
          <Field>
            <FieldLabel className={labelClass}>
              Old Password
            </FieldLabel>

            <InputGroup>
              <InputGroupAddon>
                <Lock className={iconClass} />
              </InputGroupAddon>

              <InputGroupInput
                type={showOldPassword ? "text" : "password"}
                placeholder="Enter old password"
                className={inputClass}
                {...register("oldPassword")}
              />

              <button
                type="button"
                onClick={() => setShowOldPassword((p) => !p)}
                className="text-gray-400 hover:text-purple-600 me-5"
              >
                {showOldPassword ? <EyeOff /> : <Eye />}
              </button>
            </InputGroup>

            {errors.oldPassword && (
              <p className={errorClass}>
                {errors.oldPassword.message}
              </p>
            )}
          </Field>

          {/* NEW PASSWORD */}
          <Field>
            <FieldLabel className={labelClass}>
              New Password
            </FieldLabel>

            <InputGroup>
              <InputGroupAddon>
                <Lock className={iconClass} />
              </InputGroupAddon>

              <InputGroupInput
                type={showNewPassword ? "text" : "password"}
                placeholder="Enter new password"
                className={inputClass}
                {...register("newPassword")}
              />

              <button
                type="button"
                onClick={() => setShowNewPassword((p) => !p)}
                className="text-gray-400 hover:text-purple-600 me-5"
              >
                {showNewPassword ? <EyeOff /> : <Eye />}
              </button>
            </InputGroup>

            {errors.newPassword && (
              <p className={errorClass}>
                {errors.newPassword.message}
              </p>
            )}
          </Field>

          {/* ✅ ROOT ERROR MESSAGE (RHF) */}
          {errors.root && (
            <p className="text-red-600 text-xs font-medium">
              {errors.root.message}
            </p>
          )}

          {/* ✅ SUCCESS MESSAGE STATE */}
          {successMessage && (
            <p className="text-green-600 text-xs font-medium">
              {successMessage}
            </p>
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
              disabled={!isDirty || isUpdating}
              type="submit"
              className="px-3 py-3 bg-green-600 text-white text-xs font-bold"
            >
              {isUpdating ? "Updating..." : "Update"}
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default ChangePasswordDialog;