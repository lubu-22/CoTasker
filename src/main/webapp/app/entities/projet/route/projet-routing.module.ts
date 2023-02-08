import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProjetComponent } from '../list/projet.component';
import { ProjetDetailComponent } from '../detail/projet-detail.component';
import { ProjetUpdateComponent } from '../update/projet-update.component';
import { ProjetRoutingResolveService } from './projet-routing-resolve.service';
import {NewdossierComponent} from '../newdossier/newdossier.component';
import {TachedeprojetResolver} from "./tachedeprojet.resolver";
import {ListetacheComponent} from "../listetache/listetache.component";
import {ListeprojetpersonneComponent} from "../listeprojetpersonne/listeprojetpersonne.component";
import {ListeprojetuserResolver} from "./listeprojetuser.resolver";
import {NewtacheComponent} from "../newtache/newtache.component";
import {ChefdeprojetComponent} from "../chefdeprojet/chefdeprojet.component";


const projetRoute: Routes = [
  {
    path: '',
    component: ProjetComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProjetDetailComponent,
    resolve: {
      projet: ProjetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProjetUpdateComponent,
    resolve: {
      projet: ProjetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/newdossier',
    component: NewdossierComponent,
    resolve: {
      projet: ProjetRoutingResolveService,
    }
  },
  {
    path: ':id/projetUser',
    component: ListeprojetpersonneComponent,
    resolve: {
      taches: ListeprojetuserResolver,
    }
  },
  {
    path: ':id/chefdeprojet',
    component: ChefdeprojetComponent,
    resolve: {
      projet: ProjetRoutingResolveService,
    }
  },
  {
    path: ':id/tachedeprojet',
    component: ListetacheComponent,
    resolve: {
      taches: TachedeprojetResolver,
    }
  },



  {
    path: ':id/newtache',
    component: NewtacheComponent,
    resolve: {
      projet: ProjetRoutingResolveService,
    }
  },




  {
    path: ':id/edit',
    component: ProjetUpdateComponent,
    resolve: {
      projet: ProjetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(projetRoute)],
  exports: [RouterModule],
})
export class ProjetRoutingModule {}
