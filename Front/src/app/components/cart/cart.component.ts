import {Component, OnDestroy, OnInit} from '@angular/core';
import {PurchaseService, stateClasses} from "../../services/purchase/purchase.service";
import {Subscription} from "rxjs";
import {Delivery} from "../../models/delivery/delivery";
import {AuthService} from "../../services/user/auth.service";
import {Router} from "@angular/router";
import {PublicationsService} from "../../services/publications/publications.service";
import {Cart} from "../../models/cart/cart";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit,OnDestroy {

  private subs: Subscription = new Subscription();

  list: Cart[] = [
  ];

  listCounts: number[] = [
  ];
  total:number=0;

  constructor(private service: PublicationsService,private authService: AuthService, private router: Router) {
  }
  ngOnInit(): void {
    this.charge()
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  charge(){

    this.subs.add(
      this.service.getCart().subscribe(
        {
          next: value => {
            // this.countTotal=value["countTotal"]
            // this.list=value["list"]
            this.list=value as Cart[]
            this.total =0;
            for (let card of this.list){
              this.listCounts.push(card.selectedCount)
              this.total += card.price*card.selectedCount
            }
          },
          error: err => {
            console.log(err)
            alert("Hubo un error al buscar");
          }
        }
      )
    );
  }

}
