import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FichierComponent } from './list/fichier.component';
import { FichierDetailComponent } from './detail/fichier-detail.component';
import { FichierUpdateComponent } from './update/fichier-update.component';
import { FichierDeleteDialogComponent } from './delete/fichier-delete-dialog.component';
import { FichierRoutingModule } from './route/fichier-routing.module';
import { NewfichierComponent } from './newfichier/newfichier.component';

@NgModule({
  imports: [SharedModule, FichierRoutingModule],
  declarations: [FichierComponent, FichierDetailComponent, FichierUpdateComponent, FichierDeleteDialogComponent, NewfichierComponent],
  entryComponents: [FichierDeleteDialogComponent],
})
export class FichierModule {}
