import { Component } from '@angular/core';
import {UserService} from "./services/user/user.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'PS-Front';

  constructor(public service:UserService) {

  }

  salir(){
    this.service.salir()
  }
}
