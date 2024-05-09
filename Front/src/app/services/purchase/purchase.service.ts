import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PurchaseService {

  private baseUrl = "api/sell/";

  constructor(private client: HttpClient) {
  }

  postSale(data: any): Observable<any> {
    return this.client.post(this.baseUrl + "reg", data);
  }

  getPurchases(firstDate: string, lastDate: string, user: string) {
    return this.client.get(this.baseUrl + "purchases?firstDate=" + firstDate + "&lastDate=" + lastDate + "&user=" + user);
  }

  getSells(firstDate: string, lastDate: string, user: string) {
    return this.client.get(this.baseUrl + "sells?firstDate=" + firstDate + "&lastDate=" + lastDate + "&user=" + user);
  }

  getDeliveries(user: string) {
    return this.client.get(this.baseUrl + "deliveries?user=" + user);
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
