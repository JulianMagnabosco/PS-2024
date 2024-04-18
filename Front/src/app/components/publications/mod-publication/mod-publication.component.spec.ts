import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModPublicationComponent } from './mod-publication.component';

describe('ModPublicationComponent', () => {
  let component: ModPublicationComponent;
  let fixture: ComponentFixture<ModPublicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModPublicationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModPublicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
