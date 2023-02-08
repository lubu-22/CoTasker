import {IDossier} from "../dossier/dossier.model";
import {IFichier} from "./fichier.model";

export interface IFichierfile {
  id?: number;
  fichier?: IFichier | null;
  file?: File | null;

}

export class Fichierfile implements IFichier {
  constructor(
    public id?: number,
    public fichier?: IFichier | null,
    public file?: File | null
  ) {}
}
