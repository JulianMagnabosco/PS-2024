import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {ProfileService} from "../../services/profile/profile.service";
import {PublicationsService} from "../../services/publications/publications.service";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.css']
})
export class TestComponent implements OnInit,OnDestroy{

  mes:string =""
  subs=new Subscription()
  image:any;
  constructor(private service:ProfileService
    , private sanitizer:DomSanitizer) {
  }

  ngOnInit(): void {

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
