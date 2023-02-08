import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeprojetpersonneComponent } from './listeprojetpersonne.component';

describe('ListeprojetpersonneComponent', () => {
  let component: ListeprojetpersonneComponent;
  let fixture: ComponentFixture<ListeprojetpersonneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListeprojetpersonneComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListeprojetpersonneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
