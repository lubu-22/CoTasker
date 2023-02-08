import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {DossierService} from "../entities/dossier/service/dossier.service";
import {faFile, faFileCircleCheck, faFolderOpen} from "@fortawesome/free-solid-svg-icons";
import {FichierService} from "../entities/fichier/service/fichier.service";

@Component({
  selector: 'jhi-dialoguefichier',
  templateUrl: './dialoguefichier.component.html',
  styleUrls: ['./dialoguefichier.component.scss']
})
export class DialoguefichierComponent implements OnInit {

  id?:number;
  urlCreator : any ;
  pdfSrc : any;
  constructor(private activeModal: NgbActiveModal, private fichierService: FichierService) { }

  ngOnInit(): void {

    this.fichierService.finddocument(this.id!).subscribe(
      data =>{
        this.urlCreator= window.URL;
        this.pdfSrc = this.urlCreator.createObjectURL(data);
        console.warn(this.id);

      });

  }

}
