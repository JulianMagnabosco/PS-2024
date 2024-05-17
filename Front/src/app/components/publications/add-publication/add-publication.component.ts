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
import {Publication} from "../../../models/publication/publication";
import {cAlert} from "../../../services/custom-alert/custom-alert.service"

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

  selectDraft=true;
  dirtyForDraft=false;

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
      price: ["1"],
      count: ["1"]
    });
    this.form.get("price")?.disable()
    this.form.get("count")?.disable()

  }
  ngOnInit(): void {
    this.subs.add(this.form.valueChanges.subscribe(
      {
        next: value => {
          this.dirtyForDraft=true;
        }
      }
    ))
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
    this.subs.add(this.form.get("price")?.valueChanges.subscribe(
      {
        next: value => {
          if(!value) this.form.get("price")?.setValue(1);
        }
      }
    ))
    this.subs.add(this.form.get("count")?.valueChanges.subscribe(
      {
        next: value => {
          if(!value) this.form.get("count")?.setValue(1);
        }
      }
    ))
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  charge(id:number){
    this.selectDraft=false;
    if(id==0) return;
    this.subs.add(
      this.service.get(id.toString()).subscribe(
        {
          next: value => {
            this.setForm(value as Publication)
          },
          error: err => {

              cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
          }
        }
      )
    );
  }

  setForm(data:Publication){
    this.form.setValue({
      name: data.name,
      description: data.description,
      type: data.type,
      difficulty: data.difficultyValue,
      image: true,
      video: data.video,
      conditions: [],
      materials: [],
      steps: [],
      canSold: data.canSold,
      price: data.price,
      count: data.count
    })

    let sectionsPhoto = data.sections.filter((s) => s.type=="PHOTO");
    let sectionsCond = data.sections.filter((s) => s.type=="COND")
      .sort((a,b) => a.number-b.number);
    let sectionsMat = data.sections.filter((s) => s.type=="MAT")
      .sort((a,b) => a.number-b.number);
    let sectionsStep = data.sections.filter((s) => s.type=="STEP")
      .sort((a,b) => a.number-b.number);


    for (let s of sectionsPhoto){
      this.subs.add(
        this.service.getImages(s.imageUrl.replace("http://localhost:8080/api/image/pub/","")).subscribe(
          {
            next: value => {
              let v = value as Blob;
              this.pubImages.push(
                {url: s.imageUrl, file: new File([v],'image', {type: v.type})}
              )
            }
          }
        )
      )
    }
    this.video=data.video||""

    for (let s of sectionsCond){
      this.addDetailCondition(s.text);
    }
    for (let s of sectionsMat){
      this.addDetailMaterials(s.text);
    }
    for (let s of sectionsStep){
      if(!s.imageUrl) continue;
      this.subs.add(
        this.service.getImages(s.imageUrl.replace("http://localhost:8080/api/image/pub/","")).subscribe(
          {
            next: value => {
              let v = value as Blob;
              this.addDetailsSteps(s.text, s.imageUrl);
              this.stepImages.push(
                {url: s.imageUrl, file: new File([v],'image', {type: v.type})}
              )
            }
          }
        )
      )
    }
  }
  get detailsConditions(){
    return this.form.get("conditions") as FormArray
  }
  addDetailCondition(text:string = ""){
    let v = this.fb.group({
      text: ["",[Validators.required,Validators.maxLength(500)]]
    })
    v.setValue({text: text})
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
  addDetailMaterials(text:string = ""){
    let v = this.fb.group({
      text: ["",[Validators.required,Validators.maxLength(500)]]
    })
    v.setValue({text: text})
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
  addDetailsSteps(text:string = "" ,url:string = ""){
    let v = this.fb.group({
      text: ["",[Validators.required,Validators.maxLength(500)]],
      image: [""]
    })
    v.setValue({text: text, image: ""})
    this.detailsSteps.push(v)
    this.detailsSteps.markAsTouched()

    this.stepImages.push({url:url, file:new File([],"") })
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
  onSubmit(draft:boolean){
    if(this.form.invalid && !draft){
      alert("El formulario es invalido");
      this.form.markAllAsTouched();
      return;
    }
    if(draft){
      this.dirtyForDraft=false;
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
      "name": this.form.controls['name'].value,
      "description": this.form.controls['description'].value,
      "type": this.form.controls['type'].value,
      "difficulty": this.form.controls['difficulty'].value,
      "draft": draft,
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
            this.uploadImages(value["sections"], draft)
          },
          error: err => {
              cAlert("error","Error inesperado en el servidor, revise su conexion a internet"); }
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
  uploadImages(sections: Section[], draft:boolean){
    let data = new FormData()
    let indexes = ""

    let sectionsPhoto = sections.filter((s) => s.type=="PHOTO");
    let sectionsStep = sections.filter((s) => s.type=="STEP")
      .sort((a,b) => a.number-b.number);


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

    if(indexes==""&&draft){
      cAlert("error","Borrador guardada");
      return;
    }
    this.subs.add(
      this.service.postImages(data).subscribe(
        {
          next: value => {
            if(draft){
              cAlert("success","Borrador guardada");
              // alert("El borrador fue guardado con éxito");
            }else {
              cAlert("success","Publicacion guardada");
              // alert("La publicacion fue guardada con éxito");
              this.router.navigate(["/mypubs"])
            }
          },
          error: err => {
              cAlert("error","Error inesperado en el servidor, revise su conexion a internet"); }
        }
      )
    );
  }

}
