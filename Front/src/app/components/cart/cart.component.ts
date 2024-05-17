import {Component, OnDestroy, OnInit} from '@angular/core';
import {PurchaseService, stateClasses} from "../../services/purchase/purchase.service";
import {Subscription} from "rxjs";
import {Delivery} from "../../models/delivery/delivery";
import {AuthService} from "../../services/user/auth.service";
import {Router} from "@angular/router";
import {PublicationsService} from "../../services/publications/publications.service";
import {Cart} from "../../models/cart/cart";
import Swal from "sweetalert2";

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

  constructor(private service: PublicationsService,private purchaseService: PurchaseService) {
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
            Swal.fire({
              title: "Error",
              text: "Error inesperado en el servidor, revise su conexion a internet",
              icon: "error"
            });
          }
        }
      )
    );
  }

  update(count:number, cart:Cart){
    let data = {
      pubId: cart.id,
      value: count
    }
    this.subs.add(
      this.service.postCart(data).subscribe({
        next: value => {
          Swal.fire({
            title: "Exito",
            text: "Añadido al carrito",
            icon: "success"
          }).then((value)=>{
            this.charge()
          });
        },
        error:err => {
          Swal.fire({
            title: "Error",
            text: "Error inesperado en el servidor, revise su conexion a internet",
            icon: "error"
          });
        }
      })
    )

  }
  buy(){
    let items=[];
    for(let item of this.list){
      items.push({
        idPub: item.id,
        count: item.selectedCount
      })
    }
    let data = {
      items: items
    }
    this.subs.add(
      this.purchaseService.postSale(data).subscribe(
        {
          next: value => {
            // console.log(value["preference"]["initPoint"])
            window.location.href = value["preference"]["initPoint"]
          },
          error: err => {
            console.log(err)
            if(err.status==400){
              Swal.fire({
                title: "Error",
                text: "El usuario no posee los datos de compra completos",
                icon: "error"
              });
            }else {
              Swal.fire({
                title: "Error",
                text: "Error inesperado en el servidor, revise su conexion a internet",
                icon: "error"
              });
            }
          }
        }
      )
    );
  }
  buyAlone(cart:Cart){
    let item={

      idPub: cart.id,
      count: cart.selectedCount
    }
    let data = {
      items: [item]
    }
    this.subs.add(
      this.purchaseService.postSale(data).subscribe(
        {
          next: value => {
            // console.log(value["preference"]["initPoint"])
            window.location.href = value["preference"]["initPoint"]
          },
          error: err => {
            console.log(err)
            if(err.status==400){
              Swal.fire({
                title: "Error",
                text: "El usuario no posee los datos de compra completos",
                icon: "error"
              });
            }else {
              Swal.fire({
                title: "Error",
                text: "Error inesperado en el servidor, revise su conexion a internet",
                icon: "error"
              });
            }
          }
        }
      )
    );
  }

  notupdate(i:number,cart:Cart){
    this.listCounts[i] = cart.selectedCount
  }
  remove(cart:Cart){
    this.update(0,cart)
  }
}
