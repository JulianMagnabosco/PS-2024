import { Injectable } from '@angular/core';
import {User} from "../../models/user/user";
import {HttpClient} from "@angular/common/http";
import {map, Observable, Subscription} from "rxjs";
import {Login} from "../../models/login/login";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  user?:User;
  password?:string;

  url = "http://localhost:8080/";
  private baseUrl = this.url+"auth/";

  private subs: Subscription = new Subscription();

  constructor(private client: HttpClient) {
  }

  init() {
    try {
      this.user = JSON.parse(localStorage.getItem("user")as string)
      this.password = localStorage.getItem("password")as string
    }catch (e){

    }

    let data:Login= {username:this.user?.username||"",password:this.password||""}
    return this.postLogin(data).pipe(
      map((value) => {
        this.user=value
        localStorage.setItem("user",JSON.stringify(value))
      })
    );
  }

  login(user: User, password:string):User{
    localStorage.setItem("user",JSON.stringify(user))
    localStorage.setItem("password",password as string)
    this.user= user
    this.password=password
    return user;
  }
  postLogin(user: any):Observable<User>{
    return this.client.post<User>(this.baseUrl + "signin", user);
  }

  postUser(user: any):Observable<any>{
      return this.client.post(this.baseUrl + "signup", user);
  }
  salir(){
    this.user=undefined
    localStorage.setItem("user","")
  }
}
