import { Injectable } from '@angular/core';
import {User} from "../../models/user/user";
import {Observable, Subscription} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./auth.service";
import {environment} from "../../../environments/environment";
import {Userget} from "../../models/user/userget";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = environment.apiUrl+"user/";

  constructor(private client: HttpClient) {

  }
  get(id: any){
    return this.client.get<any>(this.baseUrl + id);
  }
  getByName(name: any):Observable<Userget>{
    return this.client.get<Userget>(this.baseUrl +"name/"+ name);
  }
  getAll(text: any){
    if(text!=""){
      return this.client.get<any>(this.baseUrl + "list?text="+ text);
    }else {
      return this.client.get<any>(this.baseUrl + "list");
    }
  }
}
