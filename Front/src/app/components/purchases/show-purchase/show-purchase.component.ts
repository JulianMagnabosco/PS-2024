import {Component, Input} from '@angular/core';
import {Purchase} from "../../../models/purchase/purchase";

@Component({
  selector: 'app-show-purchase',
  templateUrl: './show-purchase.component.html',
  styleUrl: './show-purchase.component.css'
})
export class ShowPurchaseComponent {
  @Input() purchase?: Purchase;
}
