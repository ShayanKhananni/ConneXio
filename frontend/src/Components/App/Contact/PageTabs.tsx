

type PageTabsProps = {
  totalPages: number;
  currentPage: number;
  handleOnPageChange: (
    page: number
  ) => void;
};


const PageTabs = ({
  totalPages,
  currentPage,
  handleOnPageChange,
}: PageTabsProps) => {
  return (
    <div className="flex gap-2 mt-4  flex-wrap">

      <p className="mt-1" >Pages</p>

      {Array.from(
        { length: totalPages },
        (_, index) => {
          const page = index + 1;

          return (
            <button
              key={page}
              onClick={() =>
                handleOnPageChange(page)
              }
              className={`
                px-3 text-md font-bold py-1 rounded border
                ${
                  currentPage === page
                    ? "bg-black text-white"
                    : "bg-white text-black"
                }
              `}
            >
              {page}
            </button>
          );
        }
      )}
    </div>
  );
};

export default PageTabs;