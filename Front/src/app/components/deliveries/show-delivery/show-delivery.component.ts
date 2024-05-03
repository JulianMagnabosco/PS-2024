import {Component, Input} from '@angular/core';
import {Purchase} from "../../../models/purchase/purchase";
import {Delivery} from "../../../models/delivery/delivery";

@Component({
  selector: 'app-show-delivery',
  templateUrl: './show-delivery.component.html',
  styleUrl: './show-delivery.component.css'
})
export class ShowDeliveryComponent {
  @Input() delivery?: Delivery;
}
