import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListSellsComponent } from './list-sells.component';

describe('ListSellsComponent', () => {
  let component: ListSellsComponent;
  let fixture: ComponentFixture<ListSellsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListSellsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListSellsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
