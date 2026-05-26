// src/components/ui/button.tsx
import React from "react";
import { cn } from "@/lib/utils"; // utility for classNames, optional

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: "default" | "destructive" | "outline" | "secondary" | "ghost" | "link";
  size?: "default" | "sm" | "lg";
}

const buttonVariants = (variant: ButtonProps["variant"], size: ButtonProps["size"]) => {
  const base = "inline-flex items-center justify-center rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none ring-offset-background";
  const variants: Record<string, string> = {
    default: "bg-primary text-primary-foreground hover:bg-primary/90",
    destructive: "bg-destructive text-destructive-foreground hover:bg-destructive/90",
    outline: "border border-input hover:bg-accent hover:text-accent-foreground",
    secondary: "bg-secondary text-secondary-foreground hover:bg-secondary/80",
    ghost: "hover:bg-accent hover:text-accent-foreground",
    link: "underline-offset-4 hover:underline text-primary",
  };
  const sizes: Record<string, string> = {
    default: "h-10 py-2 px-4",
    sm: "h-9 px-3 rounded-md",
    lg: "h-11 px-8 rounded-md",
  };
  return `${base} ${variants[variant ?? "default"]} ${sizes[size ?? "default"]}`;
};

export const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant, size, children, ...props }, ref) => (
    <button
      className={cn(buttonVariants(variant, size), className)}
      ref={ref}
      {...props}
    >
      {children}
    </button>
  )
);
Button.displayName = "Button";

export default Button;

// src/components/ui/toast.tsx
import * as React from "react";
import { useToast } from "@/hooks/useToast"; // custom hook placeholder

export const ToastProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  // In a real project you would render shadcn/ui Toast components here.
  return <>{children}</>;
};

export default ToastProvider;

// src/components/ui/dialog.tsx
import * as React from "react";
export const Dialog: React.FC<{ open: boolean; onOpenChange: (open: boolean) => void; children: React.ReactNode }> = ({ open, onOpenChange, children }) => {
  if (!open) return null;
  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/50">
      <div className="bg-background rounded-lg p-4 shadow-lg max-w-md w-full">
        {children}
        <button onClick={() => onOpenChange(false)} className="mt-4 btn">
          Close
        </button>
      </div>
    </div>
  );
};

export default Dialog;
