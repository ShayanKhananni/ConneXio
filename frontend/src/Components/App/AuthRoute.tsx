import { useAuthStore } from '@/store/authStore'
import { Navigate, Outlet } from 'react-router-dom';  // ← remove unused React import

const AuthRoute = () => {
  const { token } = useAuthStore();

  return token ? <Navigate to="/" replace /> : <Outlet />;  
};

export default AuthRoute;