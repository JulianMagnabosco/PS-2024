import { Injectable } from '@angular/core';
import {Loginuser} from "../../models/user/loginuser";
import {HttpClient, HttpHeaders, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {Publication} from "../../models/publication/publication";
import {AuthService} from "../user/auth.service";
import {DomSanitizer} from "@angular/platform-browser";

@Injectable({
  providedIn: 'root'
})
export class PublicationsService {

  private baseUrl = "api/pub/";
  constructor(private client: HttpClient) { }

  postPublication(user: any):Observable<any>{
    return this.client.post(this.baseUrl + "new", user);
  }
  postImages(data: any):Observable<any>{
    return this.client.post(this.baseUrl + "image", data,{});
  }
  postCalification(data: any):Observable<any>{
    return this.client.post(this.baseUrl + "cal", data,{});
  }
  search(search: any):Observable<any>{
    return this.client.post(this.baseUrl + "search",search)
  }

  get(id: string,user:string):Observable<any>{
    return this.client.get(this.baseUrl + id+"/"+user);
  }

  getImages(pub: string, index: string):Observable<any>{
    return this.client.get(this.baseUrl+ "image", {
      params: {
        pub:pub,
        index:index
      }
    });
  }


  delete(id: string):Observable<any>{
    return this.client.delete(this.baseUrl + id);
  }
}
