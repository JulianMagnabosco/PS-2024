import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowDeliveryComponent } from './show-delivery.component';

describe('ShowDeliveryComponent', () => {
  let component: ShowDeliveryComponent;
  let fixture: ComponentFixture<ShowDeliveryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowDeliveryComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ShowDeliveryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
