import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemPublicationComponent } from './item-publication.component';

describe('ItemPublicationComponent', () => {
  let component: ItemPublicationComponent;
  let fixture: ComponentFixture<ItemPublicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItemPublicationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ItemPublicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
