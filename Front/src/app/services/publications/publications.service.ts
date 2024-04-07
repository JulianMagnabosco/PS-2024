import { Injectable } from '@angular/core';
import {User} from "../../models/user/user";
import {HttpClient, HttpHeaders, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {Publication} from "../../models/publication/publication";
import {UserService} from "../user/user.service";
import {DomSanitizer} from "@angular/platform-browser";

@Injectable({
  providedIn: 'root'
})
export class PublicationsService {

  url = this.userService.url
  private baseUrl = this.url+"pub/";
  constructor(private client: HttpClient, private userService:UserService, private sanitizer: DomSanitizer) { }

  postPublication(user: any):Observable<any>{
    return this.client.post(this.baseUrl + "new", user);
  }
  postImages(data: any):Observable<any>{
    return this.client.post(this.baseUrl + "image", data,{});
  }
  oldsearch():Observable<any>{
        return this.client.get(this.baseUrl + "list", {
          headers: new HttpHeaders({'Authorization': 'Bearer ' + this.userService.token})
        })
  }
  search(search: any):Observable<any>{
    return this.client.post(this.baseUrl + "search",search, {
      headers: new HttpHeaders({'Authorization': 'Bearer ' + this.userService.token})
    })
  }

  get(id: string):Observable<any>{
    return this.client.get(this.baseUrl + id);
  }

  getImages(pub: string, index: string):Observable<any>{
    return this.client.get(this.baseUrl+ "image", {
      params: {
        pub:pub,
        index:index
      }
    });
  }


}
