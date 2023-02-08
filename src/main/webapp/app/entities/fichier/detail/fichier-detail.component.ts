import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFichier } from '../fichier.model';

@Component({
  selector: 'jhi-fichier-detail',
  templateUrl: './fichier-detail.component.html',
})
export class FichierDetailComponent implements OnInit {
  fichier: IFichier | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fichier }) => {
      this.fichier = fichier;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
