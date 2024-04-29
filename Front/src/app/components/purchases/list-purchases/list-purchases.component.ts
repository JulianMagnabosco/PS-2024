import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {FormBuilder, FormGroup, FormsModule, Validators} from "@angular/forms";
import {PublicationMin} from "../../../models/publication/publication-min";
import {PublicationsService} from "../../../services/publications/publications.service";
import {Router} from "@angular/router";
import {PurchaseService} from "../../../services/purchase/purchase.service";
import {Purchase} from "../../../models/purchase/purchase";

@Component({
  selector: 'app-list-purchases',
  templateUrl: './list-purchases.component.html',
  styleUrl: './list-purchases.component.css'
})
export class ListPurchasesComponent implements OnInit,OnDestroy {

  private subs: Subscription = new Subscription();

  firstDate: string ;
  lastDate: string ;

  list: Purchase[] = [
  ];
  selected?:Purchase;

  countTotal=1;
  size=3;
  page=0;

  constructor(private service: PurchaseService, private router: Router) {
    let datenow= new Date(Date.now());
    this.lastDate= datenow.toISOString().split("T")[0]
    datenow.setDate(datenow.getDate()-1)
    this.firstDate= datenow.toISOString().split("T")[0]
  }
  ngOnInit(): void {
    this.charge(0)
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  get pages(){
    return Array(Math.ceil(this.countTotal/this.size)).fill(0).map((x,i)=>i);
  }
  clear(){

  }
  charge(page: number){
    this.page=page;

    if(page>Math.ceil(this.countTotal/this.size)-1){
      this.page=Math.ceil(this.countTotal/this.size)-1;
    }
    if(page<=0){
      this.page=0;
    }

    let firstDate1 = this.firstDate+"T00:00:00"
    let lastDate1 = this.lastDate+"T23:59:59"

    console.log(lastDate1)


    this.subs.add(
      this.service.getPurchases(firstDate1,lastDate1).subscribe(
        {
          next: value => {
            // this.countTotal=value["countTotal"]
            // this.list=value["list"]
            this.list=value as Purchase[]
          },
          error: err => {
            console.log(err)
            alert("Hubo un error al buscar");
          }
        }
      )
    );
  }

  open(p:Purchase){
    this.selected=p;
  }
}
