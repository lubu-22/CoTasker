import { Component, OnInit } from '@angular/core';
import {Role} from "../../enumerations/role.model";
import {TacheService} from "../../tache/service/tache.service";
import {ActivatedRoute} from "@angular/router";
import {FormBuilder} from "@angular/forms";
import {Observable} from "rxjs";
import {HttpResponse} from "@angular/common/http";
import {ITache, Tache} from "../../tache/tache.model";
import {finalize, map} from "rxjs/operators";
import {IProjet} from "../projet.model";
import {IUser} from "../../user/user.model";
import {UserService} from "../../user/user.service";

@Component({
  selector: 'jhi-newtache',
  templateUrl: './newtache.component.html',
  styleUrls: ['./newtache.component.scss']
})
export class NewtacheComponent implements OnInit {
  isSaving = false;
  roleValues = Object.keys(Role);
  projet?: IProjet;
  usersSharedCollection: IUser[] = [];


  editForm = this.fb.group({
    id: [],
    role: [],
    descriptiontache: [],
    projet: [],
    user: [],
  });
  constructor(protected userService: UserService, protected tacheService: TacheService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projet }) => {
      this.projet=projet;});

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
      this.subscribeToSaveResponse(this.tacheService.create(tache));
  }



  protected loadRelationshipsOptions(): void {
    this.userService.query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }








  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITache>>): void {
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

  protected updateForm(tache: ITache): void {
    this.editForm.patchValue({
      id: tache.id,
      role: tache.role,
      descriptiontache: tache.descriptiontache,
      projet: tache.projet,
      user: tache.user,
    });
  }

  protected createFromForm(): ITache {
    return {
      ...new Tache(),
      id: this.editForm.get(['id'])!.value,
      role: this.editForm.get(['role'])!.value,
      descriptiontache: this.editForm.get(['descriptiontache'])!.value,
      projet: this.projet,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
