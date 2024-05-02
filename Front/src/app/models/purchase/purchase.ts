import {PurchaseItem} from "./purchase-item";

export interface Purchase {
   id:number;
   dateTime:string;
   details:PurchaseItem[];
   saleState:string,
   total:number
}
