import { Component, OnInit } from '@angular/core';
import {DossierService} from "../../dossier/service/dossier.service";
import {ActivatedRoute} from "@angular/router";
import {FormBuilder} from "@angular/forms";
import {Observable} from "rxjs";
import {HttpResponse} from "@angular/common/http";
import {Dossier, IDossier} from "../../dossier/dossier.model";
import {finalize} from "rxjs/operators";
import {IProjet} from "../projet.model";

@Component({
  selector: 'jhi-newdossier',
  templateUrl: './newdossier.component.html',
  styleUrls: ['./newdossier.component.scss']
})
export class NewdossierComponent implements OnInit {

  isSaving = false;


  editForm = this.fb.group({
    id: [],
    nomD: [],
    cheminD: [],
    projet: [],
  });

  projet?: IProjet;




  constructor(protected dossierService: DossierService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {

    this.activatedRoute.data.subscribe(({ projet }) => {
      this.projet=projet;

    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dossier = this.createFromForm();
      this.subscribeToSaveResponse(this.dossierService.create(dossier));

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









  protected createFromForm(): IDossier {
    return {
      ...new Dossier(),
      id: this.editForm.get(['id'])!.value,
      nomD: this.editForm.get(['nomD'])!.value,
      cheminD: this.editForm.get(['cheminD'])!.value,
      projet: this.projet
      }
  }
}

