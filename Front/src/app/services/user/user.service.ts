import { Injectable } from '@angular/core';
import {User} from "../../models/user/user";
import {HttpClient} from "@angular/common/http";
import {map, Observable, Subscription} from "rxjs";
import {Login} from "../../models/login/login";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  usuarioActual?:User
  token:string|null=""

  url = "http://localhost:8080/";
  private baseUrl = this.url+"auth/";

  private subs: Subscription = new Subscription();

  constructor(private client: HttpClient) {


  }

  init() {
    this.usuarioActual = JSON.parse(localStorage.getItem("user")as string)
    return this.postLogin(this.usuarioActual).pipe(
      map((value) => {
        localStorage.setItem("token",value["token"])
        this.token=value["token"]
        console.log("inicio")
      })
    );
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
    localStorage.setItem("user","")
    localStorage.setItem("token","")
  }

}
