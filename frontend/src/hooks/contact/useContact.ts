import { addContactApi, deletContactApi, getContactDetails, getContacts, getPagedContacts, updateContactApi } from "@/api/contactApi"
import type { ContatcInfo } from "@/types/contactTypes";
import type { CreateContactForm, UpdateContactForm } from "@/types/UpdateContactType"
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"


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
    },
  });
};

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


      // 1. update detail page cache
      queryClient.setQueryData(
        ["contact", id],
        updatedContact
      );

      // 2. invalidate all list caches
      queryClient.invalidateQueries({
        queryKey: ["contacts"],
      });
    },
  });
};