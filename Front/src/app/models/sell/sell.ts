import {PurchaseItem} from "../purchase/purchase-item";

export interface Sell {
  id:number;
  dateTime:string;
  saleState:string,
  buyer:string;
  pubId:number;
  name:string;
  imageUrl:string;
  count:number;
  total:number
}
