import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Subscription} from "rxjs";
import {
  FormArray,
  FormBuilder,
  FormGroup,
  Validators
} from "@angular/forms";
import {AuthService} from "../../../services/user/auth.service";
import {Router} from "@angular/router";
import {PublicationsService} from "../../../services/publications/publications.service";
import {Section} from "../../../models/publication/section";

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

  pubImages: {url: any, file: File }[]=[]
  stepImages: {url: any, file: File }[]=[]
  video:any=""
  @ViewChild('img1') img1?: ElementRef<HTMLDivElement>;


  constructor(private fb: FormBuilder, private service: PublicationsService,
              private userService: AuthService, private router: Router) {
    this.form = this.fb.group({
      name: ["", [Validators.required, Validators.maxLength(50 )]],
      description: ["", [Validators.required]],
      type: ["", [Validators.required]],
      difficulty: ["", [Validators.required]],
      image: [false, [Validators.requiredTrue]],
      video: [""],
      conditions: this.fb.array([]),
      materials: this.fb.array([]),
      steps: this.fb.array([]),
      canSold: [false],
      price: ["0"],
      count: ["0"]
    });
    this.form.get("price")?.disable()
    this.form.get("count")?.disable()

  }
  ngOnInit(): void {
    this.subs.add(this.form.get("canSold")?.valueChanges.subscribe(
      {
        next: value => {
          if(value){
            this.form.get("price")?.enable()
            this.form.get("count")?.enable()
            this.form.get("price")?.setValidators([Validators.min(1)])
            this.form.get("count")?.setValidators([Validators.min(1)])
          }else {
            this.form.get("price")?.disable()
            this.form.get("count")?.disable()
            this.form.get("price")?.clearValidators()
            this.form.get("count")?.clearValidators()
          }
        }
      }
    ))
  }
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

    this.stepImages.push({url:"assets/camera.png", file:new File([],"") })
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

    let img = this.stepImages[id+1]
    this.stepImages[id+1] = this.stepImages[id+1+dir];
    this.stepImages[id+1+dir] = img;
  }

  removeDetailsSteps(id:number){
    this.detailsSteps.removeAt(id)
    this.stepImages.slice(id)
  }


  //guardar
  onSubmit(){
    if(this.form.invalid){
      alert("El formulario es invalido");
      this.form.markAllAsTouched();
      return;
    }

    let sections:any[] = [];
    for (let s in this.pubImages){
      sections.push({
        "number":"",
        "type":"PHOTO",
        "text":""
      })
    }
    for (let s in this.detailsConditions.controls){
      sections.push({
        "number":s+1,
        "type":"COND",
        "text":this.detailsConditions.controls[s].value["text"]
      })
    }
    for (let s in this.detailsMaterials.controls){
      sections.push({
        "number":s+1,
        "type":"MAT",
        "text":this.detailsMaterials.controls[s].value["text"]
      })
    }
    for (let s in this.detailsSteps.controls){
      sections.push({
        "number":s+1,
        "type":"STEP",
        "text":this.detailsSteps.controls[s].value["text"]
      })
    }

    let data = {
      "user": this.userService.user?.id,
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
            this.uploadImages(value["sections"])
          },
          error: err => { alert("Hubo un error al guardar"); }
        }
      )
    );
  }

  selectPubImages(event:any){
    if (event.target.files) {
      this.form.get("image")?.setValue(true)
      this.pubImages = []
      for (let f of event.target.files){
        var reader = new FileReader();
        reader.readAsDataURL(f); // read file as data url

        reader.onload = (event) => { // called once readAsDataURL is completed
          this.pubImages.push({url: event.target?.result, file: f})
        }
      }
    }
  }
  selectStepFile(event:any, index:number){
    if (event.target.files && event.target.files[0] && this.stepImages.at(index)) {
      var reader = new FileReader();

      this.stepImages[index].file = event.target.files[0]
      reader.readAsDataURL(event.target.files[0]); // read file as data url

      reader.onload = (event) => { // called once readAsDataURL is completed
        this.stepImages[index].url=event.target?.result
      }
    }

  }
  changeVideo(event:any){
    let value=event.target.value as string
    value=value.replace("https://www.youtube.com/watch?v=","")
      .replace("https://www.youtube.com/shorts/","")
    this.video=value.split("&")[0];
  }

  uploadImages(sections: Section[]){
    let data = new FormData()
    let indexes = ""

    let sectionsPhoto = sections.filter((s) => s.type=="PHOTO");
    let sectionsStep = sections.filter((s) => s.type=="STEP")
      .sort((a,b) => a.number-b.number);

    console.log(sectionsPhoto)
    console.log(sectionsStep)

    for (let i in sectionsPhoto){
      data.append("images",this.pubImages[i].file);
      indexes += sectionsPhoto[i].id + "_"
    }

    for (let i in sectionsStep){
      if(this.detailsSteps.controls[i].value["image"]){
        data.append("images",this.stepImages[i].file);
        indexes += sectionsStep[i].id + "_"
      }
    }

    data.append("indexes",indexes);
    console.log(indexes)

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
