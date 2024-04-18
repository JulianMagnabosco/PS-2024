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

@Component({
  selector: 'app-mod-user',
  templateUrl: './mod-user.component.html',
  styleUrl: './mod-user.component.css'
})
export class ModUserComponent implements OnInit, OnDestroy {

  private subs: Subscription = new Subscription();
  user: User = {
    id: "",
    username: "",
    role: "",
    email: "",
    iconUrl: "",
    token: "",
    direction: "", floor: "", lastname: "", name: "", numberDir: "", postalNum: "", room: "", state: "",
    idState: ""
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
            console.log(value)
            this.user = value
            let userdata = {
              "username": this.user.username,
              "password": "",
              "password2": "",
              "icon": this.user.iconUrl != "",
              "email": this.user.email,
              "name": this.user.name||"",
              "lastname": this.user.lastname||"",
              "state": this.user.idState,
              "direction": this.user.direction||"",
              "numberDir": this.user.numberDir||"",
              "postalNum": this.user.postalNum||"",
              "floor": this.user.floor||"",
              "room": this.user.room||""
            }
            this.form.setValue(userdata)
          },
          error: err => {
            alert("Hubo un error al cargar");
          }
        }
      )
    );
  }

  submit() {
    if (this.form.invalid) {
      alert("El formulario es invalido");
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
      "idState": this.form.controls['state'].value,
      "direction": this.form.controls['direction'].value,
      "numberDir": this.form.controls['numberDir'].value,
      "postalNum": this.form.controls['postalNum'].value,
      "floor": this.form.controls['floor'].value,
      "room": this.form.controls['room'].value
    }
    console.log(JSON.stringify(userdata))
    let data = new FormData();
    data.append("data", JSON.stringify(userdata))
    data.append("icon", this.icon)

    this.subs.add(
      this.userService.put(data).subscribe(
        {
          next: value => {
            console.log(value)
            this.authService.logout()
            this.router.navigate(["/user/" + this.user.id]);
          },
          error: err => {
            alert("Hubo un error al cargar");
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
