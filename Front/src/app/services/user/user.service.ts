import { Injectable } from '@angular/core';
import {Loginuser} from "../../models/user/loginuser";
import {Observable, Subscription} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./auth.service";
import {User} from "../../models/user/user";
import {LoadingService} from "../loading/loading.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = "api/user/";

  constructor(private client: HttpClient, private loadService:LoadingService) {
  }

  get loading(){
    return this.loadService.loading
  }
  get(id: any){
    return this.client.get<any>(this.baseUrl + id);
  }
  getAll(data: any){
    return this.client.get<any>(this.baseUrl + "list", {params: data});
  }
  getDealers(){
    return this.client.get<any>(this.baseUrl + "dealers" );
  }
  getStates(){
    return this.client.get<any>(this.baseUrl + "states");
  }

  getImages(id:string):Observable<any>{
    return this.client.get("api/image/user/"+id, {responseType: "blob"});
  }
  put(data: any){
    return this.client.put<any>(this.baseUrl + "mod", data);
  }
  putRole(data: any){
    return this.client.put<any>(this.baseUrl + "role", null, {params: data});
  }
}
