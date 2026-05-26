// src/components/layout/Navbar.tsx
import Link from "next/link";
import { useAppSelector, useAppDispatch } from "@/store/provider";
import { selectAuth, logout } from "@/features/auth/authSlice";
import { Button } from "@/components/ui/button";

export const Navbar = () => {
  const auth = useAppSelector(selectAuth);
  const dispatch = useAppDispatch();

  const handleLogout = async () => {
    // In a real app you would call logout mutation; here we just clear state
    dispatch(logout());
  };

  return (
    <nav className="flex items-center justify-between p-4 bg-primary text-primary-foreground">
      <Link href="/" className="font-bold text-xl">
        RecipeFlow
      </Link>
      <div className="flex items-center gap-4">
        {auth.user ? (
          <>
            <span>{auth.user.username}</span>
            <Button onClick={handleLogout}>Logout</Button>
          </>
        ) : (
          <>
            <Link href="/login">
              <Button variant="outline">Login</Button>
            </Link>
            <Link href="/register">
              <Button>Register</Button>
            </Link>
          </>
        )}
      </div>
    </nav>
  );
};
