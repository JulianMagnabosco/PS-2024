import {Component, OnInit} from '@angular/core';
import {Publication} from "../../../models/publication/publication";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {PublicationsService} from "../../../services/publications/publications.service";

@Component({
  selector: 'app-show-publication',
  templateUrl: './show-publication.component.html',
  styleUrls: ['./show-publication.component.css']
})
export class ShowPublicationComponent implements OnInit{

  private subs: Subscription = new Subscription();

  publication:Publication={
    id: 2,
    name: "Panal de bellotas",
    description: "esto es un panal de bellotas",
    type: "ARTE",
    difficulty: "Hard",
    image: "null",
    sections: [
      {
        number: 1,
        type: "COND",
        text: "esto es un panal de bellotas",
        image: "a"
      }
    ],
    canSold: false,
    price: 0,
    count: 0
  };
  constructor(private activeRoute:ActivatedRoute, private service: PublicationsService, private router: Router) {


  }
  ngOnInit(): void {
    this.charge();
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  get conditions(){
    return this.publication.sections.filter((s) => s.type=="COND")
  }
  get materials(){
    return this.publication.sections.filter((s) => s.type=="MAT")
  }

  get steps(){
    return this.publication.sections.filter((s) => s.type=="STEP")
  }


  charge(){
    let id="" ;
    this.subs.add(
      this.activeRoute.params.subscribe(
        {
          next: value => {
            id = value["id"]
            this.subs.add(
              this.service.get(id).subscribe(
                {
                  next: value => {
                    console.log(value)
                    this.publication=value
                  },
                  error: err => {
                    alert("Hubo un error al guardar");
                  }
                }
              )
            );
          }
        }
      )
    );


  }

}
