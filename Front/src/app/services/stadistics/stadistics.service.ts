import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class StadisticsService {

  private baseUrl = "api/stats/";
  constructor(private client: HttpClient) { }

  getUserStadistics(year: any):Observable<any>{
    return this.client.post(this.baseUrl + "new", user);
  }
}
