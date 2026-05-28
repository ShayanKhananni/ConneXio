import { zodResolver } from "@hookform/resolvers/zod";
import { Eye, EyeOff, Lock, Mail, Phone, User } from "lucide-react";
import { useState } from "react";
import { useForm, type SubmitHandler } from "react-hook-form";
import { z } from "zod";

import { Button } from "../Components/ui/button";
import { Field, FieldError, FieldLabel } from "../Components/ui/field";
import { InputGroup, InputGroupAddon, InputGroupInput } from "../Components/ui/input-group";
import { registerApi } from "@/api/authApi";
import { useMutation } from "@tanstack/react-query";
import { useAuthStore } from "../store/authStore";
import { useNavigate } from "react-router-dom";
import { useSignup } from "@/hooks/auth/useAuth";


const SignupPage = () => {

  const [showPassword, setShowPassword] = useState(false);
  const { setToken } = useAuthStore();
  const navigate = useNavigate();


  type FormFields = z.infer<typeof schema>;
  
const schema = z.object({
  username: z
    .string()
    .nonempty("Username is required")
    .min(5, "Username must be at least 5 characters"),
  email: z         
  .string()            
    .nonempty("Email is required")
    .email("Invalid email address"), 

  phone: z
    .string()
    .nonempty("Phone is required")
    .min(10, "Phone must be at least 10 digits")
    .regex(/^\+?[0-9]+$/, "Invalid phone number"),
  password: z
    .string()
    .nonempty("Password is required")
    .min(8, "Password must be at least 8 characters"),
});


 useForm<FormFields>({
  resolver: zodResolver(schema),
});

  const {
    register,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm<FormFields>({
    defaultValues: { username: "", phone: "", password: "" },
    resolver: zodResolver(schema),
  });

  const { mutate: signup, isPending } = useSignup(setError);

  const handleOnSignup: SubmitHandler<FormFields> = (data) => {
    signup(data);
  };


  
  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-purple-100 via-white to-violet-200 px-4">
      <div className="w-full max-w-md bg-white shadow-2xl rounded-2xl p-8 border border-purple-100">
        <div className="mb-8 text-center">
          <h1 className="text-3xl font-bold text-purple-700">ConneXio</h1>
          <p className="text-gray-500 mt-2">Create your account</p>
        </div>

        <form onSubmit={handleSubmit(handleOnSignup)} className="space-y-5">

          <Field data-invalid={!!errors.username}>
            <FieldLabel>Username</FieldLabel>
            <InputGroup>
              <InputGroupAddon>
                <User className="h-4 w-4 text-gray-400" />
              </InputGroupAddon>
              <InputGroupInput
                {...register("username")}
                placeholder="Enter username"
              />
            </InputGroup>
            {errors.username && <FieldError errors={[errors.username]} />}
          </Field>

          <Field data-invalid={!!errors.email}>
            <FieldLabel>Email</FieldLabel>
            <InputGroup>
              <InputGroupAddon>
                <Mail className="h-4 w-4 text-gray-400" />
              </InputGroupAddon>
              <InputGroupInput
                {...register("email")}
                placeholder="Enter email"
              />
            </InputGroup>
            {errors.email && <FieldError errors={[errors.email]} />}
          </Field>


          <Field data-invalid={!!errors.phone}>
            <FieldLabel>Phone</FieldLabel>
            <InputGroup>
              <InputGroupAddon>
                <Phone className="h-4 w-4 text-gray-400" />
              </InputGroupAddon>
              <InputGroupInput
                {...register("phone")}
                placeholder="Enter phone number"
              />
            </InputGroup>
            {errors.phone && <FieldError errors={[errors.phone]} />}
          </Field>

          <Field data-invalid={!!errors.password}>
            <FieldLabel>Password</FieldLabel>
            <InputGroup>
              <InputGroupAddon>
                <Lock className="h-4 w-4 text-gray-400" />
              </InputGroupAddon>
              <InputGroupInput
                {...register("password")}
                type={showPassword ? "text" : "password"}
                placeholder="Enter password"
              />
              <button
                type="button"
                onClick={() => setShowPassword((p) => !p)}
                className="text-gray-400 hover:text-purple-600 me-5 transition-colors"
              >
                {showPassword ? (
                  <EyeOff className="h-4 w-4" />
                ) : (
                  <Eye className="h-4 w-4" />
                )}
              </button>
            </InputGroup>
            {errors.password && <FieldError errors={[errors.password]} />}
          </Field>

          <Button
            type="submit"
            disabled={isPending}
            className="w-full bg-purple-600 hover:bg-purple-700 transition-colors duration-200 text-white font-semibold py-3 rounded-xl shadow-lg disabled:opacity-70"
          >
            {isPending ? "Creating account..." : "Sign Up"}
          </Button>

          {errors.root && (
            <p className="text-red-500 text-sm text-center">
              {errors.root.message}
            </p>
          )}
        </form>
      </div>
    </div>
  );
};

export default SignupPage;