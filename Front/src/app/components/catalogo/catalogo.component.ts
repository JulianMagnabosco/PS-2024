import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {ProfileService} from "../../services/profile/profile.service";
import {PublicationsService} from "../../services/publications/publications.service";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-catalogo',
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent implements OnInit,OnDestroy{

  mes:string =""
  subs=new Subscription()
  image:any;
  constructor(private service:ProfileService,private pservice:PublicationsService
    , private sanitizer:DomSanitizer) {
  }

  ngOnInit(): void {
    this.subs.add(
      this.pservice.getImages("1","0").subscribe(
        {
          next: value => {
            console.log(value)


            this.image = value;
          }
        }
      )
    )
    // this.subs.add(
    //   this.service.get().subscribe(
    //     {
    //       next: value => {
    //         this.mes = "value"
    //       }
    //     }
    //   )
    // )
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe()
  }
}
