import { useEffect, useState } from "react";
import Papa from "papaparse";
import { z } from "zod";
import { CheckCircle2, XCircle } from "lucide-react";

import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/Components/ui/dialog";
import { csvRowSchema } from "@/zod/csvSchema";
import { useAddBatchContacts } from "@/hooks/contact/useContact";


type ImporterDialogProps = {
  open: boolean;
  setActiveDialog: (open: any) => void;
  title: string;
};

type RowError = {
  row: number;
  errors: Record<string, string[]>;
};

type Contact = {
  firstName: string;
  lastName: string;
  emails: { email: string; label: string }[];
  phones: { phone: string; label: string }[];
};

const REQUIRED_HEADERS = [
  "firstName",
  "lastName",
  "email_1",
  "email_label_1",
  "phone_1",
  "phone_label_1",
];

const OPTIONAL_HEADERS = [
  "email_2",
  "email_label_2",
  "phone_2",
  "phone_label_2",
];


function rowToContact(row: z.infer<typeof csvRowSchema>): Contact {
  return {
    firstName: row.firstName,
    lastName: row.lastName,
    emails: [
      { email: row.email_1, label: row.email_label_1 },
      ...(row.email_2
        ? [{ email: row.email_2, label: row.email_label_2 ?? "" }]
        : []),
    ],
    phones: [
      { phone: row.phone_1, label: row.phone_label_1 },
      ...(row.phone_2
        ? [{ phone: row.phone_2, label: row.phone_label_2 ?? "" }]
        : []),
    ],
  };
}


const ImporterDialog = ({
  open,
  setActiveDialog,
  title,
}: ImporterDialogProps) => {


  const [file, setFile] = useState<File | null>(null);
  const [parsedData, setParsedData] = useState<any[]>([]);
  const [totalRows, setTotalRows] = useState<number>(0);
  const [missingHeaders, setMissingHeaders] = useState<string[]>([]);
  const [detectedHeaders, setDetectedHeaders] = useState<string[]>([]);
  const [rowErrors, setRowErrors] = useState<RowError[]>([]);

  
    const {
      mutate: addBatchContact,
      isPending: bacthAddPending,
      isSuccess,
    } = useAddBatchContacts();
  

  const resetState = () => {
    setFile(null);
    setParsedData([]);
    setTotalRows(0);
    setMissingHeaders([]);
    setDetectedHeaders([]);
    setRowErrors([]);
  };

  const isHeaderPresent = (header: string) => detectedHeaders.includes(header);
  const hasFile = !!file;
  const isValid =
    hasFile && missingHeaders.length === 0 && parsedData.length > 0;


  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFile = e.target.files?.[0];
    if (!selectedFile) return;
    resetState();
    setFile(selectedFile);
  };

  const handleClose = () => {
    resetState();
    setActiveDialog(null);
  };

  const expandScientific = (value) => {
    if (!value.includes("e") && !value.includes("E")) return value;
    return Number(value).toLocaleString("fullwide", { useGrouping: false });
  };

  const handleParsing = () => {
    if (!file) return;

    Papa.parse(file, {
      header: true,
      skipEmptyLines: true,

      complete: (results) => {
        const headers = results.meta.fields ?? [];

        setDetectedHeaders(headers);
        setTotalRows(results.data.length);

        const missing = REQUIRED_HEADERS.filter((h) => !headers.includes(h));
        setMissingHeaders(missing);

        if (missing.length > 0) return;

        setParsedData(results.data as any[]);
      },

      error: (err) => console.error("CSV Parse Error:", err),
    });
  };

  const handleOnImport = () => {
    const succeeded: Contact[] = [];
    const failed: RowError[] = [];

    parsedData.forEach((row, index) => {

      const cleanedRow = {
        ...row,
        phone_1: expandScientific(row.phone_1),
        phone_2: expandScientific(row.phone_2),
      };

      const result = csvRowSchema.safeParse(cleanedRow);

      if (!result.success) {
        failed.push({
          row: index + 1,
          errors: result.error.flatten().fieldErrors as Record<
            string,
            string[]
          >,
        });
        return;
      }

      const intraRowErrors: Record<string, string[]> = {};

      if (result.data.email_2) {
        const email1 = result.data.email_1.trim().toLowerCase();
        const email2 = result.data.email_2.trim().toLowerCase();

        if (email1 === email2) {
          intraRowErrors["email_error"] = ["email_2 is same as email_1"];
        }
      }

      if (result.data.phone_2) {
        const phone1 = result.data.phone_1.trim();
        const phone2 = result.data.phone_2.trim();

        if (phone1 === phone2) {
          intraRowErrors["phone_error"] = ["phone_2 is same as phone_1"];
        }
      }

      if (Object.keys(intraRowErrors).length > 0) {
        failed.push({ row: index + 1, errors: intraRowErrors });
        return;
      }

      succeeded.push(rowToContact(result.data));
    });

    setRowErrors(failed);

    if (succeeded.length === 0) return;


    addBatchContact(succeeded, {
      onSuccess: () => {
        setActiveDialog(false);
        resetState();
      },
    });

  };

  
  useEffect(() => {
    handleParsing();
  }, [file]);

  return (
    <Dialog
      open={open}
      onOpenChange={(isOpen) => {
        if (!isOpen) handleClose();
      }}
    >
      <DialogContent className="max-w-md">
        <DialogHeader>
          <DialogTitle>{title}</DialogTitle>
        </DialogHeader>

        <div className="space-y-4">
          <p className="text-sm text-gray-500">
            Upload a CSV file to import contacts.
          </p>

          {/* FILE UPLOADER */}
          <label className="block cursor-pointer">
            <div className="border border-dashed rounded-md p-6 text-center hover:bg-gray-50 transition">
              <p className="text-sm font-medium">
                {file ? file.name : "Click to select a CSV file"}
              </p>
              <p className="text-xs text-gray-400 mt-1">CSV files only</p>
            </div>
            <input
              type="file"
              accept=".csv"
              className="hidden"
              onChange={handleFileChange}
            />
          </label>

          {/* HEADER VALIDATION */}
          <div className="rounded-md border p-3 space-y-2">
            <p className="text-xs font-semibold text-gray-600 uppercase tracking-wide">
              Required Columns
            </p>
            <div className="grid grid-cols-2 gap-1">
              {REQUIRED_HEADERS.map((header) => {
                const present = isHeaderPresent(header);
                return (
                  <div key={header} className="flex items-center gap-1.5">
                    {hasFile ? (
                      present ? (
                        <CheckCircle2 className="w-3.5 h-3.5 text-green-500 shrink-0" />
                      ) : (
                        <XCircle className="w-3.5 h-3.5 text-red-500   shrink-0" />
                      )
                    ) : (
                      <div className="w-3.5 h-3.5 rounded-full border border-gray-300 shrink-0" />
                    )}
                    <span
                      className={`text-xs font-mono ${
                        hasFile
                          ? present
                            ? "text-green-700"
                            : "text-red-600 font-semibold"
                          : "text-gray-500"
                      }`}
                    >
                      {header}
                    </span>
                  </div>
                );
              })}
            </div>

            <p className="text-xs font-semibold text-gray-600 uppercase tracking-wide pt-1">
              Optional Columns
            </p>
            <div className="grid grid-cols-2 gap-1">
              {OPTIONAL_HEADERS.map((header) => {
                const present = isHeaderPresent(header);
                return (
                  <div key={header} className="flex items-center gap-1.5">
                    {hasFile && present ? (
                      <CheckCircle2 className="w-3.5 h-3.5 text-green-500 shrink-0" />
                    ) : (
                      <div className="w-3.5 h-3.5 rounded-full border border-gray-300 shrink-0" />
                    )}
                    <span className="text-xs font-mono text-gray-400">
                      {header}
                    </span>
                  </div>
                );
              })}
            </div>
          </div>

          {/* ROW COUNT / HEADER ERROR SUMMARY */}
          {hasFile && (
            <p
              className={`text-xs ${
                isValid ? "text-green-600" : "text-red-500"
              }`}
            >
              {isValid
                ? `✓ ${totalRows} row${
                    totalRows !== 1 ? "s" : ""
                  } ready to import`
                : missingHeaders.length > 0
                ? `✗ Fix missing columns before importing — ${totalRows} row${
                    totalRows !== 1 ? "s" : ""
                  } detected`
                : "No valid rows found"}
            </p>
          )}

          {/* ROW VALIDATION ERRORS (shown after Import click) */}
          {rowErrors.length > 0 && (
            <div className="rounded-md border border-red-200 bg-red-50 p-3 space-y-1 max-h-36 overflow-y-auto">
              <p className="text-xs font-semibold text-red-600">
                {rowErrors.length} row{rowErrors.length !== 1 ? "s" : ""} failed
                validation
              </p>
              {rowErrors.map(({ row, errors }) => (
                <div key={row} className="text-xs text-red-500">
                  <span className="font-medium">Row {row}:</span>{" "}
                  {Object.entries(errors)
                    .map(([field, msgs]) => `${field} — ${msgs[0]}`)
                    .join(", ")}
                </div>
              ))}
            </div>
          )}

          {/* ACTIONS */}
          <div className="flex justify-end gap-2 pt-2">
            <button
              className="px-3 py-1 text-sm border rounded"
              onClick={handleClose}
            >
              Cancel
            </button>
            <button
              className="px-3 py-1 text-sm bg-black text-white rounded disabled:opacity-50"
              onClick={handleOnImport}
              disabled={!isValid}
            >
              Import
            </button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default ImporterDialog;
