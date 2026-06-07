import { addBacthContactsApi, addContactApi, deletContactApi, getContacts, getPagedContacts, updateContactApi } from "@/api/contactApi"
import type { ContatcInfo } from "@/types/contactTypes";
import type { UpdateContactForm } from "@/zod/contactSchema"
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { toast } from "react-toastify";


export const useContacts = () => {
  return useQuery<ContatcInfo[]>({
    queryKey: ["contacts"],
    queryFn: getContacts,
  });
}


export const usePagedContacts = (
  pageNumber: number,
  pageSize: number,
  search: string
) => {
  return useQuery({
    queryKey: [
      "contacts",
      pageNumber,
      pageSize,
      search,
    ],

    queryFn: () =>
      getPagedContacts(
        pageNumber,
        pageSize,
        search
      ),

    placeholderData: (prev) => prev,
    staleTime: 1000 * 60 * 5, 

    enabled:
      search.length === 0 ||
      search.length >= 3,
  });
};


export const useAddContact = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: addContactApi,

    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["contacts"],
      });
      toast.success("Contact Added Successfully");
    },
  });
};

export const useAddBatchContacts = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: addBacthContactsApi, 

    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["contacts"],
      });
      toast.success("Contacts Imported Successfully");
    },
  });
};

export const useDeleteContact = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: deletContactApi,

    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["contacts"],
      });
      toast.success("Contact Deleted Successfully");
    },
  });
};


export const useContactDetails = (
  id: number | null,
  enabled: boolean
) => {
  const queryClient = useQueryClient();

  return useQuery({
    queryKey: ["contact", id],

    queryFn: async () => {
      const cachedPages = queryClient.getQueriesData({
        queryKey: ["contacts"],
      });

      for (const [, data] of cachedPages) {
        const contact = (data as any)?.content?.find(
          (c: any) => c.contactId === id
        );

        if (contact) {
          return contact;
        }
      }

      throw new Error("Contact not found");
    },

    enabled: !!id && enabled,
    staleTime: Infinity,
  });
};


export const useUpdateContact = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: UpdateContactForm }) =>
      updateContactApi(id, data),

    onSuccess: (updatedContact, variables) => {
      const id = variables.id;

      
      queryClient.setQueryData(
        ["contact", id],
        updatedContact
      );

      queryClient.invalidateQueries({
        queryKey: ["contacts"],
      });

      toast.success("Contacts Updated Successfully");
    },
  });
};