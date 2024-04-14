import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PublicationsService} from "../../../services/publications/publications.service";
import {Router} from "@angular/router";
import {PublicationMin} from "../../../models/publication/publication-min";

@Component({
  selector: 'app-list-publications',
  templateUrl: './list-publications.component.html',
  styleUrls: ['./list-publications.component.css']
})
export class ListPublicationsComponent  implements OnInit,OnDestroy {

  private subs: Subscription = new Subscription();
  form: FormGroup = this.fb.group({});

  data: any ;

  list: PublicationMin[] = [
  ];
  countTotal=1;
  size=3;
  page=0;

  constructor(private fb: FormBuilder, private service: PublicationsService, private router: Router) {
    this.form = this.fb.group({
      text: ["", [Validators.maxLength(200 )]],
      type: ["NONE"],
      diffMin: ["1"],
      diffMax: ["4"],
      points: ["0"],
      mine: [false]
    });
  }
  ngOnInit(): void {
    this.charge(0)
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  get pages(){
    return Array(Math.ceil(this.countTotal/this.size)).fill(0).map((x,i)=>i);
  }
  clear(){
    this.form.setValue({

      text: "",
      type: "NONE",
      diffMin: "1",
      diffMax: "4",
      points: "0",
      mine: false
    })
  }
  charge(page: number){
    this.page=page;
    if(page<0){
      this.page=0;
    }

    if(page>Math.ceil(this.countTotal/this.size)-1){
      this.page=Math.ceil(this.countTotal/this.size)-1;
    }

    if(this.form.invalid){
      alert("El formulario es invalido");
      this.form.markAllAsTouched();
      return;
    }

    this.data = {
      "text": this.form.controls['text'].value,
      "type": this.form.controls['type'].value,
      "diffMin": this.form.controls['diffMin'].value,
      "diffMax": this.form.controls['diffMax'].value,
      "points": this.form.controls['points'].value,
      "mine": this.form.controls['mine'].value,
      "page": this.page,
      "size": this.size
    }


    this.subs.add(
      this.service.search(this.data).subscribe(
        {
          next: value => {
            this.countTotal=value["countTotal"]
            this.list=value["list"]
          },
          error: err => {
            console.log(err)
            alert("Hubo un error al buscar"); }
        }
      )
    );
  }

  go(id:number){
    this.router.navigate(["/pub/"+id])

  }
  gonew(){
    this.router.navigate(["/publicate"])

  }
}
