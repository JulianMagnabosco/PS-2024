import { Injectable } from '@angular/core';
import {User} from "../../models/user/user";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  usuarioActual?:User
  token:string=""

  private baseUrl = "http://localhost:8080/auth/";
  constructor(private client: HttpClient) { }

  login(user: User, token:string):User{
    this.usuarioActual=user
    this.token=token
    return user;
  }
  postLogin(user: any):Observable<any>{
    return this.client.post(this.baseUrl + "signin", user);
  }

  postUser(user: any):Observable<any>{
      return this.client.post(this.baseUrl + "signup", user);
  }
  salir(){
    this.usuarioActual=undefined
    this.token=""
  }

}
