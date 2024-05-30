import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {Subscription} from "rxjs";
import {PublicationMin} from "../../../models/publication/publication-min";
import {PublicationsService} from "../../../services/publications/publications.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {cAlert, cConfirm} from "../../../services/custom-alert/custom-alert.service"

@Component({
  selector: 'app-drafts',
  templateUrl: './drafts.component.html',
  styleUrl: './drafts.component.css'
})
export class DraftsComponent implements OnInit,OnDestroy {

  private subs: Subscription = new Subscription();

  list: PublicationMin[] = [
  ];

  @Output() selectEvent= new EventEmitter<any>();

  constructor(private service: PublicationsService,
              private router: Router) {
  }
  ngOnInit(): void {
    this.charge()
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }


  charge(){

    this.subs.add(
      this.service.getDrafts().subscribe(
        {
          next: value => {
            this.list=value
            if(this.list.length <1 ){
              this.selectEvent.emit(0);
            }
          },
          error: err => {
            console.log(err)
            cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
          }
        }
      )
    );
  }

  delete(id:number){
    cConfirm("¿Quieres eliminar el borrador?").then((value)=>{
      if(value.isConfirmed){
        this.subs.add(
          this.service.delete(id.toString()).subscribe(
            {
              next: value => {
                this.charge()
              },
              error: err => {
                console.log(err)
                cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
              }
            }
          )
        );
      }
    })
  }

  selectDraft(id:number){
    this.selectEvent.emit(id)

  }
}
