import { IFichier } from 'app/entities/fichier/fichier.model';
import {IProjet} from "../projet/projet.model";

export interface IDossier {
  id?: number;
  nomD?: string | null;
  cheminD?: string | null;
  projet?: IProjet | null;
  fichiers?: IFichier[] | null;


}

export class Dossier implements IDossier {
  constructor(public id?: number, public nomD?: string | null, public cheminD?: string | null, public fichiers?: IFichier[] | null,    public projet?: IProjet | null
  ) {}
}

export function getDossierIdentifier(dossier: IDossier): number | undefined {
  return dossier.id;
}
