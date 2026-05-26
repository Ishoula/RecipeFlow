import type { NextConfig } from 'next';

/**
 * Next.js configuration for the RecipeFlow frontend.
 * Uses the App Router (app directory) and enables future Webpack 5 features.
 */
const nextConfig: NextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  // Enable experimental appDir if using older Next versions; for v16+ it's default.
  experimental: {
    appDir: true,
  },
  // Allow environment variables prefixed with NEXT_PUBLIC_ to be exposed to the client.
  env: {
    NEXT_PUBLIC_API_URL: process.env.NEXT_PUBLIC_API_URL,
  },
};

export default nextConfig;
