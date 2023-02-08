import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewfichierComponent } from './newfichier.component';

describe('NewfichierComponent', () => {
  let component: NewfichierComponent;
  let fixture: ComponentFixture<NewfichierComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewfichierComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewfichierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
