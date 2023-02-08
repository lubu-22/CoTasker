import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize, map} from 'rxjs/operators';

import {IProjet, Projet} from '../projet.model';
import {ProjetService} from '../service/projet.service';
import {IClient} from 'app/entities/client/client.model';
import {ClientService} from 'app/entities/client/service/client.service';
import {TypeProjet} from 'app/entities/enumerations/type-projet.model';
import {Etape} from 'app/entities/enumerations/etape.model';
import {IUser} from "../../user/user.model";
import {UserService} from "../../user/user.service";
import {TacheService} from "../../tache/service/tache.service";
import {ITache, Tache} from "../../tache/tache.model";
import {Role} from "../../enumerations/role.model";

@Component({
  selector: 'jhi-projet-update',
  templateUrl: './projet-update.component.html',
})
export class ProjetUpdateComponent implements OnInit {
  isSaving = false;
  typeProjetValues = Object.keys(TypeProjet);
  etapeValues = Object.keys(Etape);

  clientsSharedCollection: IClient[] = [];
  usersSharedCollection: IUser[] = [];
  tache?: ITache;

  Formtache = this.fb.group({
    id: [],
    role: [],
    descriptiontache: [],
    projet: [],
    user: [],
  });

  editForm = this.fb.group({
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

  constructor(
    protected projetService: ProjetService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected userService: UserService,
    protected tacheService: TacheService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projet }) => {
      this.updateForm(projet);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const projet = this.createFromForm();
    if (projet.id !== undefined) {
      this.subscribeToSaveResponse(this.projetService.update(projet));
    } else {
      this.subscribeToSaveResponse(this.projetService.create(projet));
    }
  }

  trackClientById(_index: number, item: IClient): number {
    return item.id!;
  }

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProjet>>): void {
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



  protected updateForm(projet: IProjet): void {
    this.editForm.patchValue({
      id: projet.id,
      intitule: projet.intitule,
      descriptionProjet: projet.descriptionProjet,
      type: projet.type,
      etape: projet.etape,
      dateCreation: projet.dateCreation,
      dateFin: projet.dateFin,
      client: projet.client,
      user: projet.user,
    });

    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, projet.client);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, projet.user);

  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
    this.userService.query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IProjet {
    return {
      ...new Projet(),
      id: this.editForm.get(['id'])!.value,
      intitule: this.editForm.get(['intitule'])!.value,
      descriptionProjet: this.editForm.get(['descriptionProjet'])!.value,
      type: this.editForm.get(['type'])!.value,
      etape: this.editForm.get(['etape'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      dateFin: this.editForm.get(['dateFin'])!.value,
      client: this.editForm.get(['client'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }


  protected createFromTache(porjet: IProjet): ITache {
    return {
      ...new Tache(),
      id: this.Formtache.get(['id'])!.value,
      role: Role.CHEF,
      descriptiontache: this.Formtache.get(['descriptiontache'])!.value,
      user: porjet.user,
      projet: porjet,

    };
  }





}
