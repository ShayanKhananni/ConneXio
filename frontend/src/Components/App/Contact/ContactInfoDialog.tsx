import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/Components/ui/dialog";
import { Button } from "@/Components/ui/button";
import type { Contact, ContatcInfo } from "@/types/contactTypes";
import { User, Phone, Mail, Link } from "lucide-react";
import { FaFacebookSquare, FaLinkedin, FaInstagram } from "react-icons/fa";

type ContactInfoDialogTypes = {
  contact: Contact | null;
  open: boolean;
  setActiveDialog: (open: any) => void;
  title: string;
};

const ContactInfoModal = ({
  contact,
  open,
  setActiveDialog,
  title,
}: ContactInfoDialogTypes) => {
  const initials = contact
    ? `${contact.firstName?.[0] ?? ""}${
        contact.lastName?.[0] ?? ""
      }`.toUpperCase()
    : "";

  return (
    <Dialog
      open={open}
      onOpenChange={(isOpen) => {
        if (!isOpen) {
          setActiveDialog(null);
        }
      }}
    >
      <DialogContent className="sm:max-w-2xl p-5 select-none">
        {/* Header */}
        <DialogHeader className="mb-4">
          <div className="flex items-center gap-2">
            <User className="w-4 h-4 text-muted-foreground" />
            <DialogTitle className="text-lg font-semibold">{title}</DialogTitle>
          </div>
        </DialogHeader>

        {!contact ? (
          <div className="text-xs text-muted-foreground">Loading...</div>
        ) : (
          <div className="space-y-4">
            {/* Avatar + Name */}
            <div className="flex items-center gap-3 px-1">
              {contact.profileImageUrl ? (
                <img
                  src={contact.profileImageUrl}
                  alt={`${contact.firstName} ${contact.lastName}`}
                  className="w-10 h-10 rounded-full object-cover"
                />
              ) : (
                <div className="w-10 h-10 rounded-full bg-muted flex items-center justify-center text-xs font-semibold">
                  {initials}
                </div>
              )}

              <div>
                <p className="font-semibold text-sm">
                  {contact.firstName} {contact.lastName}
                </p>

                {contact.emails?.[0] && (
                  <p className="text-[11px] text-muted-foreground">
                    {contact.emails[0].email}
                  </p>
                )}
              </div>
            </div>

            {/* Info Grid (UPDATED RESPONSIVE GRID) */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-2">
              {/* Emails */}
              {contact.emails?.map((e) => (
                <div
                  key={e.id}
                  className="flex items-start gap-2 rounded-md border bg-muted/50 px-2 py-1.5"
                >
                  <Mail className="mt-0.5 h-3.5 w-3.5 text-muted-foreground" />

                  <div className="min-w-0 flex-1">
                    <div className="mb-0.5 flex items-center gap-1.5">
                      <p className="text-[10px] uppercase text-muted-foreground">
                        Email
                      </p>
                    </div>

                    <div className="flex justify-between items-center">
                      <p className="truncate text-xs font-medium">{e.email}</p>
                      {e.label && (
                        <span className="rounded-xs bg-purple-600 px-3 py-1 mb-2 me-4 text-[9px] font-semibold uppercase tracking-wide text-white dark:bg-purple-500/20 dark:text-purple-300">
                          {e.label}
                        </span>
                      )}
                    </div>
                  </div>
                </div>
              ))}

              {/* Phones */}
              {contact.phones?.map((p) => (
                <div
                  key={p.id}
                  className="flex items-start gap-2 rounded-md border bg-muted/50 px-2 py-1.5"
                >
                  <Phone className="mt-0.5 h-3.5 w-3.5 text-muted-foreground" />

                  <div className="min-w-0 flex-1">
                    <div className="mb-0.5 flex items-center gap-1.5">
                      <p className="text-[10px] uppercase text-muted-foreground">
                        Contact
                      </p>
                    </div>

                    <div className="flex justify-between items-center">
                      <p className="truncate text-xs font-medium">{p.phone}</p>

                      {p.label && (
                        <span className="rounded-xs bg-purple-600 px-3 py-1 mb-2 me-4 text-[9px] font-semibold uppercase tracking-wide text-white dark:bg-purple-500/20 dark:text-purple-300">
                          {p.label}
                        </span>
                      )}
                    </div>
                  </div>
                </div>
              ))}

              {/* LinkedIn */}
              {contact.linkedinUrl && (
                <div className="flex items-start gap-2 rounded-md bg-muted/50 border px-2 py-1.5">
                  <FaLinkedin className="w-3.5 h-3.5 mt-0.5 text-muted-foreground" />
                  <div>
                    <p className="text-[10px] uppercase text-muted-foreground mb-0.5">
                      LinkedIn
                    </p>
                    <a
                      href={contact.linkedinUrl}
                      target="_blank"
                      rel="noreferrer"
                      className="text-xs font-medium text-blue-500 underline"
                    >
                      Open profile
                    </a>
                  </div>
                </div>
              )}

              {/* Instagram */}
              {contact.instagramUrl && (
                <div className="flex items-start gap-2 rounded-md bg-muted/50 border px-2 py-1.5">
                  <FaInstagram className="w-3.5 h-3.5 mt-0.5 text-muted-foreground" />
                  <div>
                    <p className="text-[10px] uppercase text-muted-foreground mb-0.5">
                      Instagram
                    </p>
                    <a
                      href={contact.instagramUrl}
                      target="_blank"
                      rel="noreferrer"
                      className="text-xs font-medium text-pink-500 underline"
                    >
                      Open profile
                    </a>
                  </div>
                </div>
              )}

              {/* Facebook */}
              {contact.facebookUrl && (
                <div className="flex items-start gap-2 rounded-md bg-muted/50 border px-2 py-1.5">
                  <FaFacebookSquare className="w-3.5 h-3.5 mt-0.5 text-muted-foreground" />
                  <div>
                    <p className="text-[10px] uppercase text-muted-foreground mb-0.5">
                      Facebook
                    </p>
                    <a
                      href={contact.facebookUrl}
                      target="_blank"
                      rel="noreferrer"
                      className="text-xs font-medium text-blue-600 underline"
                    >
                      Open profile
                    </a>
                  </div>
                </div>
              )}
            </div>
          </div>
        )}

        {/* Footer */}
        <div className="flex justify-end mt-4">
          <Button variant="outline" onClick={() => setActiveDialog(null)}>
            Close
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default ContactInfoModal;
