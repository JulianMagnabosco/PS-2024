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
import {SwalPortalTargets} from "@sweetalert2/ngx-sweetalert2";

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

  images: { url:any, file:File }[]=[{url:"assets/camera.png", file:new File([],"") }]

  constructor(private fb: FormBuilder, private service: PublicationsService,
              public readonly swalTargets: SwalPortalTargets, private router: Router) {
    this.form = this.fb.group({
      name: ["", [Validators.required, Validators.maxLength(50 )]],
      description: ["", [Validators.required]],
      type: ["", [Validators.required]],
      difficulty: ["", [Validators.required]],
      image: ["", [Validators.required]],
      video: [""],
      conditions: this.fb.array([]),
      materials: this.fb.array([]),
      steps: this.fb.array([]),
      canSold: [false],
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
    return this.form.get("steps") as FormArray
  }
  addDetailsSteps(){
    let v = this.fb.group({
      text: ["",[Validators.required,Validators.maxLength(500)]],
      image: [""]
    })
    this.detailsSteps.push(v)
    this.detailsSteps.markAsTouched()

    this.images.push({url:"assets/camera.png", file:new File([],"") })
  }
  moveDetailsSteps(id:number,dir:number){
    let val = this.detailsSteps.at(id).value;
    this.detailsSteps.removeAt(id)
    let d = this.fb.group({
      text: ["",[Validators.required,Validators.maxLength(500)]],
      image: [""]
    })
    d.setValue(val)
    this.detailsSteps.insert(id+dir,d)
    this.detailsSteps.markAsTouched()

    let img = this.images[id+1]
    this.images[id+1] = this.images[id+1+dir];
    this.images[id+1+dir] = img;
  }

  removeDetailsSteps(id:number){
    this.detailsSteps.removeAt(id)
    this.images.slice(id)
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
        "number":s+1,
        "type":"COND",
        "text":this.detailsConditions.controls[s].value["text"],
        "imageUrl":0
      })
    }
    for (let s in this.detailsMaterials.controls){
      sections.push({
        "number":s+1,
        "type":"MAT",
        "text":this.detailsMaterials.controls[s].value["text"],
        "imageUrl":0
      })
    }
    for (let s in this.detailsSteps.controls){
      sections.push({
        "number":s+1,
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
      "video": this.form.controls['video'].value,
      "sections": sections,
      "canSold": this.form.controls['canSold'].value,
      "price": this.form.controls['price'].value,
      "count": this.form.controls['count'].value
    }

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

  selectFile(event:any, index:number){
    if (event.target.files && event.target.files[0] && this.images.at(index)) {
      var reader = new FileReader();

      this.images[index].file = event.target.files[0]
      reader.readAsDataURL(event.target.files[0]); // read file as data url

      reader.onload = (event) => { // called once readAsDataURL is completed
        this.images[index].url=event.target?.result
      }
    }

  }


  uploadImages(pub:number){
    let data = new FormData()

    data.append("images",this.images[0].file);
    let indexes = "0"
    for (let s in this.detailsSteps.controls){
      // imgs.push(this.detailsSteps.controls[s].value["image"] as File)
      if(this.detailsSteps.controls[s].value["image"]){
        let i = parseInt(s)+1
        console.log(i)
        data.append("images",this.images[i].file);
        indexes += "_" + i
      }
    }

    data.append("pub",pub.toString());
    data.append("indexes",indexes);

    console.log(data.getAll("images"))

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
