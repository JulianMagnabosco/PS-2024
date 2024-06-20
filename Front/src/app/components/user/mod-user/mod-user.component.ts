import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {User} from "../../../models/user/user";
import {UserService} from "../../../services/user/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Loginuser} from "../../../models/user/loginuser";
import {AuthService} from "../../../services/user/auth.service";
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  FormsModule, ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {cAlert, cConfirm} from "../../../services/custom-alert/custom-alert.service"

@Component({
  selector: 'app-mod-user',
  templateUrl: './mod-user.component.html',
  styleUrl: './mod-user.component.css'
})
export class ModUserComponent implements OnInit, OnDestroy {

  private subs: Subscription = new Subscription();
  user: User = {
    dateTime: "",
    direction: "",
    dni: "",
    dniType: "",
    email: "",
    floor: "",
    iconUrl: "",
    id: "",
    idState: "",
    lastname: "",
    cvu: "",
    name: "",
    numberDir: "",
    phone: "",
    postalNum: "",
    role: "",
    room: "",
    state: "",
    username: "",
    same:false
  };
  form: FormGroup = this.fb.group({});
  iconUrl: any;
  icon: any;

  canBuy:boolean=false;
  canSell:boolean=false;
  lackings:string[]=[];

  listStates:{id:number,name:string}[]=[];

  constructor(private userService: UserService, private authService: AuthService,
              private fb: FormBuilder, private router: Router) {
    this.form = this.fb.group({
      username: ["", [Validators.required, Validators.maxLength(50)]],
      password: ["", [ Validators.maxLength(50)]],
      password2: ["", [ Validators.maxLength(50)]],
      email: ["", [Validators.required, Validators.email]],
      icon: false,
      name: ["",[Validators.maxLength(200),Validators.pattern("[a-zA-Z]*")]],
      lastname: ["",[Validators.maxLength(200),Validators.pattern("[a-zA-Z]*")]],
      phone: ["", [Validators.pattern("[0-9]*"),Validators.minLength(7)]],
      cvu: ["", [Validators.pattern("[0-9]*"),Validators.minLength(20)]],
      dni: ["", [Validators.pattern("[0-9]*"),Validators.minLength(8)]],
      dniType: ["DNI"],
      state: ["1", [Validators.required]],
      direction: ["", [Validators.maxLength(200)]],
      numberDir: ["",Validators.pattern("[0-9]*")],
      postalNum: ["",Validators.pattern("[0-9]*")],
      floor: ["",Validators.pattern("[0-9]*")],
      room: ["",Validators.pattern("[0-9]*")]
    }, {
      validators: this.checkPasswords
    });

  }

  ngOnInit(): void {
    this.charge();
    this.subs.add(this.form.valueChanges.subscribe({
      next: value => {
        this.checkBuySell()
      }
    }))
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  charge() {
    this.subs.add(this.userService.getStates().subscribe({
      next: value => {
        this.listStates=value;
        this.listStates[0]={id:this.listStates[0].id,name:"-SELECCIONAR-"}
      }
    }))

    let id = this.authService.user?.id;
    this.subs.add(
      this.userService.get(id).subscribe(
        {
          next: value => {

            this.user = value
            this.iconUrl=this.user.iconUrl
            let userdata = {
              "username": this.user.username,
              "password": "",
              "password2": "",
              "icon": false,
              "email": this.user.email,

              "name": this.user.name||"",
              "lastname": this.user.lastname||"",
              "phone": this.user.phone||"",
              "dni": this.user.dni||"",
              "dniType": this.user.dniType||"DNI",

              "state": this.user.idState,
              "direction": this.user.direction||"",
              "numberDir": this.user.numberDir||"",
              "postalNum": this.user.postalNum||"",
              "floor": this.user.floor||"",
              "room": this.user.room||"",

              "cvu": this.user.cvu||""
            }
            this.form.setValue(userdata)

            this.setIcon();

            this.checkBuySell()
          },
          error: err => {

              cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
          }
        }
      )
    );
  }

  setIcon(){

    this.subs.add(
      this.userService.getImages(this.user.id).subscribe(
        {
          next: value => {
            let v = value as Blob;
            this.icon = v
          }
        }
      )
    )
  }

  checkBuySell(){
    this.lackings=[]
    this.canBuy=true;
    this.canSell=true;
    let userdata:{key:string,value:any}[] = [

      {key:"Nombre",value:this.form.controls['name'].value},
      {key:"Apellido",value: this.form.controls['lastname'].value},
      {key:"Teléfono",value: this.form.controls['phone'].value},
      {key:"DNI",value: this.form.controls['dni'].value},
      {key:"Tipo Dni",value: this.form.controls['dniType'].value},

      {key:"Provincia",value: this.form.controls['state'].value},
      {key:"Dirección",value: this.form.controls['direction'].value},
      {key:"Numeración",value: this.form.controls['numberDir'].value},
      {key:"Código Postal",value: this.form.controls['postalNum'].value}
    ]

    for (let l in userdata){
      if(!userdata[l].value) {
        this.canBuy=false;
        this.canSell=false;
        this.lackings.push(userdata[l].key)
      }
    }
    if(userdata.find((l)=>l.key=="Provincia")?.value==1){
      this.canBuy=false;
      this.canSell=false;
      this.lackings.push("Provincia")
    }

    if((!this.form.controls['cvu'].value || this.form.controls['cvu'].invalid) && this.canBuy){
      this.canSell=false;
      this.lackings.push("CVU")
    }
  }
  swalSubmit(){
    cConfirm("¿Guardar usuario?").then(value => {
      if(value.isConfirmed){
        this.submit();
      }
    })
  }

  submit() {
    if (this.form.invalid) {
      cAlert("error","El formulario es invalido:"+this.form.errors);
      this.form.markAllAsTouched();
      return;
    }

    let userdata = {
      "id": this.user.id,
      "username": this.form.controls['username'].value,
      "changePass": this.form.controls['password'].value != "",
      "password": this.form.controls['password'].value,
      "email": this.form.controls['email'].value,

      "name": this.form.controls['name'].value,
      "lastname": this.form.controls['lastname'].value,
      "phone": this.form.controls['phone'].value,
      "dni": this.form.controls['dni'].value,
      "dniType": this.form.controls['dniType'].value,

      "idState": this.form.controls['state'].value,
      "direction": this.form.controls['direction'].value,
      "numberDir": this.form.controls['numberDir'].value,
      "postalNum": this.form.controls['postalNum'].value,
      "floor": this.form.controls['floor'].value,
      "room": this.form.controls['room'].value,

      "cvu": this.form.controls['cvu'].value
    }
    let data = new FormData();
    data.append("data", JSON.stringify(userdata))
    data.append("icon", this.icon)

    this.subs.add(
      this.userService.put(data).subscribe(
        {
          next: value => {
            let userl = this.authService.user;
            if(userl){
              userl.canSell=this.canSell;
              userl.canBuy=this.canBuy;
              this.authService.setUser(userl)
            }
            this.router.navigate(["/user/" + this.user.id]);
          },
          error: err => {

              cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
          }
        }
      )
    );
  }

  selectIcon(event: any) {
    let size=0;
    for (let f of event.target.files){
      if(size > 5e+6) {
        cAlert("error","Archivo/s muy grande/s");
        return;
      }
      size+=f.size
    }
    if (event.target.files) {
      this.form.get("icon")?.setValue(true)
      this.form.get("icon")?.markAsDirty()
      var reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]); // read file as data url

      this.icon = event.target.files[0];
      reader.onload = (event) => { // called once readAsDataURL is completed
        this.iconUrl = event.target?.result;
      }
    }else {
      this.form.get("icon")?.setValue(false)
      this.form.get("icon")?.markAsDirty()
      this.icon=""
      this.iconUrl=""
    }
  }
  checkPasswords: ValidatorFn = (group: AbstractControl): ValidationErrors | null => {
    let pass = group.get('password')?.value;
    let confirmPass = group.get('password2')?.value
    return pass === confirmPass || pass == "" ? null : {notSame: true}
  }

}

