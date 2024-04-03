import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {ProfileService} from "../../services/profile/profile.service";

@Component({
  selector: 'app-catalogo',
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent implements OnInit,OnDestroy{

  mes:string =""
  subs=new Subscription()
  constructor(private service:ProfileService) {
  }

  ngOnInit(): void {
    this.subs.add(
      this.service.get().subscribe(
        {
          next: value => {
            this.mes = "value"
          }
        }
      )
    )
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe()
  }
}
