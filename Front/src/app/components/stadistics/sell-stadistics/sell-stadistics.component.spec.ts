import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SellStadisticsComponent } from './sell-stadistics.component';

describe('SellStadisticsComponent', () => {
  let component: SellStadisticsComponent;
  let fixture: ComponentFixture<SellStadisticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SellStadisticsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SellStadisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
