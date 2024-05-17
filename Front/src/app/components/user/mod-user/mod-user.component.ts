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
import Swal from "sweetalert2";

@Component({
  selector: 'app-mod-user',
  templateUrl: './mod-user.component.html',
  styleUrl: './mod-user.component.css'
})
export class ModUserComponent implements OnInit, OnDestroy {

  private subs: Subscription = new Subscription();
  user: User = {
    direction: "",
    dni: "",
    dniType: "",
    email: "",
    floor: "",
    iconUrl: "",
    id: "",
    idState: "",
    lastname: "",
    mpClient: "",
    mpSecret: "",
    name: "",
    numberDir: "",
    phone: "",
    postalNum: "",
    role: "",
    room: "",
    state: "",
    username: ""
  };
  form: FormGroup = this.fb.group({});
  iconUrl: any;
  icon: any;

  constructor(private userService: UserService, private authService: AuthService,
              private fb: FormBuilder, private router: Router) {
    this.form = this.fb.group({
      username: ["", [Validators.required, Validators.maxLength(50)]],
      password: ["", [ Validators.maxLength(50)]],
      password2: ["", [ Validators.maxLength(50)]],
      email: ["", [Validators.required, Validators.email]],
      icon: [""],
      name: [""],
      lastname: [""],
      phone: [""],
      cvu: [""],
      dni: [""],
      dniType: [""],
      state: ["1", [Validators.required]],
      direction: ["", [Validators.maxLength(200)]],
      numberDir: [""],
      postalNum: [""],
      floor: [""],
      room: [""]
    }, {
      validators: this.checkPasswords
    });

  }

  ngOnInit(): void {
    this.charge();
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  charge() {
    let id = this.authService.user?.id;
    this.subs.add(
      this.userService.get(id).subscribe(
        {
          next: value => {

            this.user = value
            let userdata = {
              "username": this.user.username,
              "password": "",
              "password2": "",
              "icon": this.user.iconUrl != "",
              "email": this.user.email,

              "name": this.user.name||"",
              "lastname": this.user.lastname||"",
              "phone": this.user.phone||"",
              "dni": this.user.dni||"",
              "dniType": this.user.dniType||"",

              "state": this.user.idState,
              "direction": this.user.direction||"",
              "numberDir": this.user.numberDir||"",
              "postalNum": this.user.postalNum||"",
              "floor": this.user.floor||"",
              "room": this.user.room||"",

              "mpClient": this.user.mpClient||"",
              "mpSecret": this.user.mpSecret||""
            }
            this.form.setValue(userdata)
          },
          error: err => {

              Swal.fire({
                title: "Error",
                text: "Error inesperado en el servidor, revise su conexion a internet",
                icon: "error"
              });
          }
        }
      )
    );
  }

  submit() {
    if (this.form.invalid) {
      Swal.fire({
        title: "Error",
        text: "El formulario es invalido:"+this.form.errors,
        icon: "error"
      });
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
      "cvu": this.form.controls['cvu'].value,
      "dni": this.form.controls['dni'].value,
      "dniType": this.form.controls['dniType'].value,

      "idState": this.form.controls['state'].value,
      "direction": this.form.controls['direction'].value,
      "numberDir": this.form.controls['numberDir'].value,
      "postalNum": this.form.controls['postalNum'].value,
      "floor": this.form.controls['floor'].value,
      "room": this.form.controls['room'].value
    }
    let data = new FormData();
    data.append("data", JSON.stringify(userdata))
    data.append("icon", this.icon)

    this.subs.add(
      this.userService.put(data).subscribe(
        {
          next: value => {

            this.authService.logout()
            this.router.navigate(["/user/" + this.user.id]);
          },
          error: err => {

              Swal.fire({
                title: "Error",
                text: "Error inesperado en el servidor, revise su conexion a internet",
                icon: "error"
              });
          }
        }
      )
    );
  }

  selectIcon(event: any) {
    if (event.target.files) {
      this.form.get("icon")?.setValue(true)
      var reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]); // read file as data url

      this.icon = event.target.files[0];
      reader.onload = (event) => { // called once readAsDataURL is completed
        this.iconUrl = event.target?.result;
      }
    }
  }

  checkPasswords: ValidatorFn = (group: AbstractControl): ValidationErrors | null => {
    let pass = group.get('password')?.value;
    let confirmPass = group.get('password2')?.value
    return pass === confirmPass || pass == "" ? null : {notSame: true}
  }

}

