import { Component, OnInit } from '@angular/core';
import {Observable} from "rxjs";
import {IDossier} from "../../dossier/dossier.model";
import {FichierService} from "../service/fichier.service";
import {DossierService} from "../../dossier/service/dossier.service";
import {ActivatedRoute} from "@angular/router";
import {FormBuilder} from "@angular/forms";
import {HttpEventType, HttpResponse} from "@angular/common/http";
import {Fichier, IFichier} from "../fichier.model";
import {finalize, map} from "rxjs/operators";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-newfichier',
  templateUrl: './newfichier.component.html',
  styleUrls: ['./newfichier.component.scss']
})
export class NewfichierComponent implements OnInit {
  isSaving = false;
  id?: number;
  selectedFiles?: FileList;
  currentFile?: File;
  progress = 0;
  message = '';
dossier?: IDossier;
  fileInfos?: Observable<any>;


  editForm = this.fb.group({
    id: [],
    nomF: [],
    cheminF: [],
    codeF: [],
    dossier: [],
    input: [],
  });

  constructor(private activeModal: NgbActiveModal,
    protected fichierService: FichierService,
    protected dossierService: DossierService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
   console.warn("initilaisation")
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fichier = this.createFromForm();

      this.subscribeToSaveResponse(this.fichierService.create(fichier));
      this.upload(this.dossier!.id!);

  }

  selectFile(event: any): void {
    this.selectedFiles = event.target.files;
  }


  upload(id: number): void {
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
  }


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

  }



  protected createFromForm(): IFichier {
    return {
      ...new Fichier(),
      id: this.editForm.get(['id'])!.value,
      nomF: this.selectedFiles?.item(0)?.name,
      cheminF: this.editForm.get(['cheminF'])!.value,
      codeF: this.editForm.get(['codeF'])!.value,
      dossier: this.dossier,
    };
  }
}
