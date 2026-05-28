import { useState } from "react";

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
    <div className="w-full max-w-md mx-auto mt-2">
      <div className="relative">
        <input
          type="text"
          value={search}
          onChange={handleOnSearch}
          placeholder="Search contacts by firstname"
          className="
            w-full
            px-2 py-2
            pl-10
            border border-gray-300
            rounded-xl
            shadow-sm
            focus:outline-none
            focus:ring-2
            focus:ring-blue-500
            focus:border-blue-500
            transition
          "
        />

        <span className="absolute left-3 top-3.5 text-gray-400">
          🔍
        </span>
      </div>

      {searchError && (
        <p className="text-red-500 text-sm mt-1">
          {searchError}
        </p>
      )}
    </div>
  );
};

export default ContactSearcher;