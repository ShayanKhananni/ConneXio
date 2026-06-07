import { useState } from "react";
import { FiSearch } from "react-icons/fi";


const ContactSearcher = ({
  search,
  setSearch,
}: {
  search: string;
  setSearch: (value: string) => void;
}) => {
  const [searchError, setSearchError] =
    useState<string | null>(null);

  const handleOnSearch = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const value = e.target.value;

    // empty allowed
    if (value.length === 0) {
      setSearch(value);
      setSearchError(null);
      return;
    }

    // min length check
    if (value.length < 3) {
      setSearch(value);
      setSearchError(
        "Please enter at least 3 characters"
      );
      return;
    }

    // regex: only letters
    const onlyLetters = /^[A-Za-z\s]+$/;

    if (!onlyLetters.test(value)) {
      setSearch(value);
      setSearchError(
        "Only letters are allowed"
      );
      return;
    }

    setSearch(value);
    setSearchError(null);
  };

  return (

    <div className="w-full max-w-md mx-auto px-5 mt-5">
  <div className="relative">

    {/* Search Icon */}
    <FiSearch
      className="
        absolute left-3 top-1/2 -translate-y-1/2
        text-black-400 text-lg
        pointer-events-none
      "
    />

    <input
      type="text"
      value={search}
      onChange={handleOnSearch}
      placeholder="Search contacts by first name..."
      className="
        w-full
        pl-10 pr-4 py-3
        bg-white
        border border-gray-200
        rounded-2xl
        shadow-sm
        text-sm
        text-gray-700
        placeholder:text-gray-400
        focus:outline-none
        focus:ring-2
        focus:ring-blue-500/20
        focus:border-blue-500
        hover:shadow-md
        transition-all duration-200
      "
    />
  </div>

  {searchError && (
    <p className="text-red-500 text-sm mt-2">
      {searchError}
    </p>
  )}
</div>
  );
};

export default ContactSearcher;