import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  url = "http://localhost:8080"
  token = ""
  constructor(private client : HttpClient) { }

  get(){
    // let headers: HttpHeaders = new HttpHeaders({
    //   Authorization: "Bearer "+ this.token
    // })
    // return this.client.get<any >(this.url+"/profile",{headers:headers})
    return this.client.get(this.url+"/ping",{responseType:"text"})
  }

}
