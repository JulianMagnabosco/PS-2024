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
}
