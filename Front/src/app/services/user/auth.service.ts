import { Injectable } from '@angular/core';
import {Loginuser} from "../../models/user/loginuser";
import {HttpClient} from "@angular/common/http";
import {map, Observable, Subscription} from "rxjs";
import {User} from "../../models/user/user";
import {UserService} from "./user.service";


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = "api/auth/";

  constructor(private client: HttpClient, private userService:UserService) {
  }

  get user():Loginuser|undefined{
    try {
      return JSON.parse( sessionStorage.getItem("app.user") || "");
    }catch (e) {
      return undefined
    }
  }

  login(user:Loginuser):string{
    sessionStorage.setItem("app.user",JSON.stringify(user));
    sessionStorage.setItem("app.token",user.token);
    return user.token;
  }
  logout(){
    sessionStorage.setItem("app.user","");
    sessionStorage.setItem("app.token","");
  }
  postLogin(username: string, password:string):Observable<any>{
    const httpOptions = {
      headers: {
        Authorization: 'Basic ' + window.btoa(username + ':' + password)
      },
    };
    return this.client.post(this.baseUrl +"signin", null, httpOptions);
  }

  postUser(user: any):Observable<any>{
      return this.client.post(this.baseUrl + "signup", user);
  }

  postTestUser(user: any):Observable<any>{
    return this.client.post(this.baseUrl + "test/signup", user);
  }

  postRequestPassword(email: any):Observable<any>{
    return this.client.post(this.baseUrl + "reset/req?email="+ email, null);
  }
  postChangePassword(user: any):Observable<any>{
    return this.client.post(this.baseUrl + "reset", user);
  }
}
