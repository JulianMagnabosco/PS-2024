import { Component } from '@angular/core';
import {AuthService} from "../../services/user/auth.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  constructor(public service:AuthService) {

  }
}
