import { createContext, useContext, useEffect, useState, type PropsWithChildren } from "react";

type User = {
  id: number;
  name: string;
};

type AuthContextType = {
  user: User | null;
  loading: boolean;
  setUser: (user: User | null) => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);


export default function AuthProvider({ children }: PropsWithChildren) {

  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  // simulate async auth (replace with Supabase later)
  useEffect(() => {
    setTimeout(() => {
      // change this to simulate login
      setUser(null); 
      setLoading(false);
    }, 500);
  }, []);

  return (
    <AuthContext.Provider value={{ user, loading, setUser }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => {
  const context = useContext(AuthContext);

  if (context === undefined) {
    throw new Error("useAuth must be used within AuthProvider");
  }

  return context;
};