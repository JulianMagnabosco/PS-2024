import { Injectable } from '@angular/core';
import {Loginuser} from "../../models/user/loginuser";
import {Observable, Subscription} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./auth.service";
import {User} from "../../models/user/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = "api/user/";

  constructor(private client: HttpClient) {

  }
  get(id: any){
    return this.client.get<any>(this.baseUrl + id);
  }
  getAll(text: any){
    if(text!=""){
      return this.client.get<any>(this.baseUrl + "list?text="+ text);
    }else {
      return this.client.get<any>(this.baseUrl + "list");
    }
  }
  put(data: any){
    return this.client.get<any>(this.baseUrl + "mod", data);
  }
}
