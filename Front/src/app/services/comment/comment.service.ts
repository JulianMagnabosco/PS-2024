import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {LoadingService} from "../loading/loading.service";

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private baseUrl = "api/comm/";
  constructor(private client: HttpClient, private loadService:LoadingService) {
  }

  get loading(){
    return this.loadService.loading
  }

  post(user: any):Observable<any>{
    return this.client.post(this.baseUrl + "new", user);
  }
  getAll(id: any):Observable<any>{
    return this.client.get(this.baseUrl + "list/"+ id);
  }
  delete(id: any):Observable<any>{
    return this.client.delete(this.baseUrl + "del/"+ id);
  }
}
