import { Injectable } from '@angular/core';
import {User} from "../../models/user/user";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  usuarioActual?:User
  listaUsuarios:User[]=[
    {name:"legajo@frc.utn.edu.ar",password:"11111111"},

    {name:"a@a",password:"11111111"}
  ]

  private baseUrl = "https://herb.nhorenstein.com/api/";
  constructor(private client: HttpClient) { }

  logear(usuario: User):boolean{
    if(this.listaUsuarios.find(u=>u.name==usuario.name && u.password==usuario.password)){
      this.usuarioActual=usuario
      return true
    }
    return false
  }

  postLogin(user: any):Observable<any>{
    return this.client.post(this.baseUrl + "login", user);
  }

  postUser(user: any):Observable<any>{
      return this.client.post(this.baseUrl + "user", user);
  }
  salir(){
    this.usuarioActual=undefined
  }

}
