import { useQueryClient } from "@tanstack/react-query";
import {Upload } from "lucide-react";
import { toast } from "react-toastify";

type ExportProps = {
  pageNumber: number;
  pageSize: number;
  search: string;
};

const ExportContacts = ({
  pageNumber,
  pageSize,
  search,
}: ExportProps) => {
  const queryClient = useQueryClient();



  const convertToCSV = (data: any[]) => {
    if (!data?.length) return "";
  
    const rows = data.map((row) => {
      return {
        firstName: row.firstName ?? "",
        lastName: row.lastName ?? "",
  
        // EMAILS (max 2)
        email_1: row.emails?.[0]?.email ?? "",
        label_1: row.emails?.[0]?.label ?? "",
  
        email_2: row.emails?.[1]?.email ?? "",
        label_2: row.emails?.[1]?.label ?? "",
  
        // PHONES (max 2)
        phone_1: row.phones?.[0]?.phone ?? "",
        phone_label_1: row.phones?.[0]?.label ?? "",
  
        phone_2: row.phones?.[1]?.phone ?? "",
        phone_label_2: row.phones?.[1]?.label ?? "",
  
        // SOCIAL LINKS
        linkedinUrl: row.linkedinUrl ?? "",
        instagramUrl: row.instagramUrl ?? "",
        facebookUrl: row.facebookUrl ?? "",
      };
    });
  
  
    const headers = Object.keys(rows[0] ?? {});
  
    const csvRows = [
      headers.join(","),
      ...rows.map((row) =>
        headers
          .map((field) => {
            const value = row[field] ?? "";
            return `"${String(value).replace(/"/g, '""')}"`;
          })
          .join(",")
      ),
    ];
  
    return csvRows.join("\n");
  };


  const downloadCSV = (csv: string) => {
    const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });

    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");

    link.href = url;
    link.setAttribute("download", "contacts.csv");

    document.body.appendChild(link);
    link.click();

    document.body.removeChild(link);
    URL.revokeObjectURL(url);
  };


  const handleExport = () => {
    const cacheKey = ["contacts", pageNumber, pageSize, search];
    const cached = queryClient.getQueryData<any>(cacheKey);
  
    if (!cached?.content || cached.content.length === 0) {
      toast.error("No  data found for this page to Export");
      return;
    }

    const contacts = cached?.content ?? cached?.data ?? cached;
    const csv = convertToCSV(contacts);
    downloadCSV(csv);
  };

  return (
    <button
  onClick={handleExport}
  className="flex items-center text-sm gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg"
>      
<Upload className="w-4 h-4" />
  Export CSV
</button>
  );
};

export default ExportContacts;