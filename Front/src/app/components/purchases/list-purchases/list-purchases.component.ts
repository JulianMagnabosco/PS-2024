import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {FormBuilder, FormGroup, FormsModule, Validators} from "@angular/forms";
import {PublicationMin} from "../../../models/publication/publication-min";
import {PublicationsService} from "../../../services/publications/publications.service";
import {Router} from "@angular/router";
import {PurchaseService, stateClasses} from "../../../services/purchase/purchase.service";
import {Purchase} from "../../../models/purchase/purchase";
import {AuthService} from "../../../services/user/auth.service";
import {cAlert} from "../../../services/custom-alert/custom-alert.service"

@Component({
  selector: 'app-list-purchases',
  templateUrl: './list-purchases.component.html',
  styleUrl: './list-purchases.component.css'
})
export class ListPurchasesComponent implements OnInit,OnDestroy {

  private subs: Subscription = new Subscription();

  text: string ="";
  firstDate: string ;
  lastDate: string ;

  list: Purchase[] = [
  ];
  selected?:Purchase;

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
      this.service.getPurchases(firstDate1,lastDate1, this.text).subscribe(
        {
          next: value => {
            // this.countTotal=value["countTotal"]
            // this.list=value["list"]
            this.list=value as Purchase[]
          },
          error: err => {
            console.log(err)


              cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
          }
        }
      )
    );
  }

  open(p:Purchase){
    this.selected=p;
  }

  protected readonly stateClasses = stateClasses;
}
