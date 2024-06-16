import {Component, Input, OnChanges, OnDestroy, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {Subscription} from "rxjs";
import {AuthService} from "../../services/user/auth.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {DomSanitizer} from "@angular/platform-browser";
import {NotificationService} from "../../services/notification/notification.service";
import {Notification} from "../../models/notification/notification";

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css'
})
export class NotificationsComponent implements OnChanges,OnDestroy{
  @Input() userId?:string;
  @Input() timeEvent?:any;
  subs=new Subscription()
  list:Notification[]=[
    // {
    //   dateTime: '2024/12/12',
    //   code: 'Compraaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa',
    //   title: 'Compraaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa',
    //   text: 'Algo se comproaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaeeee'
    // },
    //
    // {
    //   dateTime: '1/1/1',
    //   code: 'Compra',
    //   title: 'Compra',
    //   text: 'Algo se compro'
    // },
    //
    // {
    //   dateTime: '1/1/1',
    //   code: 'Compra',
    //   title: 'Compra',
    //   text: 'Algo se compro'
    // },
    //
    // {
    //   dateTime: '1/1/1',
    //   code: 'Compra',
    //   title: 'Compra',
    //   text: 'Algo se compro'
    // }
  ]
  constructor(private service:NotificationService) {
  }

  ngOnChanges(): void {
    this.charge()
  }
  charge(){

    this.subs.add(this.service.getAll(5).subscribe({
      next: value => {
        this.list=value;
        this.list=this.list.map((c)=>{
          if(c.code.includes("pass") || c.code.includes("register") || c.code.includes("req")){
            c.direction="/user/"+this.userId;
          }
          else if(c.code.includes("cal")){
            c.direction="/pub/"+c.code.split("_")[1];
          }
          else {
            c.direction="/home";
          }
          return c
        })

        console.log(this.list)
      }
    }))
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe()
  }

}
