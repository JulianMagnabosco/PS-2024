import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {AuthService} from "../../../services/user/auth.service";
import {Router} from "@angular/router";
import {UserService} from "../../../services/user/user.service";
import Swal from 'sweetalert2'
import {cAlert} from "../../../services/custom-alert/custom-alert.service";
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit,OnDestroy {
  private subs: Subscription = new Subscription();
  form: FormGroup = this.fb.group({});

  constructor(private fb: FormBuilder, protected service: AuthService,
              private userService: UserService, private router: Router) {
    this.form = this.fb.group({
      username: ["", [Validators.required, Validators.maxLength(50 )]],
      password: ["", [Validators.required, Validators.maxLength(50)]]
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

    let username= this.form.controls['username'].value;
    let password= this.form.controls['password'].value;


    this.subs.add(
      this.service.postLogin(username,password).subscribe(
        {
          next: value => {
            // alert("Inicio de secion éxitoso");

            this.service.login(value)

            this.router.navigate(["/home"])
          },
          error: err => {
            if(err["status"]==401){
              cAlert("error","No existe usuario con esas credenciales"
              );
              // alert("No existe usuario con esas credenciales")
            }else {
              cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
            }
          }
        }
      )
    );
  }

  exit(){
    this.router.navigate(["/register"]);
  }

  checkPasswords: ValidatorFn = (group: AbstractControl):  ValidationErrors | null => {
    let pass = group.get('password')?.value;
    let confirmPass = group.get('password2')?.value
    return pass === confirmPass ? null : { notSame: true }
  }

}
