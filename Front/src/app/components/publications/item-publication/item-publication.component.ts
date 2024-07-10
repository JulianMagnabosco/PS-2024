import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {Publication} from "../../../models/publication/publication";
import {PublicationMin} from "../../../models/publication/publication-min";
import {StyledTextPipe} from "../../../pipes/styled-text.pipe";

@Component({
  selector: 'app-item-publication',
  standalone: true,
    imports: [
        NgForOf,
        RouterLink,
        DatePipe,
        StyledTextPipe,
        NgIf
    ],
  templateUrl: './item-publication.component.html',
  styleUrl: './item-publication.component.css'
})
export class ItemPublicationComponent {
  @Input("publication") p:PublicationMin={
    userId: "",
    userIconUrl: "",
    calification: 0,
    canSold: false,
    count: 0,
    dateTime: "",
    description: "",
    difficulty: "",
    id: 0,
    imageUrl: "",
    name: "",
    price: 0,
    type: "",
    username: ""

  };

  @Input() showAll=true;
}
