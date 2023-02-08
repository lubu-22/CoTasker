import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialoguedossierproComponent } from './dialoguedossierpro.component';

describe('DialoguedossierproComponent', () => {
  let component: DialoguedossierproComponent;
  let fixture: ComponentFixture<DialoguedossierproComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialoguedossierproComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DialoguedossierproComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
