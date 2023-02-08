import { IDossier } from 'app/entities/dossier/dossier.model';

export interface IFichier {
  id?: number;
  nomF?: string | null;
  cheminF?: string | null;
  codeF?: string | null;
  dossier?: IDossier | null;
}

export class Fichier implements IFichier {
  constructor(
    public id?: number,
    public nomF?: string | null,
    public cheminF?: string | null,
    public codeF?: string | null,
    public dossier?: IDossier | null
  ) {}
}

export function getFichierIdentifier(fichier: IFichier): number | undefined {
  return fichier.id;
}
