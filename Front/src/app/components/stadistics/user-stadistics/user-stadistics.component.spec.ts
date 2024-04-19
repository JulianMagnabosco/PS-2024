import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserStadisticsComponent } from './user-stadistics.component';

describe('UserStadisticsComponent', () => {
  let component: UserStadisticsComponent;
  let fixture: ComponentFixture<UserStadisticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserStadisticsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserStadisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
