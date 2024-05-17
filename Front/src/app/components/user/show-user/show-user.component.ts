import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {Subscription} from "rxjs";
import {Publication} from "../../../models/publication/publication";
import {PublicationsService} from "../../../services/publications/publications.service";
import {AuthService} from "../../../services/user/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DomSanitizer} from "@angular/platform-browser";
import {Loginuser} from "../../../models/user/loginuser";
import {UserService} from "../../../services/user/user.service";
import {User} from "../../../models/user/user";
import {cAlert} from "../../../services/custom-alert/custom-alert.service"

@Component({
  selector: 'app-show-user',
  templateUrl: './show-user.component.html',
  styleUrl: './show-user.component.css'
})
export class ShowUserComponent  implements OnInit, OnDestroy{

  private subs: Subscription = new Subscription();

  user:User={
    direction: "",
    dni: "",
    dniType: "",
    email: "",
    floor: "",
    iconUrl: "",
    id: "",
    idState: "",
    lastname: "",
    mpClient: "",
    mpSecret: "",
    name: "",
    numberDir: "",
    phone: "",
    postalNum: "",
    role: "",
    room: "",
    state: "",
    username: ""
  };
  constructor(private service: UserService,
              private activeRoute:ActivatedRoute, private router: Router) {

  }
  ngOnInit(): void {
    this.charge();
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  back(){
    this.router.navigate(["/users"])
  }

  charge(){
    let id="" ;
    this.subs.add(
      this.activeRoute.params.subscribe(
        {
          next: value => {
            id = value["id"]
            this.subs.add(
              this.service.get(id).subscribe(
                {
                  next: value => {

                    this.user=value
                  },
                  error: err => {

                    cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
                  }
                }
              )
            );
          }
        }
      )
    );
  }


}

