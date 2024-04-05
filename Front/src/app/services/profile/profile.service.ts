import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {UserService} from "../user/user.service";
import {Subscription} from "rxjs";
import {User} from "../../models/user/user";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  baseUrl = this.userService.url
  token = ""
  private subs: Subscription = new Subscription();

  constructor(private client: HttpClient, private userService: UserService) {
  }

  // get() {
  //
  //     console.log(this.userService.token)
  //     const headers = new HttpHeaders()
  //         // .set('Authorization',"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYWEiLCJleHAiOjE5MjgxNzkxOTl9.HaOJrSwJRZxS9DiUGGxTs4OxS241ctcexWF2-0TPKW0")
  //       // .set('Access-Control-Allow-Origin','*')
  //       // .set('Access-Control-Allow-Methods','GET, POST, PUT, DELETE')
  //       // .set('Access-Control-Allow-Headers','*')
  //       // .set('Access-Control-Allow-Credentials','true')
  //     ;
  //     return this.client.get(this.url + "/auth/ping", {headers: headers})
  //
  //
  // }

  get(){

   const headers = new HttpHeaders()
    .set('Authorization',"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImVtYWlsIjoiYUBhIiwiZXhwIjoxNzEyMTkxOTkxfQ.g-pRQZ7f5G7NMYstJnc8-w_TMk9AG6T6dRYeQbrIeTc")
    // let headers: HttpHeaders = new HttpHeaders({
    //   Authorization: "Bearer "+ this.userService.token,
    //   'Access-Control-Allow-Origin':'*'
    // })
    console.log(headers)
    return this.client.get(this.baseUrl + "pub/list",{headers:headers});
  }

}
