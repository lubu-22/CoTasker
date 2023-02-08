import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FichierComponent } from '../list/fichier.component';
import { FichierDetailComponent } from '../detail/fichier-detail.component';
import { FichierUpdateComponent } from '../update/fichier-update.component';
import { FichierRoutingResolveService } from './fichier-routing-resolve.service';
import {NewfichierComponent} from "../newfichier/newfichier.component";
import {NewfichierResolver} from "../newfichier/newfichier.resolver";

const fichierRoute: Routes = [
  {
    path: '',
    component: FichierComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FichierDetailComponent,
    resolve: {
      fichier: FichierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FichierUpdateComponent,
    resolve: {
      fichier: FichierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: ':id/newfichier',
    component: NewfichierComponent,
    resolve: {
      dossier: NewfichierResolver,
    },
    canActivate: [UserRouteAccessService],
  },








  {
    path: ':id/edit',
    component: FichierUpdateComponent,
    resolve: {
      fichier: FichierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fichierRoute)],
  exports: [RouterModule],
})
export class FichierRoutingModule {}
