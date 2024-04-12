import { Injectable } from '@angular/core';
import {User} from "../../models/user/user";
import {Observable, Subscription} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = this.authService.url+"user/";

  private subs: Subscription = new Subscription();

  constructor(private client: HttpClient, private  authService:AuthService) {

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
}
