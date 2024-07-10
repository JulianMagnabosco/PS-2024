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
import {PublicationMin} from "../../../models/publication/publication-min";

@Component({
  selector: 'app-show-user',
  templateUrl: './show-user.component.html',
  styleUrl: './show-user.component.css'
})
export class ShowUserComponent  implements OnInit, OnDestroy{

  private subs: Subscription = new Subscription();

  user:User={
    dateTime: "",
    direction: "",
    dni: "",
    dniType: "",
    email: "",
    floor: "",
    iconUrl: "",
    id: "",
    idState: "",
    lastname: "",
    cvu: "",
    name: "",
    numberDir: "",
    phone: "",
    postalNum: "",
    role: "",
    room: "",
    state: "",
    username: "",
    same:false
  };


  list: PublicationMin[] = [
  ];
  elements=1;
  pages=0;

  size=10;
  page=0;

  sort="CALF"
  constructor(private service: UserService,protected pubService: PublicationsService,
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

  changeSort(sort:string){
    this.sort=sort
    this.chargeList(0)
  }
  chargeList(page:number){
    this.page=page;

    if(this.page>this.pages-1){
      this.page=this.pages-1;
    }
    if(this.page<=0){
      this.page=0;
    }
    let data = {
      "text": "@"+this.user.username,
      "materials": "",
      "type": "NONE",
      "diffMin": "1",
      "diffMax": "4",
      "points": "0",
      "mine": false,
      "sort": this.sort,
      "page": this.page,
      "size": this.size
    }
    this.subs.add(
      this.pubService.search(data).subscribe(
        {
          next: value => {
            this.elements=value["elements"]
            this.pages=value["pages"]
            this.list=value["list"]
          },
          error: err => {
            console.log(err)
            cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
          }
        }
      )
    );
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
                    this.chargeList(0);
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

