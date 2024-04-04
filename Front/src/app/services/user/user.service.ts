import { Injectable } from '@angular/core';
import {User} from "../../models/user/user";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Login} from "../../models/login/login";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  usuarioActual?:User
  token:string|null=""

  private baseUrl = "http://localhost:8080/auth/";
  constructor(private client: HttpClient) {

    this.usuarioActual = JSON.parse(localStorage.getItem("user")as string)
    this.token = localStorage.getItem("token")

  }

  login(user: User, token:string):User{
    localStorage.setItem("user",JSON.stringify(user))
    localStorage.setItem("token",token)
    this.usuarioActual=user
    this.token=token
    return user;
  }
  postLogin(user: any):Observable<Login>{
    return this.client.post<Login>(this.baseUrl + "signin", user);
  }

  postUser(user: any):Observable<any>{
      return this.client.post(this.baseUrl + "signup", user);
  }
  salir(){
    this.usuarioActual=undefined
    this.token=""
  }

}
