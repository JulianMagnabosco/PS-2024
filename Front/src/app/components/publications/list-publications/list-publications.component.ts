import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PublicationsService} from "../../../services/publications/publications.service";
import {Router} from "@angular/router";
import {Publication} from "../../../models/publication/publication";

@Component({
  selector: 'app-list-publications',
  templateUrl: './list-publications.component.html',
  styleUrls: ['./list-publications.component.css']
})
export class ListPublicationsComponent  implements OnInit,OnDestroy {

  private subs: Subscription = new Subscription();
  form: FormGroup = this.fb.group({});

  constructor(private fb: FormBuilder, private service: PublicationsService, private router: Router) {
    this.form = this.fb.group({
      name: ["", [Validators.required, Validators.maxLength(50 )]],
      description: ["", [Validators.required]],
      type: ["", [Validators.required]],
      difficulty: ["", [Validators.required]],
      image: [""],
      conditions: this.fb.array([],{validators:Validators.required}),
      materials: this.fb.array([],{validators:Validators.required}),
      steps: this.fb.array([]),
      cansold: [""],
      price: [""],
      count: [""]
    });

  }
  ngOnInit(): void {  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
  onSubmit(){
    if(this.form.invalid){
      alert("El formulario es invalido");
      this.form.markAllAsTouched();
      return;
    }

    let data: Publication = {
      "name": this.form.controls['name'].value,
      "description": this.form.controls['description'].value,
      "image": this.form.controls['image'].value,
      "type": this.form.controls['type'].value,
      "difficulty": this.form.controls['difficulty'].value,
      "conditions": this.form.controls['conditions'].value,
      "materials": this.form.controls['materials'].value,
      "steps": this.form.controls['steps'].value,
      "canSold": this.form.controls['canSold'].value,
      "price": this.form.controls['price'].value,
      "count": this.form.controls['count'].value
    }

    console.log(data);

    this.subs.add(
      this.service.postPublication(data).subscribe(
        {
          next: value => {
            alert("La yerba fue guardada con éxito");
            this.router.navigate(["/explore"])
          },
          error: err => { alert("Hubo un error al guardar"); }
        }
      )
    );
  }

}
