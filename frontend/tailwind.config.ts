// tailwind.config.ts
import type { Config } from 'tailwindcss';

export default <Config>{
  darkMode: 'class', // enable class strategy for dark mode
  content: [
    './src/**/*.{js,ts,jsx,tsx}',
    './app/**/*.{js,ts,jsx,tsx}',
    './components/**/*.{js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {
      colors: {
        primary: 'hsl(212, 100%, 50%)',
        secondary: 'hsl(212, 15%, 30%)',
        accent: 'hsl(45, 100%, 50%)',
      },
    },
  },
  plugins: [],
};
