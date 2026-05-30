import { useState } from "react";
import ChangePasswordDialog from "../Contact/ChangePasswordDialog";
import UserDropDown from "./UserDropDown";
import UserProfile from "./UserProfile";
import { useCurrentUser } from "@/hooks/user/users";



const Header = () => {


  const [activeDialog, setActiveDialog] = useState<
    "profile" | "password" | null
  >(null);


  const { data: user, isLoading, error } = useCurrentUser();
  

  // fallback initials
  const getInitials = (name: string) => {
    return name
      .split(" ")
      .map((n) => n[0])
      .slice(0, 2)
      .join("")
      .toUpperCase();
  };

  return (
    <>
      <header className="w-full lg:px-10 px-3 py-3 border-b bg-white flex items-center justify-between">
        {/* LEFT: LOGO */}
        <div className="flex items-center gap-2">
          <img
            src="https://via.placeholder.com/40"
            alt="logo"
            className="w-10 h-10 rounded-md object-cover"
          />

          <span className="font-semibold text-lg">MyApp</span>
        </div>

        {/* RIGHT: USER */}
        <div className="flex items-center gap-3 cursor-pointer hover:opacity-80 transition">
          {/* AVATAR */}
          {isLoading ? (
            <div className="w-9 h-9 rounded-full bg-gray-200 animate-pulse" />
          ) : user?.profImageUrl ? (
            <img
              src={user.profImageUrl}
              alt="user avatar"
              className="w-9 h-9 rounded-full object-cover border"
            />
          ) : (
            <div className="w-9 h-9 rounded-full bg-gray-300 flex items-center justify-center text-sm font-semibold text-gray-700">
              {user?.username ? getInitials(user.username) : "?"}
            </div>
          )}

          {/* Username */}
          <span className="text-sm font-medium text-gray-700">
            {user?.username || "User"}
          </span>

          <UserDropDown
            setActiveProfile={() => setActiveDialog("profile")}
            setActivePassword={() => setActiveDialog("password")}
          />
        </div>
      </header>

      {user && (
        <UserProfile
          open={activeDialog === "profile"}
          setActiveDialog={setActiveDialog}
          title="Update Profile"
          defaultValues={{
            username: user.username || "",
            email: user.email || "",
            phone: user.phone || "",
            linkedinUrl: user.linkedinUrl || "",
            instagramUrl: user.instagramUrl || "",
            facebookUrl: user.facebookUrl || "",
            profImageUrl: user.profImageUrl || "",
          }}
          onSubmit={(data) => {
            console.log(data);

            // call mutation here
          }}
        />
      )}

      <ChangePasswordDialog
        title="Change Password"
        open={activeDialog === "password"}
        setActiveDialog={setActiveDialog}
        onSubmit={(data) => {
          console.log("password data:", data);
          // call mutation here
        }}
      />
    </>
  );
};

export default Header;
