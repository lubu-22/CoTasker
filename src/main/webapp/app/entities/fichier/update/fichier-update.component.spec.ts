import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FichierService } from '../service/fichier.service';
import { IFichier, Fichier } from '../fichier.model';
import { IDossier } from 'app/entities/dossier/dossier.model';
import { DossierService } from 'app/entities/dossier/service/dossier.service';

import { FichierUpdateComponent } from './fichier-update.component';

describe('Fichier Management Update Component', () => {
  let comp: FichierUpdateComponent;
  let fixture: ComponentFixture<FichierUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fichierService: FichierService;
  let dossierService: DossierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FichierUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FichierUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FichierUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fichierService = TestBed.inject(FichierService);
    dossierService = TestBed.inject(DossierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Dossier query and add missing value', () => {
      const fichier: IFichier = { id: 456 };
      const dossier: IDossier = { id: 54742 };
      fichier.dossier = dossier;

      const dossierCollection: IDossier[] = [{ id: 59968 }];
      jest.spyOn(dossierService, 'query').mockReturnValue(of(new HttpResponse({ body: dossierCollection })));
      const additionalDossiers = [dossier];
      const expectedCollection: IDossier[] = [...additionalDossiers, ...dossierCollection];
      jest.spyOn(dossierService, 'addDossierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fichier });
      comp.ngOnInit();

      expect(dossierService.query).toHaveBeenCalled();
      expect(dossierService.addDossierToCollectionIfMissing).toHaveBeenCalledWith(dossierCollection, ...additionalDossiers);
      expect(comp.dossiersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const fichier: IFichier = { id: 456 };
      const dossier: IDossier = { id: 87921 };
      fichier.dossier = dossier;

      activatedRoute.data = of({ fichier });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(fichier));
      expect(comp.dossiersSharedCollection).toContain(dossier);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Fichier>>();
      const fichier = { id: 123 };
      jest.spyOn(fichierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fichier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fichier }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(fichierService.update).toHaveBeenCalledWith(fichier);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Fichier>>();
      const fichier = new Fichier();
      jest.spyOn(fichierService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fichier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fichier }));
      saveSubject.complete();

      // THEN
      expect(fichierService.create).toHaveBeenCalledWith(fichier);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Fichier>>();
      const fichier = { id: 123 };
      jest.spyOn(fichierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fichier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fichierService.update).toHaveBeenCalledWith(fichier);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDossierById', () => {
      it('Should return tracked Dossier primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDossierById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
