import { Injectable } from '@angular/core';
import {User} from "../../models/user/user";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PublicationsService {

  private baseUrl = "https://herb.nhorenstein.com/api/";
  constructor(private client: HttpClient) { }

  postPublication(user: any):Observable<any>{
    return this.client.post(this.baseUrl + "login", user);
  }

}
