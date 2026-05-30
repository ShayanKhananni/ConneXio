import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/Components/ui/dropdown-menu";
import { useAuthStore } from "@/store/authStore";

import { ChevronDown, User, LogOut, Lock } from "lucide-react";


type UserDropDownProps = {
  setActiveProfile: () => void;
  setActivePassword: () => void;
};


const UserDropDown = ({setActiveProfile,setActivePassword}:UserDropDownProps) => {


  const logout = useAuthStore((state) => state.logout);


  return (
    <DropdownMenu>

      <DropdownMenuTrigger asChild>
        <button className="flex items-center gap-1 text-sm font-medium text-gray-700 hover:text-black transition">
          <ChevronDown className="w-4 h-4 text-gray-600" />
        </button>
      </DropdownMenuTrigger>

      {/* Content */}
      <DropdownMenuContent align="end" className="w-48">
        <DropdownMenuItem onClick={setActiveProfile}  className="flex items-center gap-2">
          <User className="w-4 h-4" />
          Profile
        </DropdownMenuItem>

        <DropdownMenuItem onClick={setActivePassword} className="flex items-center gap-2">
          <Lock className="w-4 h-4" />
          Change Password
        </DropdownMenuItem>


        <DropdownMenuSeparator />

        <DropdownMenuItem onClick={logout} className="flex items-center gap-2 text-red-600 focus:text-red-600">
          <LogOut className="w-4 h-4"
           />
          Logout
        </DropdownMenuItem>

        
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default UserDropDown;