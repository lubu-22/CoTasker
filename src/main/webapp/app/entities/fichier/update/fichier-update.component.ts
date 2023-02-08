import { Component, OnInit } from '@angular/core';
import {HttpEventType, HttpResponse} from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFichier, Fichier } from '../fichier.model';
import { FichierService } from '../service/fichier.service';
import { IDossier } from 'app/entities/dossier/dossier.model';
import { DossierService } from 'app/entities/dossier/service/dossier.service';

@Component({
  selector: 'jhi-fichier-update',
  templateUrl: './fichier-update.component.html',
})
export class FichierUpdateComponent implements OnInit {
  isSaving = false;

  selectedFiles?: FileList;
  currentFile?: File;
  progress = 0;
  message = '';

  fileInfos?: Observable<any>;



  dossiersSharedCollection: IDossier[] = [];

  editForm = this.fb.group({
    id: [],
    nomF: [],
    cheminF: [],
    codeF: [],
    dossier: [],
  });

  constructor(
    protected fichierService: FichierService,
    protected dossierService: DossierService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fichier }) => {
      this.updateForm(fichier);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fichier = this.createFromForm();
    if (fichier.id !== undefined) {
      this.subscribeToSaveResponse(this.fichierService.update(fichier));
    } else {
      this.subscribeToSaveResponse(this.fichierService.create(fichier));
      //this.upload(1);
    }
  }

  selectFile(event: any): void {
    this.selectedFiles = event.target.files;
  }


  /*upload(): void {
    this.progress = 0;

    if (this.selectedFiles) {
      const file: File | null = this.selectedFiles.item(0);

      if (file) {
        this.currentFile = file;

        this.fichierService.upload(this.currentFile,id).subscribe({
          next: (event: any) => {
            if (event.type === HttpEventType.UploadProgress) {
              this.progress = Math.round(100 * event.loaded / event.total);
            } else if (event instanceof HttpResponse) {
              this.message = event.body.message;
            }
          },
          error: (err: any) => {
            console.warn(err);
            this.progress = 0;


            this.currentFile = undefined;
          }
        });
      }

      this.selectedFiles = undefined;
    }
  }*/


  trackDossierById(_index: number, item: IDossier): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFichier>>): void {
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

  protected updateForm(fichier: IFichier): void {
    this.editForm.patchValue({
      id: fichier.id,
      nomF: fichier.nomF,
      cheminF: fichier.cheminF,
      codeF: fichier.codeF,
      dossier: fichier.dossier,
    });

    this.dossiersSharedCollection = this.dossierService.addDossierToCollectionIfMissing(this.dossiersSharedCollection, fichier.dossier);
  }

  protected loadRelationshipsOptions(): void {
    this.dossierService
      .query()
      .pipe(map((res: HttpResponse<IDossier[]>) => res.body ?? []))
      .pipe(
        map((dossiers: IDossier[]) => this.dossierService.addDossierToCollectionIfMissing(dossiers, this.editForm.get('dossier')!.value))
      )
      .subscribe((dossiers: IDossier[]) => (this.dossiersSharedCollection = dossiers));
  }

  protected createFromForm(): IFichier {
    return {
      ...new Fichier(),
      id: this.editForm.get(['id'])!.value,
      nomF: this.editForm.get(['nomF'])!.value,
      cheminF: this.editForm.get(['cheminF'])!.value,
      codeF: this.editForm.get(['codeF'])!.value,
      dossier: this.editForm.get(['dossier'])!.value,
    };
  }
}
