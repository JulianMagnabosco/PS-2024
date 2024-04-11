import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {PublicationsService} from "../../services/publications/publications.service";
import {DomSanitizer} from "@angular/platform-browser";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {UserService} from "../../services/user/user.service";

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.css']
})
export class TestComponent implements OnInit,OnDestroy{

  mes:string =""
  subs=new Subscription()
  image:any;
  constructor(private service:UserService,private http:HttpClient
    , private sanitizer:DomSanitizer) {
  }

  ngOnInit(): void {
    this.http.get("http://localhost:8080/api/v1/books", {
      headers: new HttpHeaders({'Authorization': 'Bearer ' + this.service.token})
    })
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
