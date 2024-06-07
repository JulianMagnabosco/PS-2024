import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {LoadingService} from "../loading/loading.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private baseUrl = "api/not/";
  constructor(private client: HttpClient, private loadService:LoadingService) {
  }

  get loading(){
    return this.loadService.loading
  }

  getAll(size: any):Observable<any>{
    return this.client.get(this.baseUrl + "list?size="+size);
  }
}
