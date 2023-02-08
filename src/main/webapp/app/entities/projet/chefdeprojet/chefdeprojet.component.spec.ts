import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChefdeprojetComponent } from './chefdeprojet.component';

describe('ChefdeprojetComponent', () => {
  let component: ChefdeprojetComponent;
  let fixture: ComponentFixture<ChefdeprojetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChefdeprojetComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChefdeprojetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
