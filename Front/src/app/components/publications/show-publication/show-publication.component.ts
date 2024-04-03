import {Component, OnInit} from '@angular/core';
import {Publication} from "../../../models/publication/publication";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-show-publication',
  templateUrl: './show-publication.component.html',
  styleUrls: ['./show-publication.component.css']
})
export class ShowPublicationComponent implements OnInit{
  showConditions=true
  showMaterials=true
  showSteps=true
  showPurchasedata=true

  id:string|null = null;

  pub: Publication = {
    id: 1,
    name: "Cracion",
    description: "Border",
    tags: ["Border"],
    type: "Arte",
    difficulty: "Dificil",
    calification: 1,
    image: "https://fcb-abj-pre.s3.amazonaws.com/img/jugadors/MESSI.jpg",
    conditions: ["Border","Border","Border"],
    materials: ["Border","Border","Border"],
    steps: [],
    canSold: true,
    price: 20,
    count: 1
  };
  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
  }
}
