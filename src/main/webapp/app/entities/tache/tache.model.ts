import { Role } from 'app/entities/enumerations/role.model';
import {IProjet} from "../projet/projet.model";
import {IUser} from "../user/user.model";

export interface ITache {
  id?: number;
  role?: Role | null;
  descriptiontache?: string | null;
  projet?: IProjet | null;
  user?: IUser | null ;
}

export class Tache implements ITache {
  constructor(public id?: number, public role?: Role | null, public descriptiontache?: string | null,public projet?: IProjet | null,public user?: IUser | null) {}
}

export function getTacheIdentifier(tache: ITache): number | undefined {
  return tache.id;
}
