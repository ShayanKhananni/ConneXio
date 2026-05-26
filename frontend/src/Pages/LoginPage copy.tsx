import {
  useForm,
  type SubmitErrorHandler,
  type SubmitHandler,
} from "react-hook-form";
import z, { email } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";


const schema = z.object({
  username: z.string().nonempty("Username is required")
  .min(5, "Username must be at least 5 characters"),
  password: z
    .string()
    .nonempty("Password is required")
    .min(8, "Password must be at least 8 characters")
})


type FormFields = z.infer<typeof schema>;

// type FormFields = {
//   username: string;
//   password: string;
// }

const LoginPage = () => {
  // const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    setError,
    formState: { errors, isSubmitting },
  } = useForm<FormFields>({
    defaultValues: {
      username: "default username",
      password: "default password",
    },
    resolver: zodResolver(schema)
  });

  // const { mutate: login, isPending, isError, error } = useLogin();

  const handleOnLogin: SubmitHandler<FormFields> = async (data) => {
    try {
      await new Promise((resolve, reject) => {
        setTimeout(() => {
          resolve("Login successful");
        }, 2000);
      });
      throw new Error("Login failed");
    } catch (error) {
      setError("root", { message: "Invalid username or password" });
    }
  };

  // login(
  //   { username, password },
  //   {
  //     onSuccess: () => navigate("/home"),
  //   }
  // );

  return (
    <div>
      <form onSubmit={handleSubmit(handleOnLogin)}>
        <input
          {...register("username", 
            
          //   {
          //   required: "Username is required",
          //   minLength: {
          //     value: 5,
          //     message: "Username must be at least 5 characters",
          //   },
          // }
        )}
          type="text"
          placeholder="Username"
          onChange={() => {}}
        />

        {errors.username && <p>{errors.username.message}</p>}

        <input
          type="password"
          placeholder="Password"
          {...register("password", 
          //   {
          //   required: "Password is required",

          //   minLength: {
          //     value: 8,
          //     message: "Password must be at least 8 characters",
          //   },

          //   pattern: {
          //     value:
          //       /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/,
          //     message:
          //       "Password must contain uppercase, lowercase, number and special character",
          //   },

          //   validate: {
          //     noSpaces: (value) =>
          //       !value.includes(" ") || "Password cannot contain spaces",
          //   },
          // }
        
        )}
        />

        {errors.password && <p className="text-red-600" >{errors.password.message}</p>}

        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? "Logging in..." : "Login"}
        </button>

        {errors.root && <p className="text-red-400" >{errors.root.message}</p>}
      </form>

      {/* {isError && <p>{error?.response?.data?.message || "Login failed"}</p>} */}
    </div>
  );
};

export default LoginPage;
