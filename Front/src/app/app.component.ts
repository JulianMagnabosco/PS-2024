import { Component } from '@angular/core';
import {AuthService} from "./services/user/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'COMOloHAGO';

  constructor(public service:AuthService, private router:Router) {

  }

  salir(){
    this.service.logout()
  }

  search(value:string){
    this.router.navigate(["explore"], {
      queryParams: {
        "text": value
      },
      replaceUrl: true
    })
  }
}
