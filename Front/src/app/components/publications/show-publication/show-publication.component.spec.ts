import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowPublicationComponent } from './show-publication.component';

describe('ShowPublicationComponent', () => {
  let component: ShowPublicationComponent;
  let fixture: ComponentFixture<ShowPublicationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShowPublicationComponent]
    });
    fixture = TestBed.createComponent(ShowPublicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
