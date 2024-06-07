import {Component, OnDestroy, OnInit} from '@angular/core';
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
export class NotificationsComponent implements OnInit,OnDestroy{
  subs=new Subscription()
  list:Notification[]=[
    {
      dateTime: '1/1/1',
      code: 'Compra',
      title: 'Compra',
      text: 'Algo se compro'
    },

    {
      dateTime: '1/1/1',
      code: 'Compra',
      title: 'Compra',
      text: 'Algo se compro'
    },

    {
      dateTime: '1/1/1',
      code: 'Compra',
      title: 'Compra',
      text: 'Algo se compro'
    },

    {
      dateTime: '1/1/1',
      code: 'Compra',
      title: 'Compra',
      text: 'Algo se compro'
    }
  ]
  constructor(private service:NotificationService) {
  }

  ngOnInit(): void {
    this.charge()
  }
  charge(){

    this.subs.add(this.service.getAll(5).subscribe({
      next: value => {
        this.list=value;
        this.list=this.list.map((c)=>{
          c.code=c.code.split("_")[0];
          return c
        })
      }
    }))
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe()
  }

}
