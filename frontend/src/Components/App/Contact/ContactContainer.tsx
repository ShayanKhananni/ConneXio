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

import { UserRound, Plus } from "lucide-react";
import PageTabs from "./PageTabs";
import ContactSearcher from "./ContactSearcher";

type ActiveDialog = "info" | "update" | "delete" | "create" | null;

const ContactContainer = () => {


  // ---------------- STATE ----------------
  const [selectedContactId, setSelectedContactId] = useState<number | null>(null);
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


  const contacts = useMemo(
    () => pagedContacts?.content ?? [],
    [pagedContacts]
  );


  if (isPagedLoading) return <p>Loading...</p>;
  if (error) return <p>Something went wrong</p>;


  return (
    <>
      <div className="border-2 px-5  mx-5 main-container">
        {/* SEARCH */}
        <ContactSearcher search={search} setSearch={setSearch} />

        {/* ADD BUTTON */}
        <button
          onClick={handleOnCreate}
          className="flex items-center ms-auto mb-2 me-2 mt-1 gap-2 bg-green-600 hover:bg-green-700 text-white text-sm font-medium px-4 py-2 rounded-md transition-colors shadow-sm"
        >
          <Plus className="w-4 h-4" />
          <UserRound className="w-4 h-4" />
          Add Contact
        </button>

        {/* CONTACT GRID */}
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