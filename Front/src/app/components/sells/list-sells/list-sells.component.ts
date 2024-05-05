import {Component, OnDestroy, OnInit} from '@angular/core';
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {ReactiveFormsModule} from "@angular/forms";
import {Subscription} from "rxjs";
import {Purchase} from "../../../models/purchase/purchase";
import {PurchaseService, stateClasses} from "../../../services/purchase/purchase.service";
import {AuthService} from "../../../services/user/auth.service";
import {Router} from "@angular/router";
import {Sell} from "../../../models/sell/sell";

@Component({
  selector: 'app-list-sells',
  templateUrl: './list-sells.component.html',
  styleUrl: './list-sells.component.css'
})
export class ListSellsComponent implements OnInit,OnDestroy {

  private subs: Subscription = new Subscription();

  firstDate: string ;
  lastDate: string ;

  list: Sell[] = [
  ];
  selected?:Sell;

  countTotal=1;
  size=3;
  page=0;

  constructor(private service: PurchaseService,private authService: AuthService, private router: Router) {
    let datenow= new Date(Date.now());
    this.lastDate= datenow.toISOString().split("T")[0]
    datenow.setDate(datenow.getDate()-90)
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

    this.subs.add(
      this.service.getSells(firstDate1,lastDate1, this.authService.user?.id||"0").subscribe(
        {
          next: value => {
            // this.countTotal=value["countTotal"]
            // this.list=value["list"]
            this.list=value as Sell[]
          },
          error: err => {
            console.log(err)
            alert("Hubo un error al buscar");
          }
        }
      )
    );
  }

  open(p:Sell){
    this.selected=p;
  }

  protected readonly stateClasses = stateClasses;
}

