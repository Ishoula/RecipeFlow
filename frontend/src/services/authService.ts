import axiosInstance from '@/lib/axiosInstance';
import type { LoginRequest, SignupRequest, UserResponse } from '@/types/auth';

export const login = async (data: LoginRequest) => {
  const response = await axiosInstance.post<{ token: string; user: UserResponse }>('/api/v1/auth/login', data);
  return response.data;
};

export const register = async (data: SignupRequest) => {
  const response = await axiosInstance.post<UserResponse>('/api/v1/auth/register', data);
  return response.data;
};
