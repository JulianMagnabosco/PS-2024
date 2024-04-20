import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PubStadisticsComponent } from './pub-stadistics.component';

describe('PubStadisticsComponent', () => {
  let component: PubStadisticsComponent;
  let fixture: ComponentFixture<PubStadisticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PubStadisticsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PubStadisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
