import dayjs from 'dayjs/esm';
import { IClient } from 'app/entities/client/client.model';
import { TypeProjet } from 'app/entities/enumerations/type-projet.model';
import { Etape } from 'app/entities/enumerations/etape.model';
import {IDossier} from "../dossier/dossier.model";
import {IUser} from "../user/user.model";

export interface IProjet {
  id?: number;
  intitule?: string | null;
  descriptionProjet?: string | null;
  cheminP?: string | null;
  type?: TypeProjet | null;
  etape?: Etape | null;
  dateCreation?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  client?: IClient | null;
  user?: IUser | null,

  dossier?: IDossier[] | null;
}

export class Projet implements IProjet {
  constructor(
    public id?: number,
    public intitule?: string | null,
    public descriptionProjet?: string | null,
    public cheminP?: string | null,
    public type?: TypeProjet | null,
    public etape?: Etape | null,
    public dateCreation?: dayjs.Dayjs | null,
    public dateFin?: dayjs.Dayjs | null,
    public client?: IClient | null,
    public dossier?: IDossier[] | null,
    public user?: IUser | null,

) {}
}

export function getProjetIdentifier(projet: IProjet): number | undefined {
  return projet.id;
}
