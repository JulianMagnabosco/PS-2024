import { Component } from '@angular/core';
import {AuthService} from "./services/user/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'PS-Front';

  constructor(public service:AuthService) {

  }

  salir(){
    this.service.salir()
  }
}
