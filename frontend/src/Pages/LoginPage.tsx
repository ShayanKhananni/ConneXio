import { zodResolver } from "@hookform/resolvers/zod";
import { Eye, EyeOff, Lock, User } from "lucide-react";
import { useState } from "react";
import { useForm, type SubmitHandler } from "react-hook-form";

import { Button } from "../Components/ui/button";
import { Field, FieldError, FieldLabel } from "../Components/ui/field";
import { InputGroup, InputGroupAddon, InputGroupInput } from "../Components/ui/input-group";
import { loginSchema, type LoginCredentials } from "@/types/authTypes";
import { useLogin } from "@/hooks/auth/useAuth";


const schema = loginSchema;
type FormFields = LoginCredentials;  

const LoginPage = () => {

  const [showPassword, setShowPassword] = useState(false);
  
  const {
    register,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm<FormFields>({
    defaultValues: { username: "", password: "" },
    resolver: zodResolver(schema),
  });
  
  const { mutate: login, isPending } = useLogin(setError)  
  const handleOnLogin: SubmitHandler<FormFields> = (data) => {
    login(data); 
  };

  
  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-purple-100 via-white to-violet-200 px-4">
      <div className="w-full max-w-md bg-white shadow-2xl rounded-2xl p-8 border border-purple-100">

        <div className="mb-8 text-center">
          <h1 className="text-3xl font-bold text-purple-700">ConneXio</h1>
          <p className="text-gray-500 mt-2">Login to your account</p>
        </div>

        <form onSubmit={handleSubmit(handleOnLogin)} className="space-y-5">


          <Field>
            <FieldLabel>Username</FieldLabel>
            <InputGroup>
              <InputGroupAddon>
                <User className="h-4 w-4 text-gray-400" />
              </InputGroupAddon>
              <InputGroupInput {...register("username")} placeholder="Enter username" />
            </InputGroup>
            {errors.username && <FieldError errors={[errors.username]} />}
          </Field>


          {/* Password */}
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
                  {showPassword ? <EyeOff className="h-4 w-4" /> : <Eye className="h-4 w-4" />}
                </button>
            </InputGroup>
            {errors.password && <FieldError errors={[errors.password]} />}
          </Field>

          {/* Submit */}
          <Button
            disabled={isPending}  // ← TanStack pending state
            className="w-full bg-purple-600 hover:bg-purple-700 transition-colors duration-200 text-white font-semibold py-3 rounded-xl shadow-lg disabled:opacity-70"
          >
            {isPending ? "Logging in..." : "Login"}
          </Button>

          {/* Server error from onError → setError("root") */}
          {errors.root && (
            <p className="text-red-500 text-sm text-center">{errors.root.message}</p>
          )}

        </form>
      </div>
    </div>
  );
};

export default LoginPage;