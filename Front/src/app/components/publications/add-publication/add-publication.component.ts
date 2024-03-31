import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {UserService} from "../../../services/user/user.service";
import {Router} from "@angular/router";
import {PublicationsService} from "../../../services/publications/publications.service";

@Component({
  selector: 'app-add-publication',
  templateUrl: './add-publication.component.html',
  styleUrls: ['./add-publication.component.css']
})
export class AddPublicationComponent implements OnInit,OnDestroy {
  showConditions=true
  showMaterials=true
  showSteps=true
  showPurchasedata=true

  private subs: Subscription = new Subscription();
  form: FormGroup = this.fb.group({});

  constructor(private fb: FormBuilder, private service: PublicationsService, private router: Router) {
    this.form = this.fb.group({
      name: ["", [Validators.required, Validators.maxLength(50 )]],
      description: ["", [Validators.required]],
      image: [""],
      type: ["", [Validators.required]],
      difficulty: ["", [Validators.required]],
      conditions: this.fb.array([],{validators:Validators.required}),
      materials: this.fb.array([],{validators:Validators.required}),
      steps: this.fb.array([]),
      purchasedata: [""]
    });

  }
  ngOnInit(): void {  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  get detailsConditions(){
    return this.form.get("conditions") as FormArray
  }
  addDetailCondition(){
    let v = this.fb.group({
      text: ["",[Validators.required,Validators.minLength(500)]]
    })
    this.detailsConditions.push(v)
    this.detailsConditions.markAsTouched()
  }
  removeDetailCondition(id:number){
    this.detailsConditions.removeAt(id)
  }

  get detailsMaterials(){
    return this.form.get("materials") as FormArray
  }
  addDetailMaterials(){
    let v = this.fb.group({
      text: ["",[Validators.required,Validators.minLength(500)]]
    })
    this.detailsMaterials.push(v)
    this.detailsMaterials.markAsTouched()
  }
  removeDetailMaterials(id:number){
    this.detailsMaterials.removeAt(id)
  }

  onSubmit(){
    if(this.form.invalid){
      alert("El formulario es invalido");
      this.form.markAllAsTouched();
      return;
    }

    let data = {
      "name": this.form.controls['name'].value,
      "description": this.form.controls['description'].value,
      "image": this.form.controls['image'].value,
      "type": this.form.controls['type'].value,
      "difficulty": this.form.controls['difficulty'].value,
      "conditions": this.form.controls['conditions'].value,
      "materials": this.form.controls['materials'].value,
      "steps": this.form.controls['steps'].value,
      "canSold": this.form.controls['purchasedata'].value!="",
      "purchasedata": this.form.controls['purchasedata'].value
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
