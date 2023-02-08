import { Component, OnInit } from '@angular/core';
import {ITache, Tache} from "../../tache/tache.model";
import {Observable} from "rxjs";
import {HttpResponse} from "@angular/common/http";
import {finalize, map} from "rxjs/operators";
import {IUser} from "../../user/user.model";
import {UserService} from "../../user/user.service";
import {TacheService} from "../../tache/service/tache.service";
import {ActivatedRoute} from "@angular/router";
import {FormBuilder} from "@angular/forms";
import {IProjet, Projet} from "../projet.model";
import {ProjetService} from "../service/projet.service";

@Component({
  selector: 'jhi-chefdeprojet',
  templateUrl: './chefdeprojet.component.html',
  styleUrls: ['./chefdeprojet.component.scss']
})
export class ChefdeprojetComponent implements OnInit {
  projet?: IProjet;
  usersSharedCollection: IUser[] = [];
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    role: [],
    descriptiontache: [],
    projet: [],
    user: [],
  });

  projetform = this.fb.group({
    id: [],
    intitule: [],
    descriptionProjet: [],
    type: [],
    etape: [],
    dateCreation: [],
    dateFin: [],
    client: [],
    user: [],
  });





  constructor(protected userService: UserService, protected tacheService: TacheService, protected projetService: ProjetService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projet }) => {
      this.editprojetform(projet);
      this.projet=projet;
      console.warn(projet);
    });
    this.loadRelationshipsOptions();

  }

  previousState(): void {
    window.history.back();
  }


  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }



  save(): void {
    this.isSaving = true;
    const tache = this.createFromForm();
    const projet=this.createprojetForm();
    this.subscribeToSaveResponse(this.tacheService.create(tache));
    this.subscribeToSaveResponseprojet(this.projetService.update(projet));

  }



  protected loadRelationshipsOptions(): void {
    this.userService.query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITache>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      error: () => this.onSaveError(),
    });
  }


  protected subscribeToSaveResponseprojet(result: Observable<HttpResponse<IProjet>>): void {
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

  protected editprojetform(projet: IProjet): void {
    this.projetform.patchValue({
      id: projet.id,
      intitule: projet.intitule,
      descriptionProjet: projet.descriptionProjet,
      type: projet.type,
      etape: projet.etape,
      dateCreation: projet.dateCreation,
      dateFin: projet.dateFin,
      client: projet.client,
      user: projet.user,
    });}

  protected createFromForm(): ITache {
    return {
      ...new Tache(),
      id: this.editForm.get(['id'])!.value,
      role: this.editForm.get(['role'])!.value,
      descriptiontache: "Devenu chef de Projet",
      projet: this.projet,
      user: this.editForm.get(['user'])!.value,
    };
  }


  protected createprojetForm(): IProjet {
    return {
      ...new Projet(),
      id: this.projetform.get(['id'])!.value,
      intitule: this.projetform.get(['intitule'])!.value,
      descriptionProjet: this.projetform.get(['descriptionProjet'])!.value,
      type: this.projetform.get(['type'])!.value,
      etape: this.projetform.get(['etape'])!.value,
      dateCreation: this.projetform.get(['dateCreation'])!.value,
      dateFin: this.projetform.get(['dateFin'])!.value,
      client: this.projetform.get(['client'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
