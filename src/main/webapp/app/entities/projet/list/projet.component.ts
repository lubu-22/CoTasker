import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import {combineLatest, Observable} from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProjet } from '../projet.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { ProjetService } from '../service/projet.service';
import { ProjetDeleteDialogComponent } from '../delete/projet-delete-dialog.component';
import {DialoguedossierproComponent} from "../../../dialoguedossierpro/dialoguedossierpro.component";
import  {  faFolder }  from '@fortawesome/free-solid-svg-icons' ;
import {faListCheck} from "@fortawesome/free-solid-svg-icons";
import {Authority} from "../../../config/authority.constants";
import {UserService} from "../../user/user.service";
import {UserManagementService} from "../../../admin/user-management/service/user-management.service";
import {IUser} from "../../../admin/user-management/user-management.model";

@Component({
  selector: 'jhi-projet',
  templateUrl: './projet.component.html',
})
export class ProjetComponent implements OnInit {
  faFolder=faFolder;
  fatache=faListCheck;
  projets?: IProjet[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  enumauthority= Authority;


  constructor(
    protected projetService: ProjetService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected userService: UserManagementService
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.projetService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IProjet[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        error: () => {
          this.isLoading = false;
          this.onError();
        },
      });
  }

  ngOnInit(): void {
    this.handleNavigation();

  }



  trackId(_index: number, item: IProjet): number {
    return item.id!;
  }

  ouvrirModal(id: number): void{

    const Dialogueref = this.modalService.open(DialoguedossierproComponent,{ size: 'lg' });
    Dialogueref.componentInstance.id=id;

  }



  delete(projet: IProjet): void {
    const modalRef = this.modalService.open(ProjetDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.projet = projet;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IProjet[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/projet'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.projets = data ?? [];

    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
