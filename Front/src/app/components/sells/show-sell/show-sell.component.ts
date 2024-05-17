import {Component, ElementRef, EventEmitter, Input, OnDestroy, Output, ViewChild} from '@angular/core';
import {Purchase} from "../../../models/purchase/purchase";
import {Sell} from "../../../models/sell/sell";
import {PurchaseService, stateClasses} from "../../../services/purchase/purchase.service";
import {Subscription} from "rxjs";
import {Comment} from "../../../models/comment/comment";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CommentService} from "../../../services/comment/comment.service";
import {Delivery} from "../../../models/delivery/delivery";
import {cAlert} from "../../../services/custom-alert/custom-alert.service"

@Component({
  selector: 'app-show-sell',
  templateUrl: './show-sell.component.html',
  styleUrl: './show-sell.component.css'
})
export class ShowSellComponent implements OnDestroy{

  @Output() eventClose = new EventEmitter<Delivery>();
  @ViewChild("close") closeModal?: ElementRef;
  @Input() sell?: Sell;
  private subs: Subscription = new Subscription();

  protected readonly stateClasses = stateClasses;
  constructor(private service: PurchaseService) {
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }


  delete(){
    this.subs.add(this.service.deleteSell(this.sell?.id.toString()||"0").subscribe(
      {
        next: value => {
          cAlert("success","Delivery guardado").then(()=>{
            this.eventClose.emit();
            this.closeModal?.nativeElement.click()
          });
        },
        error: err => {
          console.log(err)
              cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
        }
      }))
  }
}
