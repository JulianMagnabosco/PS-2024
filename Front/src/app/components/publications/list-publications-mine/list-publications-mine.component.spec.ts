import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListPublicationsMineComponent } from './list-publications-mine.component';

describe('ListPublicationsMineComponent', () => {
  let component: ListPublicationsMineComponent;
  let fixture: ComponentFixture<ListPublicationsMineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListPublicationsMineComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListPublicationsMineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
