import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewtacheComponent } from './newtache.component';

describe('NewtacheComponent', () => {
  let component: NewtacheComponent;
  let fixture: ComponentFixture<NewtacheComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewtacheComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewtacheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
