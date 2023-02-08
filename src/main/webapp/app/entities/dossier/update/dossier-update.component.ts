import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDossier, Dossier } from '../dossier.model';
import { DossierService } from '../service/dossier.service';
import {IProjet} from "../../projet/projet.model";

@Component({
  selector: 'jhi-dossier-update',
  templateUrl: './dossier-update.component.html',
})
export class DossierUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomD: [],
    cheminD: [],
    projet_id: [],
  });

  editFormProjet = this.fb.group({
    id: [],
    nomP: [],

  });

  constructor(protected dossierService: DossierService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dossier }) => {
      this.updateForm(dossier);
    });
    this.activatedRoute.data.subscribe(({ projet }) => {
      this.updateFormProjet(projet);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dossier = this.createFromForm();
    if (dossier.id !== undefined) {
      this.subscribeToSaveResponse(this.dossierService.update(dossier));
    } else {
      this.subscribeToSaveResponse(this.dossierService.create(dossier));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDossier>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(dossier: IDossier): void {
    this.editForm.patchValue({
      id: dossier.id,
      cheminD: dossier.cheminD,
    });
  }

  protected updateFormProjet(projet: IProjet): void {
    this.editFormProjet.patchValue({
      id: projet.id,
      nomP: projet.intitule,
    });
  }





  protected createFromForm(): IDossier {
    return {
      ...new Dossier(),
      id: this.editForm.get(['id'])!.value,
      cheminD: this.editForm.get(['cheminD'])!.value,
      projet: this.editFormProjet.get(['id'])!.value   };
  }
}
