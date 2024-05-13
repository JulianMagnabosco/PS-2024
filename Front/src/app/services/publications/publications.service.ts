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

  postPublication(data: any):Observable<any>{
    return this.client.post(this.baseUrl + "new", data);
  }
  postImages(data: any):Observable<any>{
    return this.client.post(this.baseUrl + "image", data,{});
  }
  postCalification(data: any):Observable<any>{
    return this.client.post(this.baseUrl + "cal", data,{});
  }
  postCart(data: any):Observable<any>{
    return this.client.post(this.baseUrl + "cart", data,{});
  }
  search(search: any):Observable<any>{
    return this.client.post(this.baseUrl + "search",search)
  }
  getCart():Observable<any>{
    return this.client.get(this.baseUrl + "cart");
  }
  getDrafts():Observable<any>{
    return this.client.get(this.baseUrl + "drafts");
  }

  get(id: string):Observable<any>{
    return this.client.get(this.baseUrl + id);
  }

  getImages(id:string):Observable<any>{
    return this.client.get("api/image/pub/"+id, {responseType: "blob"});
  }

  putPublication(data: any):Observable<any>{
    return this.client.put(this.baseUrl + "mod", data);
  }


  delete(id: string):Observable<any>{
    return this.client.delete(this.baseUrl + id);
  }
}
