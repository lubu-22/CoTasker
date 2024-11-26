import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";

import {ITache} from "../../tache/tache.model";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DialoguedossierproComponent} from "../../../dialoguedossierpro/dialoguedossierpro.component";
import {Account} from "../../../core/auth/account.model";
import {takeUntil} from "rxjs/operators";
import {AccountService} from "../../../core/auth/account.service";
import {Subject} from "rxjs";
import { IProjet } from '../projet.model';

@Component({
  selector: 'jhi-listeprojetpersonne',
  templateUrl: './listeprojetpersonne.component.html',
  styleUrls: ['./listeprojetpersonne.component.scss']
})
export class ListeprojetpersonneComponent implements OnInit {
  taches?:IProjet[];
  account: Account | null = null;
  private readonly destroy$ = new Subject<void>();


  constructor(private accountService: AccountService,protected activatedRoute: ActivatedRoute,protected modalService: NgbModal
  ) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taches }) => {
      this.taches= taches;
      this.accountService
        .getAuthenticationState()
        .pipe(takeUntil(this.destroy$))
        .subscribe(account => (this.account = account));


    })

}

  ouvrirModal(id: number): void{

    const Dialogueref = this.modalService.open(DialoguedossierproComponent,{ size: 'lg' });
    Dialogueref.componentInstance.id=id;

  }
}
