import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {UserService} from "../user/user.service";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  url = "http://localhost:8080"
  token = ""
  constructor(private client: HttpClient, private userService:UserService) { }

  get(){
    // let headers: HttpHeaders = new HttpHeaders({
    //   Authorization: "Bearer "+ this.token
    // })
    // return this.client.get<any >(this.url+"/profile",{headers:headers})
    return this.client.get(this.url+"/auth/ping",{responseType:"text"})
  }
  get2(){

    let headers: HttpHeaders = new HttpHeaders({
      Authorization: "Bearer "+ this.userService.token
    })
    console.log(headers)
    return this.client.get(this.url + "/pub/list",{headers:headers});
  }

}
