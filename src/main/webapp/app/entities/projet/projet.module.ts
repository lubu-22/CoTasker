import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProjetComponent } from './list/projet.component';
import { ProjetDetailComponent } from './detail/projet-detail.component';
import { ProjetUpdateComponent } from './update/projet-update.component';
import { ProjetDeleteDialogComponent } from './delete/projet-delete-dialog.component';
import { ProjetRoutingModule } from './route/projet-routing.module';
import { NewdossierComponent } from './newdossier/newdossier.component';
import { ListetacheComponent } from './listetache/listetache.component';
import { ListeprojetpersonneComponent } from './listeprojetpersonne/listeprojetpersonne.component';
import { NewtacheComponent } from './newtache/newtache.component';
import { ChefdeprojetComponent } from './chefdeprojet/chefdeprojet.component';

@NgModule({
  imports: [SharedModule, ProjetRoutingModule],
  declarations: [ProjetComponent, ProjetDetailComponent, ProjetUpdateComponent, ProjetDeleteDialogComponent, NewdossierComponent, ListetacheComponent, ListeprojetpersonneComponent, NewtacheComponent, ChefdeprojetComponent],
  entryComponents: [ProjetDeleteDialogComponent],
})
export class ProjetModule {}
