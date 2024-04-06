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
import {Publication} from "../../../models/publication/publication";

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
      type: ["", [Validators.required]],
      difficulty: ["", [Validators.required]],
      image: [""],
      conditions: this.fb.array([]),
      materials: this.fb.array([]),
      steps: this.fb.array([]),
      cansold: [false],
      price: [""],
      count: [""]
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
      text: ["",[Validators.required,Validators.maxLength(500)]]
    })
    this.detailsConditions.push(v)
    this.detailsConditions.markAsTouched()
  }
  removeDetailCondition(id:number){
    this.detailsConditions.removeAt(id)
  }
//MAteriales
  get detailsMaterials(){
    return this.form.get("materials") as FormArray
  }
  addDetailMaterials(){
    let v = this.fb.group({
      text: ["",[Validators.required,Validators.maxLength(500)]]
    })
    this.detailsMaterials.push(v)
    this.detailsMaterials.markAsTouched()
  }
  removeDetailMaterials(id:number){
    this.detailsMaterials.removeAt(id)
  }
  //pasos
  get detailsSteps(){
    return this.form.get("conditions") as FormArray
  }
  addDetailsSteps(){
    let v = this.fb.group({
      text: ["",[Validators.required,Validators.maxLength(500)]],
      image: [""]
    })
    this.detailsSteps.push(v)
    this.detailsSteps.markAsTouched()
  }
  removeDetailsSteps(id:number){
    this.detailsSteps.removeAt(id)
  }


  //guardar
  onSubmit(){
    if(this.form.invalid){
      alert("El formulario es invalido");
      this.form.markAllAsTouched();
      return;
    }

    let sections:any[] = [];
    for (let s in this.detailsConditions.controls){
      sections.push({
        "number":s,
        "type":"COND",
        "text":this.detailsConditions.controls[s].value["text"],
        "imageUrl":0
      })
    }
    for (let s in this.detailsMaterials.controls){
      sections.push({
        "number":s,
        "type":"MAT",
        "text":this.detailsMaterials.controls[s].value["text"],
        "imageUrl":0
      })
    }
    for (let s in this.detailsSteps.controls){
      sections.push({
        "number":s,
        "type":"STEP",
        "text":this.detailsSteps.controls[s].value["text"],
        "imageUrl":0
      })
    }


    let data = {
      "name": this.form.controls['name'].value,
      "description": this.form.controls['description'].value,
      "type": this.form.controls['type'].value,
      "difficulty": this.form.controls['difficulty'].value,
      "sections": sections,
      "canSold": this.form.controls['canSold'].value,
      "price": this.form.controls['price'].value,
      "count": this.form.controls['count'].value
    }

    console.log(data);

    this.subs.add(
      this.service.postPublication(data).subscribe(
        {
          next: value => {
            alert("La publicacion fue guardada con éxito");
            this.uploadImages(value["id"])
          },
          error: err => { alert("Hubo un error al guardar"); }
        }
      )
    );
  }

  uploadImages(pub:number){
    let data = new FormData()
    let imgs = []
    imgs.push(this.form.get("image")?.value)
    let indexes = "0_"
    for (let s in this.detailsSteps.controls){
      imgs.push(this.detailsSteps.controls[s].value["image"])
      indexes += s+"_"
    }
    // data.append()

    this.subs.add(
      this.service.postImages(data).subscribe(
        {
          next: value => {
            alert("Imagenes guardada con éxito");
            this.router.navigate(["/explore"])
          },
          error: err => { alert("Hubo un error al guardar"); }
        }
      )
    );
  }

}
