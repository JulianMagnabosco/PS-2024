import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Purchase} from "../../../models/purchase/purchase";
import {PurchaseService} from "../../../services/purchase/purchase.service";
import {AuthService} from "../../../services/user/auth.service";
import {Router} from "@angular/router";
import {Delivery} from "../../../models/delivery/delivery";

@Component({
  selector: 'app-list-deliveries',
  templateUrl: './list-deliveries.component.html',
  styleUrl: './list-deliveries.component.css'
})
export class ListDeliveriesComponent implements OnInit,OnDestroy {

  private subs: Subscription = new Subscription();

  list: Delivery[] = [
  ];
  selected?:Delivery;

  countTotal=1;
  size=3;
  page=0;

  constructor(private service: PurchaseService,private authService: AuthService, private router: Router) {
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

    this.subs.add(
      this.service.getDeliveries(this.authService.user?.id||"0").subscribe(
        {
          next: value => {
            // this.countTotal=value["countTotal"]
            // this.list=value["list"]
            this.list=value as Delivery[]
          },
          error: err => {
            console.log(err)
            alert("Hubo un error al buscar");
          }
        }
      )
    );
  }

  open(p:Delivery){
    this.selected=p;
  }
}
