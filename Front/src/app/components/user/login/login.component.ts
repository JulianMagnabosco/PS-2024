import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {AuthService} from "../../../services/user/auth.service";
import {Router} from "@angular/router";
import {User} from "../../../models/user/user";
import {Login} from "../../../models/login/login";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit,OnDestroy {
  private subs: Subscription = new Subscription();
  form: FormGroup = this.fb.group({});

  constructor(private fb: FormBuilder, private service: AuthService, private router: Router) {
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

    let data:Login = {
      username: this.form.controls['username'].value,
      password: this.form.controls['password'].value
    }

    this.subs.add(
      this.service.postLogin(data).subscribe(
        {
          next: value => {
            alert("Inicio de secion éxitoso");
            this.service.login(value,data.password)
            this.router.navigate(["/explore"])
          },
          error: err => {
            console.log(err)
            alert("Hubo un error");
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
