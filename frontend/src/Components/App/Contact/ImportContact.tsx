import Papa from "papaparse";

const ImportContact = () => {
  const handleOnFileUpload = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];

    if (!file) return;

    Papa.parse(file, {
      header: true,
      skipEmptyLines: true,
      complete: (results) => {
        console.log(results.data);
      },
      error: (error) => {
        console.error("CSV Parse Error:", error);
      },
    });
  };

  return (
    <>
      <button>
        <label htmlFor="csv-upload">Import Contacts</label>
      </button>

      <input
        id="csv-upload"
        type="file"
        accept=".csv"
        onChange={handleOnFileUpload}
        className="hidden"
      />
    </>
  );
};

export default ImportContact;
