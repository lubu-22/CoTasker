import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse,HttpRequest,HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFichier, getFichierIdentifier } from '../fichier.model';

export type EntityResponseType = HttpResponse<IFichier>;
export type EntityArrayResponseType = HttpResponse<IFichier[]>;

@Injectable({ providedIn: 'root' })
export class FichierService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fichiers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fichier: IFichier): Observable<EntityResponseType> {
    return this.http.post<IFichier>(this.resourceUrl, fichier, { observe: 'response' });
  }


  upload(file: File, id: number): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
   // return this.http.post<FormData>(`${this.resourceUrl}/upload/${id}`, formData, { observe: 'response' });
   const req = new HttpRequest('POST', `${this.resourceUrl}/upload/${id}`, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }








  update(fichier: IFichier): Observable<EntityResponseType> {
    return this.http.put<IFichier>(`${this.resourceUrl}/${getFichierIdentifier(fichier) as number}`, fichier, { observe: 'response' });
  }

  partialUpdate(fichier: IFichier): Observable<EntityResponseType> {
    return this.http.patch<IFichier>(`${this.resourceUrl}/${getFichierIdentifier(fichier) as number}`, fichier, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFichier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFichier[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }


  finddocument(id: number): Observable<Blob> {

    return this.http.get(`${this.resourceUrl}/ouvrir-fichier/${id}`, {responseType: "blob"});

  }

  addFichierToCollectionIfMissing(fichierCollection: IFichier[], ...fichiersToCheck: (IFichier | null | undefined)[]): IFichier[] {
    const fichiers: IFichier[] = fichiersToCheck.filter(isPresent);
    if (fichiers.length > 0) {
      const fichierCollectionIdentifiers = fichierCollection.map(fichierItem => getFichierIdentifier(fichierItem)!);
      const fichiersToAdd = fichiers.filter(fichierItem => {
        const fichierIdentifier = getFichierIdentifier(fichierItem);
        if (fichierIdentifier == null || fichierCollectionIdentifiers.includes(fichierIdentifier)) {
          return false;
        }
        fichierCollectionIdentifiers.push(fichierIdentifier);
        return true;
      });
      return [...fichiersToAdd, ...fichierCollection];
    }
    return fichierCollection;
  }
}
