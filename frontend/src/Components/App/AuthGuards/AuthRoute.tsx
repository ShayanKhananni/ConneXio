import { useAuthStore } from '@/store/authStore'
import { Navigate, Outlet } from 'react-router-dom';  

const AuthRoute = () => {
  const { token } = useAuthStore();

  return token ? <Navigate to="/" replace /> : <Outlet />;  
};

export default AuthRoute;