import { Component, OnInit } from '@angular/core';
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ProjetService} from "../entities/projet/service/projet.service";
import {IDossier} from "../entities/dossier/dossier.model";
import  {  faFolder }  from '@fortawesome/free-solid-svg-icons' ;
import {DialogueComponent} from "../dialogue/dialogue.component";
import {faFolderOpen} from "@fortawesome/free-solid-svg-icons/faFolderOpen";
import {NewfichierComponent} from "../entities/fichier/newfichier/newfichier.component";
import {DossierDeleteDialogComponent} from "../entities/dossier/delete/dossier-delete-dialog.component";

@Component({
  selector: 'jhi-dialoguedossierpro',
  templateUrl: './dialoguedossierpro.component.html',
  styleUrls: ['./dialoguedossierpro.component.scss']
})
export class DialoguedossierproComponent implements OnInit {
  faFolder=faFolder;
faopen=faFolderOpen
  dossiers?:IDossier[]=[];
  id?:number;
  videbolean=false;
  vide?:string;


  constructor(private activeModal: NgbActiveModal, private projetservice: ProjetService,protected modalService: NgbModal
  ) { }

  ngOnInit(): void {


    this.projetservice.dossierdeprojet(this.id!).subscribe(data=>{
      if(data.body && data.body.length > 0 ){
        this.dossiers=data.body;
        console.warn('liste des produit disponible');
      }
      if (data.body && data.body.length===0){
        this.vide="aucun fichier";
        this.videbolean=true;
      }
    }, () => {
      console.warn('produit indisponible');
    })
  }
  ouvrirModal(id: number): void{

    const Dialogueref = this.modalService.open(DialogueComponent,{ size: 'lg' });
    Dialogueref.componentInstance.id=id;

  }

  ouvrirmodaldenewfichier(dossier: IDossier): void {

    const Dialoguereff = this.modalService.open(NewfichierComponent,{ size: 'lg' });
    Dialoguereff.componentInstance.dossier=dossier;

  }



  delete(dossier: IDossier): void {
    const modalRef = this.modalService.open(DossierDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dossier = dossier;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
    }

  }




