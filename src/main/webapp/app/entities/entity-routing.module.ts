import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'client',
        data: { pageTitle: 'gestionApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'dossier',
        data: { pageTitle: 'gestionApp.dossier.home.title' },
        loadChildren: () => import('./dossier/dossier.module').then(m => m.DossierModule),
      },
      {
        path: 'fichier',
        data: { pageTitle: 'gestionApp.fichier.home.title' },
        loadChildren: () => import('./fichier/fichier.module').then(m => m.FichierModule),
      },
      {
        path: 'tache',
        data: { pageTitle: 'gestionApp.tache.home.title' },
        loadChildren: () => import('./tache/tache.module').then(m => m.TacheModule),
      },
      {
        path: 'projet',
        data: { pageTitle: 'gestionApp.projet.home.title' },
        loadChildren: () => import('./projet/projet.module').then(m => m.ProjetModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
