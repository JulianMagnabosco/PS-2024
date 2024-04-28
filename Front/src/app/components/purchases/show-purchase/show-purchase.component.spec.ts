import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowPurchaseComponent } from './show-purchase.component';

describe('ShowPurchaseComponent', () => {
  let component: ShowPurchaseComponent;
  let fixture: ComponentFixture<ShowPurchaseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowPurchaseComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ShowPurchaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
