import { addContactApi, deletContactApi, getContactDetails, getContacts, updateContactApi } from "@/api/contactApi"
import type { ContatcInfo } from "@/types/contactTypes";
import type { CreateContactForm, UpdateContactForm } from "@/types/UpdateContactType"
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"



export const useContacts = () => {
  return useQuery<ContatcInfo[]>({
    queryKey: ["contacts"],
    queryFn: getContacts,
  });
}




export const useAddContact = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: (data: any) => addContactApi(data),
    onSuccess: (newContact) => {
      queryClient.setQueryData<ContatcInfo[]>(
        ["contacts"],
        (old = []) => {
          return [newContact, ...old];
        }
      );
    },
  });
};



// Getting Contact-Details for Specific Contact
export const useDeleteContact = () => {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: deletContactApi,
    
    onSuccess: (_, deletedId) => {
      queryClient.setQueryData(["contacts"], (old: any) => {
        return old?.filter((c: any) => c.contactId !== deletedId)
      })
    },

  })
}

export const useContactDetails = (
  id: number | null,
  enabled: boolean
) => {
  return useQuery({
    queryKey: ["contact", id],
    queryFn: () => getContactDetails(id as number),
    enabled: !!id && enabled,

    
    staleTime: 1000 * 60 * 5, 
    gcTime: 1000 * 60 * 30,   

    refetchOnMount: false,
    refetchOnWindowFocus: false,
    refetchOnReconnect: false,
  });
};


export const useUpdateContact = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: UpdateContactForm }) =>
      updateContactApi(id, data),

    onSuccess: (updatedContact, variables) => {
      const id = variables.id;

      // 1. Update detail cache
      queryClient.setQueryData(["contact", id], updatedContact);


      // 2. Update list cache
      queryClient.setQueryData(["contacts"], (old : ContatcInfo[] ) => {
        if (!old) return old;
      
        return old.map((c) => {
          if (c.contactId !== id) return c;
      
          return {
            ...c,
            firstName: updatedContact.firstName,
            lastName: updatedContact.lastName,
            linkedinUrl: updatedContact.linkedinUrl,
            instagramUrl: updatedContact.instagramUrl,
            facebookUrl: updatedContact.facebookUrl,
            profileImageUrl: updatedContact.profileImageUrl,
          };
        });
      });

    },
  });
};