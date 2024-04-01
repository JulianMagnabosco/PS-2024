import { Injectable } from '@angular/core';
import {User} from "../../models/user/user";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Publication} from "../../models/publication/publication";

@Injectable({
  providedIn: 'root'
})
export class PublicationsService {

  private baseUrl = "http://localhost:8080/";
  constructor(private client: HttpClient) { }

  postPublication(user: Publication):Observable<any>{
    return this.client.post(this.baseUrl + "pub/new", user);
  }

}
