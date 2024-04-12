import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {Subscription} from "rxjs";
import {Publication} from "../../../models/publication/publication";
import {PublicationsService} from "../../../services/publications/publications.service";
import {AuthService} from "../../../services/user/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DomSanitizer} from "@angular/platform-browser";
import {User} from "../../../models/user/user";
import {UserService} from "../../../services/user/user.service";
import {Userget} from "../../../models/user/userget";

@Component({
  selector: 'app-show-user',
  standalone: true,
    imports: [
        NgForOf,
        NgIf
    ],
  templateUrl: './show-user.component.html',
  styleUrl: './show-user.component.css'
})
export class ShowUserComponent  implements OnInit, OnDestroy{

  private subs: Subscription = new Subscription();

  user:Userget={
    id:"",
    username:"",
    role:"",
    email:"",
    iconUrl:"",
    token:"",
    direction: "", floor: "", lastname: "", name: "", numberDir: "", postalNum: "", room: "", state: ""
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
                    console.log(value)
                    this.user=value
                  },
                  error: err => {
                    alert("Hubo un error al cargar");
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

