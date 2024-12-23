import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDossier, getDossierIdentifier } from '../dossier.model';
import { IFichier } from 'app/entities/fichier/fichier.model';

export type EntityResponseType = HttpResponse<IDossier>;
export type EntityArrayResponseType = HttpResponse<IDossier[]>;
export type fichierResponse = HttpResponse<IFichier[]>;
export type fichierResponses = HttpResponse<IFichier>;

@Injectable({ providedIn: 'root' })
export class DossierService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dossiers');
  protected urlFichiers = this.applicationConfigService.getEndpointFor('api/fichierdedossiers');
  protected urlouvrirfichier = this.applicationConfigService.getEndpointFor('api/ouvrirfichier');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dossier: IDossier): Observable<EntityResponseType> {
    return this.http.post<IDossier>(this.resourceUrl, dossier, { observe: 'response' });
  }

  update(dossier: IDossier): Observable<EntityResponseType> {
    return this.http.put<IDossier>(`${this.resourceUrl}/${getDossierIdentifier(dossier) as number}`, dossier, { observe: 'response' });
  }

  partialUpdate(dossier: IDossier): Observable<EntityResponseType> {
    return this.http.patch<IDossier>(`${this.resourceUrl}/${getDossierIdentifier(dossier) as number}`, dossier, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDossier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDossier[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDossierToCollectionIfMissing(dossierCollection: IDossier[], ...dossiersToCheck: (IDossier | null | undefined)[]): IDossier[] {
    const dossiers: IDossier[] = dossiersToCheck.filter(isPresent);
    if (dossiers.length > 0) {
      const dossierCollectionIdentifiers = dossierCollection.map(dossierItem => getDossierIdentifier(dossierItem)!);
      const dossiersToAdd = dossiers.filter(dossierItem => {
        const dossierIdentifier = getDossierIdentifier(dossierItem);
        if (dossierIdentifier == null || dossierCollectionIdentifiers.includes(dossierIdentifier)) {
          return false;
        }
        dossierCollectionIdentifiers.push(dossierIdentifier);
        return true;
      });
      return [...dossiersToAdd, ...dossierCollection];
    }
    return dossierCollection;
  }

  fichierdedossier(id: number): Observable<HttpResponse<IFichier[]>> {

    return this.http.get<IFichier[]>(`${this.resourceUrl}/fichier/${id}`, { observe: 'response' });

  }

  finddocument(id: number): Observable<Blob> {

    return this.http.get(`${this.resourceUrl}/ouvrir-fichier/${id}`, {responseType: "blob"});

  }
}
