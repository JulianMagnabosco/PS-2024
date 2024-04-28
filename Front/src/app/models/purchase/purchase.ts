import {Purchaseitem} from "./purchaseitem";

export interface Purchase {
   id:number;
   dateTime:string;
   details:Purchaseitem[];
   saleState:string
}
