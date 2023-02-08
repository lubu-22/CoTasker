import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';

import {
  Router, Resolve,
  ActivatedRouteSnapshot
} from '@angular/router';
import { Observable, of ,EMPTY } from 'rxjs';
import {Dossier, IDossier} from "../../dossier/dossier.model";
import { mergeMap } from 'rxjs/operators';
import {ProjetService} from "../service/projet.service";



@Injectable({
  providedIn: 'root'
})
export class DossierprojetResoleResolver implements Resolve<IDossier> {
  constructor(protected service: ProjetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDossier> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dossier: HttpResponse<Dossier>) => {
          if (dossier.body) {
            return of(dossier.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }   return of(new Dossier());
  }
}
