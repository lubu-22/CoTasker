import { Injectable } from '@angular/core';
import {
  Router, Resolve,
  ActivatedRouteSnapshot
} from '@angular/router';
import {EMPTY, Observable, of} from 'rxjs';
import {ITache, Tache} from "../../tache/tache.model";
import {ProjetService} from "../service/projet.service";
import {mergeMap} from "rxjs/operators";
import {HttpResponse} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class TachedeprojetResolver implements Resolve<ITache[]> {
  constructor(protected service: ProjetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITache[]> | Observable<never> {
    const id = route.params['id'];
      return this.service.tachedeprojet(id).pipe(
        mergeMap((taches: HttpResponse<Tache[]>) => {
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

