import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import {Dossier, IDossier} from "../../dossier/dossier.model";
import {DossierService} from "../../dossier/service/dossier.service";

@Injectable({ providedIn: 'root' })
export class NewfichierResolver implements Resolve<IDossier> {
  constructor(protected service: DossierService, protected router: Router) {}

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
    }
    return of(new Dossier());
  }
}
