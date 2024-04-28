import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListPurchasesComponent } from './list-purchases.component';

describe('ListPurchasesComponent', () => {
  let component: ListPurchasesComponent;
  let fixture: ComponentFixture<ListPurchasesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListPurchasesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListPurchasesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
