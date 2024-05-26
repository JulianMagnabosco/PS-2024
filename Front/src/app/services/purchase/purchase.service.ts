import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {LoadingService} from "../loading/loading.service";

@Injectable({
  providedIn: 'root'
})
export class PurchaseService {

  private baseUrl = "api/sell/";

  constructor(private client: HttpClient, private loadService:LoadingService) {
  }

  get loading(){
    return this.loadService.loading
  }

  postSale(data: any): Observable<any> {
    return this.client.post(this.baseUrl + "reg", data);
  }

  getPurchases(firstDate: string, lastDate: string, data: string) {
    return this.client.get(this.baseUrl + "purchases?firstDate=" + firstDate +
      "&lastDate=" + lastDate + "&name=" + data);
  }

  getSells(firstDate: string, lastDate: string, data: string) {
    return this.client.get(this.baseUrl + "sells?firstDate=" + firstDate +
      "&lastDate=" + lastDate + "&name=" + data);
  }

  getDeliveries() {
    return this.client.get(this.baseUrl + "deliveries");
  }

  putDeliveries(data: any) {
    return this.client.put(this.baseUrl + "delivery", data);
  }

  deleteSell(id: string) {
    return this.client.delete(this.baseUrl + "sell/" + id);
  }

}


export function stateClasses(state: string) {
  switch (state) {
    case "CANCELADA" :
    case "PERCANCE" :
      return "bg-danger text-white"
    case "PENDIENTE" :
      return "bg-info text-white"
    case "APROBADA" :
    case "ENTREGADO" :
    default :
      return "bg-success text-white"
  }
}
