export interface IClient {
  id?: number;
  nomC?: string | null;
  adresse?: string | null;
  numero?: number | null;
  email?: string | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public nomC?: string | null,
    public adresse?: string | null,
    public numero?: number | null,
    public email?: string | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
