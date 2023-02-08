import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewdossierComponent } from './newdossier.component';

describe('NewdossierComponent', () => {
  let component: NewdossierComponent;
  let fixture: ComponentFixture<NewdossierComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewdossierComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewdossierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
