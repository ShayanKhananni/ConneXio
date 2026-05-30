import { zodResolver } from "@hookform/resolvers/zod";
import { Eye, EyeOff, Lock, Mail, Phone, User } from "lucide-react";
import { useState } from "react";
import { useForm, type SubmitHandler } from "react-hook-form";
import { z } from "zod";

import { Button } from "../Components/ui/button";
import { Field, FieldError, FieldLabel } from "../Components/ui/field";
import {
  InputGroup,
  InputGroupAddon,
  InputGroupInput,
} from "../Components/ui/input-group";

import { Link } from "react-router-dom";
import { useSignup } from "@/hooks/auth/useAuth";
import { registerSchema, type SignupCredentials } from "@/zod/authSchema";

const SignupPage = () => {
  const [showPassword, setShowPassword] = useState(false);

  type FormFields = z.infer<typeof registerSchema>;

  useForm<SignupCredentials>({
    resolver: zodResolver(registerSchema),
  });

  const {
    register,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm<FormFields>({
    defaultValues: { username: "", phone: "", password: "" },
    resolver: zodResolver(registerSchema),
  });

  const { mutate: signup, isPending } = useSignup(setError);

  const handleOnSignup: SubmitHandler<SignupCredentials> = (data) => {
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
          <Field>
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

          <Field>
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

          <Field>
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

          <Field>
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

          {/* Signup link */}
          <p className="text-center text-sm text-gray-500 mt-4">
            Already have an account?{" "}
            <Link
              to="/login"
              className="text-purple-600 hover:underline font-medium"
            >
              Login
            </Link>
          </p>

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
