import type { Contact, ContatcInfo } from "@/types/contactTypes"
import api from "./axiosInstance"
import type { CreateContactForm, UpdateContactForm } from "@/types/UpdateContactType"


export const getContacts = async (): Promise<ContatcInfo[]> => {
  const response = await api.get(`/contact`)
  return response.data
}

export const getContactDetails = async (id:number): Promise<Contact> => {
  const response = await api.get(`/contact/${id}`)
  return response.data
}

export const deletContactApi = async (id:number)  => {
  const response = await api.delete(`/contact/${id}`)
  return response.data
}

export const updateContactApi = async (
  id: number,
  contact: UpdateContactForm
) => {
  const response = await api.patch(`/contact/${id}`, contact);
  return response.data;
};


export const addContactApi = async (
  contact: CreateContactForm
) => {
  const response = await api.post(`/contact`,contact);
  return response.data;
};


export const getPagedContacts = async (pageNum: number, pageSize: number, search: String)  => {
  const res = await api.get(
    `/contact/paged?pageNum=${pageNum}&pageSize=${pageSize}&search=${search}`
  );
  return res.data;
};

