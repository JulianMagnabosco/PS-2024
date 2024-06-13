import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserService} from "../../../services/user/user.service";
import {AuthService} from "../../../services/user/auth.service";
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {cAlert} from "../../../services/custom-alert/custom-alert.service";

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css'
})
export class ResetPasswordComponent implements OnInit,OnDestroy{
  phase=0;
  loading=true;

  private subs: Subscription = new Subscription();

  emailForm: FormGroup = this.fb.group({});

  form: FormGroup = this.fb.group({});
  tips: { okName:boolean, okEmail:boolean, points:number}={ okName:true,okEmail:true,points:0 }

  constructor(private fb: FormBuilder, protected authService: AuthService, private router: Router) {
    this.emailForm = this.fb.group({
      email: ["", [Validators.required, Validators.email]],
    });

    this.form = this.fb.group({
      token: ["", [Validators.required]],
      password: ["", [Validators.required]],
      password2: ["", [Validators.required]]
    },{
      validators: [this.checkPasswords]
    });

  }

  ngOnInit(): void {
    // this.subs.add(
    //   this.emailForm.valueChanges.subscribe({
    //     next: value =>  {
    //       this.test();
    //     }
    //   }));
    this.subs.add(
      this.form.valueChanges.subscribe({
        next: value =>  {
          this.test();
        }
      }));
  }
  test(){
    let user = {
      "username": "a",
      "password": this.form.controls['password'].value||"a",
      "email": this.emailForm.controls['email'].value||"a"
    }
    this.subs.add(
      this.authService.postTestUser(user).subscribe(
        {
          next: value => {
            this.tips=value;
          }
        }
      )
    );
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
  back(){
    this.phase=0
  }

  onSubmit(){

    this.subs.add(this.authService.postRequestPassword(
      this.emailForm.controls["email"].value).subscribe({
      next: value => {
        this.phase=1;
      },
      error: err => {
        if(err["status"]==404){
          cAlert("error","No existe usuario con ese mail");
        }else {
          cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
        }
      }
    }))
  }

  saveSubmit(){
    let user = {
      "token": this.form.controls['token'].value,
      "password": this.form.controls['password'].value,
      "email": this.emailForm.controls['email'].value
    }
    this.subs.add(this.authService.postChangePassword(user).subscribe({
      next: value => {
        cAlert("success","Contraseña Guardada").then(value1 => {
          this.router.navigate(["/home"])
        });
      },
      error: err => {
        if(err["status"]==404){
          cAlert("error","Token Invalido");
        }else {
          cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
        }
      }
    }))
  }
  checkPasswords: ValidatorFn = (group: AbstractControl):  ValidationErrors | null => {
    let pass = group.get('password')?.value;
    let confirmPass = group.get('password2')?.value
    return pass === confirmPass ? null : { notSame: true }
  }
}
