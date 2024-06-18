import {Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {Purchase} from "../../../models/purchase/purchase";
import {PurchaseService, stateClasses} from "../../../services/purchase/purchase.service";
import {Delivery} from "../../../models/delivery/delivery";
import {cAlert, cConfirm} from "../../../services/custom-alert/custom-alert.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-show-purchase',
  templateUrl: './show-purchase.component.html',
  styleUrl: './show-purchase.component.css'
})
export class ShowPurchaseComponent {
  @Output() eventClose = new EventEmitter<Delivery>();
  @ViewChild("close") closeModal?: ElementRef;
  @Input() purchase?: Purchase;
  private subs: Subscription = new Subscription();

  protected readonly stateClasses = stateClasses;

  constructor(private service: PurchaseService) {
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
  delete(){
    cConfirm("¿Seguro que quieres cancelarla?").then(value => {
      this.subs.add(this.service.deleteSell(this.purchase?.id.toString()||"0").subscribe(
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
    })
  }
}
