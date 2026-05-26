import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useDispatch } from "react-redux";
import { useRouter } from "next/navigation";
import { login as loginAction } from "@/store/authSlice";
import { login as loginApi } from "@/services/authService";
import type { LoginRequest } from "@/types/auth";

const loginSchema = z.object({
  username: z.string().min(1, "Username is required"),
  password: z.string().min(1, "Password is required"),
});

type LoginForm = z.infer<typeof loginSchema>;

export default function LoginPage() {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginForm>({ resolver: zodResolver(loginSchema) });
  const dispatch = useDispatch();
  const router = useRouter();

  const onSubmit = async (data: LoginForm) => {
    const payload: LoginRequest = { username: data.username, password: data.password };
    try {
      const response = await loginApi(payload);
      dispatch(loginAction(response));
      router.push("/recipes");
    } catch (e) {
      console.error(e);
      // You could set error UI here
    }
  };

  return (
    <section className="flex flex-col items-center justify-center min-h-screen bg-gray-100 dark:bg-gray-900">
      <h1 className="text-2xl font-bold mb-4">Login</h1>
      <form onSubmit={handleSubmit(onSubmit)} className="w-full max-w-sm space-y-4">
        <div>
          <label className="block text-sm font-medium" htmlFor="username">Username</label>
          <input
            id="username"
            type="text"
            {...register("username")}
            className="mt-1 block w-full rounded border p-2"
          />
          {errors.username && <p className="text-red-600 text-sm">{errors.username.message}</p>}
        </div>
        <div>
          <label className="block text-sm font-medium" htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            {...register("password")}
            className="mt-1 block w-full rounded border p-2"
          />
          {errors.password && <p className="text-red-600 text-sm">{errors.password.message}</p>}
        </div>
        <button
          type="submit"
          disabled={isSubmitting}
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
        >
          {isSubmitting ? "Logging in..." : "Login"}
        </button>
      </form>
    </section>
  );
}
