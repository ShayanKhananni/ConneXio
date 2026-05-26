
export interface Email {
  id: number
  email: string
  label: "HOME" | "WORK"
}

export interface Phone {
  id: number
  phone: string
  label: "HOME" | "WORK"
}

// User for getting only specific data for showing contact list, not all details
export interface ContatcInfo
{
  contactId: number
  firstName: string
  lastName: string

  linkedinUrl?: string
  instagramUrl?: string
  profileImageUrl?: string
  facebookUrl?: string
}

export interface Contact {
  contactId: number
  firstName: string
  lastName: string

  linkedinUrl?: string
  instagramUrl?: string
  profileImageUrl?: string
  facebookUrl?: string
  linkedUserId?: number

  emails?: Email[]
  phones?: Phone[]
}



