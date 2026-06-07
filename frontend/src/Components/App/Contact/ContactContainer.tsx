import { useState, useMemo, useCallback } from "react";
import {
  useContactDetails,
  useDeleteContact,
  usePagedContacts,
} from "@/hooks/contact/useContact";

import ConfirmDialog from "@/Components/ui/ConfirmDialog";
import ContactInfoModal from "./ContactInfoDialog";
import ContactCard from "./ConactCard";
import ContactUpdateDialog from "./ContactUpdateDialog";
import CreateContactDialog from "./CreateContactDialog";

import { Plus, FileUp } from "lucide-react";
import PageTabs from "./PageTabs";
import ContactSearcher from "./ContactSearcher";
import ExportContacts from "./ExportContacts";
import ImporterDialog from "./ImporterDialog";

type ActiveDialog = "info" | "update" | "delete" | "create" | "import" | null;

const ContactContainer = () => {
  const [selectedContactId, setSelectedContactId] = useState<number | null>(
    null
  );
  const [activeDialog, setActiveDialog] = useState<ActiveDialog>(null);

  const [search, setSearch] = useState("");
  const [pageNumber, setPageNumber] = useState(1);
  const [pageSize] = useState(6);

  const [openDeleteDialog, setDeleteOpen] = useState(false);

  const {
    data: pagedContacts,
    isLoading: isPagedLoading,
    error,
  } = usePagedContacts(pageNumber, pageSize, search);

  const shouldFetchDetails =
    selectedContactId !== null &&
    (activeDialog === "info" || activeDialog === "update");

  const { data: contactDetails } = useContactDetails(
    selectedContactId,
    shouldFetchDetails
  );

  const deleteMutation = useDeleteContact();

  const handleOnDelete = useCallback((id: number) => {
    setSelectedContactId(id);
    setDeleteOpen(true);
  }, []);

  const handleOnDeleteConfirm = useCallback(() => {
    if (!selectedContactId) return;

    deleteMutation.mutate(selectedContactId);
    setDeleteOpen(false);
    setSelectedContactId(null);
  }, [selectedContactId, deleteMutation]);

  const handleOnInfo = useCallback((id: number) => {
    setSelectedContactId(id);
    setActiveDialog("info");
  }, []);

  const handleOnUpdate = useCallback((id: number) => {
    setSelectedContactId(id);
    setActiveDialog("update");
  }, []);

  const handleOnCreate = useCallback(() => {
    setActiveDialog("create");
  }, []);

  const handleOnPageChange = useCallback((page: number) => {
    setPageNumber(page);
  }, []);

  const handleOnExport = useCallback(() => {
    setActiveDialog("import");
  }, []);


  const contacts = useMemo(() => pagedContacts?.content ?? [], [pagedContacts]);

  if (isPagedLoading) return <p>Loading...</p>;
  if (error) return <p>Something went wrong</p>;

  return (
    <>
      {/* SEARCH */}
      <ContactSearcher search={search} setSearch={setSearch} />
      
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-4 items-center sm:ps-10 my-4 lg:px-16 px-5">

        {/* Actions */}
        <div className="flex  order-1 lg:order-2  justify-center  lg:justify-end gap-2">
          <button
            onClick={handleOnCreate}
            className="flex items-center justify-center gap-2 bg-green-600 hover:bg-green-700 text-white text-sm font-medium px-4 py-2 rounded-md transition-colors shadow-sm"
          >
            <Plus className="w-4 h-4" />
            Add
          </button>

          <button
            onClick={handleOnExport}
            className="flex items-center justify-center gap-2 bg-green-600 hover:bg-green-700 text-white text-sm font-medium px-4 py-2 rounded-md transition-colors shadow-sm"
          >
            <FileUp className="w-4 h-4" />
            Import
          </button>

          <ExportContacts
            pageNumber={pageNumber}
            pageSize={pageSize}
            search={search}
          />
        </div>

        {/* Pagination Info */}
        <div className="flex flex-col sm:flex-row order-2 lg:order-1 gap-2 sm:gap-6  text-sm sm:text-center text-gray-600">
          <span>
            Showing {pagedContacts?.numberOfElements ?? 0} of{" "}
            {pagedContacts?.totalElements ?? 0} contacts
          </span>

          <span>
            Page {(pagedContacts?.number ?? 0) + 1} of{" "}
            {pagedContacts?.totalPages ?? 0}
          </span>
        </div>
      </div>

      <div className=" px-5 h-full m-5  main-container">
        {/* CONTACT GRID */}

        {contacts.length === 0 ? (
          <div className="text-center text-gray-500 mt-10">
            No contacts found Please Add Some Contacts!!!
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {contacts.map((contact) => (
              <ContactCard
                key={contact.contactId}
                contact={contact}
                onDelete={handleOnDelete}
                onInfo={handleOnInfo}
                onUpdate={handleOnUpdate}
              />
            ))}
          </div>
        )}
{/* 
        {contacts.length && ( */}
          <PageTabs
            totalPages={pagedContacts?.totalPages ?? 0}
            currentPage={pageNumber}
            handleOnPageChange={handleOnPageChange}
          />
        
      </div>

      {activeDialog === "update" && (
        <ContactUpdateDialog
          contact={contactDetails}
          open
          title="Update Contact"
          setActiveDialog={setActiveDialog}
        />
      )}

      {activeDialog === "info" && (
        <ContactInfoModal
          contact={contactDetails}
          open
          title="Contact Details"
          setActiveDialog={setActiveDialog}
        />
      )}

      {activeDialog === "create" && (
        <CreateContactDialog
          open
          title="Create Contact"
          setActiveDialog={setActiveDialog}
        />
      )}

      {activeDialog === "import" && (
        <ImporterDialog
          open
          title="Import Contact"
          setActiveDialog={setActiveDialog}
        />
      )}

      <ConfirmDialog
        open={openDeleteDialog}
        setOpen={setDeleteOpen}
        title="Delete Contact"
        onConfirm={handleOnDeleteConfirm}
        description="Are you sure you want to delete this contact? All associated data will be deleted too!"
      />
    </>
  );
};

export default ContactContainer;
