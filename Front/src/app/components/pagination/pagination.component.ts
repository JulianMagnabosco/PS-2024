import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgClass, NgForOf} from "@angular/common";

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [
    NgForOf,
    NgClass
  ],
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.css'
})
export class PaginationComponent {
  @Output() eventChange=new EventEmitter<any>();
  @Input() pages=0;
  @Input() page=0;

  charge(n:number){
    this.eventChange.emit(n);
  }

  get pagesList(){
    return Array(this.pages).fill(0).map((x,i)=>i);
  }
}
