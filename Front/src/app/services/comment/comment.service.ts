import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private baseUrl = "api/comm/";
  constructor(private client: HttpClient) { }

  post(user: any):Observable<any>{
    return this.client.post(this.baseUrl + "new", user);
  }
  getAll(id: any):Observable<any>{
    return this.client.get(this.baseUrl + "list/"+ id);
  }
}
