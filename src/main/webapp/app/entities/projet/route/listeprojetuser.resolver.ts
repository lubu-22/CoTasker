import { Injectable } from '@angular/core';
import {
  Router, Resolve,
  ActivatedRouteSnapshot
} from '@angular/router';
import {EMPTY, Observable, of} from 'rxjs';
import {IProjet, Projet} from "../projet.model";
import {mergeMap} from "rxjs/operators";
import {HttpResponse} from "@angular/common/http";
import {ProjetService} from "../service/projet.service";
import {ITache, Tache} from "../../tache/tache.model";

@Injectable({
  providedIn: 'root'
})
export class ListeprojetuserResolver implements Resolve<IProjet[]> {

  constructor(protected service: ProjetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITache[]> | Observable<never> {
    const id = route.params['id'];

      return this.service.projetdeuser(id).pipe(
        mergeMap((taches: HttpResponse<Projet[]>) => {
          if (taches.body) {
            return of(taches.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }

  }

