import { useNavigate } from "react-router-dom";
import { useAuth } from "../Components/AuthProvider";

const LoginPage = () => {
  const { setUser } = useAuth();
  const navigate = useNavigate();

  const handleLogin = () => {
    setUser({ id: 1, name: "Shayan" });
    navigate("/home");
  };

  return (
    <div>
      <h1>Login</h1>
      <button onClick={handleLogin}>Login</button>
    </div>
  );
};

export default LoginPage;