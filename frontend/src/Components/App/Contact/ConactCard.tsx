import { Info, Pencil, Trash2, Link, Eye } from "lucide-react";
import { FaFacebookSquare } from "react-icons/fa";
import { IoLogoLinkedin } from "react-icons/io5";
import { FaInstagram } from "react-icons/fa";
import type { Contact } from "@/types/contactTypes";

type Props = {
  contact: Contact;
  onDelete: (id: number) => void;
  onInfo: (id: number) => void;
  onUpdate: (id: number) => void;
};

const ContactCard = ({ contact, onDelete, onInfo, onUpdate }: Props) => {


  return (
    <div className="bg-white rounded-xl shadow-lg p-4 w-full  border border-gray-200 select-none">
      {/* Header */}
      <div className="flex items-start justify-between mb-4">
        <div className="flex items-center gap-3">
          {/* Avatar */}
          <div className="w-12 h-12 rounded-full bg-blue-100 flex items-center justify-center text-blue-700 font-semibold text-sm border-2 border-gray-200">
            {contact.firstName[0]}
          </div>

          <div>
            <h2 className="text-base font-semibold text-gray-900">{`${contact.firstName}  ${contact?.lastName}`}</h2>

            {contact.linkedUserId && (
              <span className="inline-flex items-center px-2 py-0.5 rounded-full text-[10px] font-medium bg-green-600 text-white mt-1">
                App-User Contact
              </span>
            )}

            {!contact.linkedUserId && (
              <span className="inline-flex items-center px-2 py-0.5 rounded-full text-[10px] font-medium bg-red-600 text-white mt-1">
                Non-App User
              </span>
            )}
          </div>
        </div>

        <button
          className="p-1.5 hover:bg-gray-100 rounded-lg transition-colors"
          title="Copy profile link"
        >
          <Link className="w-4 h-4 text-gray-600" />
        </button>
      </div>

      {/* Mutual + Social */}
      <div className="mb-4 flex items-center justify-between">
        <div className="flex items-center">
          {/* Avatar 1 */}
          <img
            src="https://i.pravatar.cc/150?img=1"
            className="w-6 h-6 rounded-full border border-white object-cover"
          />

          {/* Avatar 2 (overlapping) */}
          <img
            src="https://i.pravatar.cc/150?img=2"
            className="-ml-2 w-6 h-6 rounded-full border border-white object-cover"
          />

          {/* View more (eye button) */}
          <button
            className="-ml-2 w-6 h-6 flex items-center justify-center rounded-full bg-gray-100 hover:bg-gray-200 border border-white transition-colors"
            title="View mutual contacts"
          >
            <Eye className="w-3.5 h-3.5 text-gray-600" />
          </button>

          <p className="text-sm font-bold ms-3">3 Mutual Contacts</p>
        </div>

        <div className="flex items-center">
          {contact.facebookUrl && (
            <a
              href={contact.facebookUrl}
              className="p-1.5 rounded-md hover:bg-blue-50 transition-colors group"
              target="_blank"
              rel="noopener noreferrer"
            >
              <FaFacebookSquare className="w-5 h-5 text-blue-600 group-hover:scale-110 transition-transform" />
            </a>
          )}


          {contact.linkedinUrl && (
            <a
              href={contact.linkedinUrl}
              className="p-1.5 rounded-md hover:bg-blue-50 transition-colors group"
              target="_blank"
              rel="noopener noreferrer"
            >
              <IoLogoLinkedin className="w-5 h-5 text-blue-700 group-hover:scale-110 transition-transform" />
            </a>
          )}

          {contact.instagramUrl && (
            <a
              href={contact.instagramUrl}
              className="p-1.5 rounded-md hover:bg-pink-50 transition-colors group"
              target="_blank"
              rel="noopener noreferrer"
            >
              <FaInstagram className="w-5 h-5 text-pink-600 group-hover:scale-110 transition-transform" />
            </a>
          )}
        </div>
      </div>

      {/* Action buttons */}
      <div className="flex gap-2">
        <button 
        onClick={() => {
          onInfo(contact.contactId);
        }}
         className="flex-1 flex items-center justify-center gap-1 px-3 py-2 bg-blue-500 hover:bg-blue-600 text-white rounded-lg text-sm">
          <Info className="w-4 h-4" />
          Info
        </button>

        <button
         onClick={() => {
          onUpdate(contact.contactId);
        }}
         className="flex-1 flex items-center justify-center gap-1 px-3 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-lg text-sm">
          <Pencil className="w-4 h-4" />
          Update
        </button>

        <button
          onClick={() => {
            onDelete(contact.contactId);
          }}
          className="flex-1 flex items-center justify-center gap-1 px-3 py-2 bg-red-500 hover:bg-red-600 text-white rounded-lg text-sm"
        >
          <Trash2 className="w-4 h-4" />
          Delete
        </button>
      </div>
    </div>
  );
};

export default ContactCard;
