import {
  useContactDetails,
  useContacts,
  useDeleteContact,
} from "@/hooks/contact/useContact";
import { useState } from "react";
import ConfirmDialog from "@/Components/ui/ConfirmDialog";
import ContactInfoModal from "./ContactInfoDialog";
import ContactCard from "./ConactCard";
import ContactUpdateDialog from "./ContactUpdateDialog";
import CreateContactDialog from "./CreateContactDialog";

import { UserRound, Plus } from "lucide-react";

const ContactContainer = () => {
  /// States
  const [selectedContactId, setSelectedContactId] = useState<number | null>(
    null
  );

  const [openDeleteDialog, setDeleteOpen] = useState(false);

  type ActiveDialog = "info" | "update" | "delete" | "create" | null;
  const [activeDialog, setActiveDialog] = useState<ActiveDialog>(null);

  //  Mutatations And Queries

  const { data: contacts, isLoading: isContactsLoading, error } = useContacts();

  const { data: contactDetails, isLoading: isDetailsLoading } =
    useContactDetails(
      selectedContactId,
      activeDialog === "info" || activeDialog === "update"
    );

  const deleteMutation = useDeleteContact();

  if (isContactsLoading) return <p>Loading...</p>;
  if (error) return <p>Something went wrong</p>;

  /// Event Handlers

  const handleOnDelete = (contactId: number) => {
    setSelectedContactId(contactId);
    setDeleteOpen(true);
  };

  const handleOnDeleteConfirm = () => {
    if (selectedContactId === null) return;

    deleteMutation.mutate(selectedContactId);
    setDeleteOpen(false);
    setSelectedContactId(null);
  };

  const handleOnInfo = (contactId: number) => {
    setSelectedContactId(contactId);
    setActiveDialog("info");
  };

  const handleOnCreate = () => {
    setActiveDialog("create");
  };

  const handleOnUpdate = (contactId: number) => {
    setSelectedContactId(contactId);
    setActiveDialog("update");
  };

  return (
    <>
      <button
        onClick={handleOnCreate}
        className="flex items-center ms-auto mt-3 me-5 gap-2 bg-green-600 hover:bg-green-700 text-white text-sm font-medium px-4 py-2 rounded-md transition-colors shadow-sm"
      >
        <Plus className="w-4 h-4 text-white" />
        <UserRound className="w-4 h-4 text-white" />
        Add Contact
      </button>


      <div className="grid grid-cols-1 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 grid-rows-2 gap-4 p-5 mt-2 border-2 mx-5">

        
        <ContactUpdateDialog
          contact={contactDetails}
          open={activeDialog === "update"}
          title="Update Contact"
          setActiveDialog={setActiveDialog}
        />

        <ContactInfoModal
          contact={contactDetails}
          open={activeDialog === "info"}
          title="Contact Details"
          setActiveDialog={setActiveDialog}
        />

        <CreateContactDialog
          open={activeDialog === "create"}
          title="Create Contact"
          setActiveDialog={setActiveDialog}
        />

        <ConfirmDialog
          open={openDeleteDialog}
          setOpen={setDeleteOpen}
          title="Delete Contact"
          onConfirm={handleOnDeleteConfirm}
          description="Are you sure you want to delete this contact? All associated data will be deleted too!"
        />

        {contacts?.map((contact) => (
          <ContactCard
            key={contact.contactId}
            contact={contact}
            onDelete={handleOnDelete}
            onInfo={handleOnInfo}
            onUpdate={handleOnUpdate}
          />
        ))}
      </div>
    </>
  );
};

export default ContactContainer;
