import { ChevronDown } from "lucide-react";

const Header = () => {
  // dummy user (you will replace later with state/API)
  const user = {
    name: "John Doe",
    image: null, // try: "https://via.placeholder.com/40"
  };

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
        {user.image ? (
          <img
            src={user.image}
            alt="user avatar"
            className="w-9 h-9 rounded-full object-cover border"
          />
        ) : (
          <div className="w-9 h-9 rounded-full bg-gray-300 flex items-center justify-center text-sm font-semibold text-gray-700">
            {getInitials(user.name)}
          </div>
        )}

        {/* Username */}
        <span className="text-sm font-medium text-gray-700">
          {user.name}
        </span>

        {/* Dropdown */}
        <ChevronDown className="w-4 h-4 text-gray-600" />
      </div>
    </header>
  );
};

export default Header;