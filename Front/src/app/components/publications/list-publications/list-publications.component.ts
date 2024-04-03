import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PublicationsService} from "../../../services/publications/publications.service";
import {Router} from "@angular/router";
import {Publication} from "../../../models/publication/publication";
import {PublicationMin} from "../../../models/publication/publication-min";

@Component({
  selector: 'app-list-publications',
  templateUrl: './list-publications.component.html',
  styleUrls: ['./list-publications.component.css']
})
export class ListPublicationsComponent  implements OnInit,OnDestroy {

  private subs: Subscription = new Subscription();
  form: FormGroup = this.fb.group({});

  list: PublicationMin[] = [
    {
      id: 1,
      name: "Cracion",
      description: "Border",
      type: "a",
      difficulty: "Dificil",
      calification: 1,
      image: "https://fcb-abj-pre.s3.amazonaws.com/img/jugadors/MESSI.jpg"
    }
  ];

  constructor(private fb: FormBuilder, private service: PublicationsService, private router: Router) {
    this.form = this.fb.group({
      text: ["", [Validators.maxLength(200 )]],
      type: [""],
      diffMin: [""],
      diffMax: [""],
      points: [""],
      mine: [""]
    });

  }
  ngOnInit(): void {

  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
  onSubmit(){
    if(this.form.invalid){
      alert("El formulario es invalido");
      this.form.markAllAsTouched();
      return;
    }

    let data = {
      "text": this.form.controls['text'].value,
      "type": this.form.controls['type'].value,
      "diffMin": this.form.controls['diffMin'].value,
      "diffMax": this.form.controls['diffMax'].value,
      "points": this.form.controls['points'].value,
      "mine": this.form.controls['mine'].value,
    }

    console.log(data);

    this.subs.add(
      this.service.postSearch(data).subscribe(
        {
          next: value => {
            alert("La yerba fue guardada con éxito");
          },
          error: err => { alert("Hubo un error al buscar"); }
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
