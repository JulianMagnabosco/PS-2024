import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PurchaseService {

  private baseUrl = "api/sell/";
  constructor(private client: HttpClient) { }

  postSingleSale(data: any):Observable<any>{
    return this.client.post(this.baseUrl + "regsingle", data);
  }

  getPurchases(firstDate:string,lastDate:string,user:string){
    return this.client.get(this.baseUrl + "purchases?firstDate="+ firstDate +"&lastDate="+ lastDate +"&user="+ user );
  }
  getSells(firstDate:string,lastDate:string,user:string){
    return this.client.get(this.baseUrl + "sells?firstDate="+ firstDate +"&lastDate="+ lastDate +"&user="+ user );
  }
  getDeliveries(user:string){
    return this.client.get(this.baseUrl + "deliveries?user="+ user );
  }
}
