import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialoguefichierComponent } from './dialoguefichier.component';

describe('DialoguefichierComponent', () => {
  let component: DialoguefichierComponent;
  let fixture: ComponentFixture<DialoguefichierComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialoguefichierComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DialoguefichierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
