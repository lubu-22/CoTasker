import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ITache} from "../../tache/tache.model";
import {Account} from "../../../core/auth/account.model";
import {Subject} from "rxjs";
import {takeUntil} from "rxjs/operators";
import {AccountService} from "../../../core/auth/account.service";

@Component({
  selector: 'jhi-listetache',
  templateUrl: './listetache.component.html',
  styleUrls: ['./listetache.component.scss']
})
export class ListetacheComponent implements OnInit {
 taches?:ITache[];
  account: Account | null = null;
  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService,protected activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taches }) => {
      this.taches= taches;
      console.warn(taches);
      this.accountService
        .getAuthenticationState()
        .pipe(takeUntil(this.destroy$))
        .subscribe(account => (this.account = account));

    });
  }

}
