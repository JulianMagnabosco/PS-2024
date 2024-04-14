import { Injectable } from '@angular/core';
import {User} from "../../models/user/user";
import {HttpClient} from "@angular/common/http";
import {map, Observable, Subscription} from "rxjs";
import {environment} from "../../../environments/environment";
import {Userget} from "../../models/user/userget";


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  user?:Userget;

  private baseUrl = environment.apiUrl+"auth/";

  constructor(private client: HttpClient) {
  }

  login(token:string):string{
    sessionStorage.setItem("app.token",token);
    return token;
  }
  logout(){
    sessionStorage.setItem("app.token","");
  }
  postLogin(username: string, password:string):Observable<string>{
    const httpOptions = {
    headers: {
      Authorization: 'Basic ' + window.btoa(username + ':' + password)
    },
  };
    return this.client.post<string>("/signin", null, httpOptions);
  }

  postUser(user: any):Observable<any>{
      return this.client.post(this.baseUrl + "signup", user);
  }
}
