import {Component, ElementRef, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Purchase} from "../../../models/purchase/purchase";
import {PurchaseService, stateClasses} from "../../../services/purchase/purchase.service";
import {AuthService} from "../../../services/user/auth.service";
import {Router} from "@angular/router";
import {Delivery} from "../../../models/delivery/delivery";
import {cAlert} from "../../../services/custom-alert/custom-alert.service"

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

  state="PENDIENTE";
  elements=1;
  size=3;
  page=0;
  pages=0;

  constructor(private service: PurchaseService,private authService: AuthService, private router: Router) {
  }
  ngOnInit(): void {
    this.charge(0)
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  clear(){

  }
  charge(page: number){
    this.page=page;

    if(this.page>this.pages-1){
      this.page=this.pages-1;
    }
    if(this.page<=0){
      this.page=0;
    }

    this.subs.add(
      this.service.getDeliveries(this.state,this.page,this.size).subscribe(
        {
          next: value => {
            this.elements=value["elements"]
            this.pages=value["pages"]
            this.list=value["list"]
          },
          error: err => {
            console.log(err)
            cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
          }
        }
      )
    );
  }

  open(p:Delivery){
    this.selected=p;
  }

    protected readonly stateClasses = stateClasses;
}
