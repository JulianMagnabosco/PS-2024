import {Component, Input} from '@angular/core';
import {Purchase} from "../../../models/purchase/purchase";
import {Sell} from "../../../models/sell/sell";

@Component({
  selector: 'app-show-sell',
  templateUrl: './show-sell.component.html',
  styleUrl: './show-sell.component.css'
})
export class ShowSellComponent {
  @Input() sell?: Sell;
}
