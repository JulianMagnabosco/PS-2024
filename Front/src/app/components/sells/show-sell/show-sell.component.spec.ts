import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowSellComponent } from './show-sell.component';

describe('ShowSellComponent', () => {
  let component: ShowSellComponent;
  let fixture: ComponentFixture<ShowSellComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowSellComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ShowSellComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
