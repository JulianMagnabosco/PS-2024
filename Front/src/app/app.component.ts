import {Component, ViewChild} from '@angular/core';
import {AuthService} from "./services/user/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'COMOloHAGO';

  subs:Subscription=new Subscription();
  searchBarVal:string="";
  constructor(public service:AuthService, private router:Router, private activeRoute:ActivatedRoute) {
    this.subs.add(
      this.activeRoute.queryParams.subscribe({
        next: value => {
          this.searchBarVal=value["text"]||""
        }
        }
      )
    )
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
