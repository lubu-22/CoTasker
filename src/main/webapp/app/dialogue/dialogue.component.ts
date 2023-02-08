import {Component, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {IFichier} from "../entities/fichier/fichier.model";
import  {  faFile }  from '@fortawesome/free-solid-svg-icons' ;
import { DossierService } from '../entities/dossier/service/dossier.service';
import {IDossier} from "../entities/dossier/dossier.model";
import {faFileCircleCheck} from "@fortawesome/free-solid-svg-icons";
import {faFolderOpen} from "@fortawesome/free-solid-svg-icons";
import {DialoguefichierComponent} from "../dialoguefichier/dialoguefichier.component";
import {FichierDeleteDialogComponent} from "../entities/fichier/delete/fichier-delete-dialog.component";
@Component({
  selector: 'jhi-dialogue',
  templateUrl: './dialogue.component.html',
  styleUrls: ['./dialogue.component.scss']
})
export class DialogueComponent implements OnInit{
  dossier?:IDossier;
fichiers:IFichier[]=[];
videbolean=false;
vide?:string;
id?:number;
file=faFile;
files=faFileCircleCheck
  folder=faFolderOpen



  constructor(private activeModal: NgbActiveModal, private dossierService: DossierService,protected modalService: NgbModal) { }

  ngOnInit(): void {
    this.dossierService.fichierdedossier(this.id!).subscribe(data=>{
      if(data.body && data.body.length > 0 ){
        this.fichiers=data.body;
        console.warn('liste des produit disponible');
        console.warn(data);
      }
      if (data.body && data.body.length===0){
        this.vide="aucun fichier";
        this.videbolean=true;
      }
    }, () => {
      console.warn('produit indisponible');
    })
    }


  ouvrirfichier(id: number): void{

    const Dialogueref = this.modalService.open(DialoguefichierComponent,{ size: 'lg' });
    Dialogueref.componentInstance.id=id;
      }

  delete(fichier: IFichier): void {
    const modalRef = this.modalService.open(FichierDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fichier = fichier;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        console.warn("supprimé");
      }
    });
  }




}
