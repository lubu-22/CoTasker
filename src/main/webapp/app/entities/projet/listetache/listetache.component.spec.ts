import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListetacheComponent } from './listetache.component';

describe('ListetacheComponent', () => {
  let component: ListetacheComponent;
  let fixture: ComponentFixture<ListetacheComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListetacheComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListetacheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
