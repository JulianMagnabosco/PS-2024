import {Component, OnDestroy, OnInit} from '@angular/core';
import {map, Observable, Subscription} from "rxjs";
import {
  AbstractControl,
  AsyncValidatorFn,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {AuthService} from "../../../services/user/auth.service";
import {Router} from "@angular/router";
import {cAlert} from "../../../services/custom-alert/custom-alert.service"

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit,OnDestroy {
  private subs: Subscription = new Subscription();
  form: FormGroup = this.fb.group({});

  tips: { okName:boolean, okEmail:boolean, points:number}={ okName:true,okEmail:true,points:0 }

  constructor(private fb: FormBuilder, private service: AuthService, private router: Router) {
    this.form = this.fb.group({
      name: ["", [Validators.required, Validators.maxLength(50 ), this.checkName]],
      email: ["", [Validators.required, Validators.email]],
      password: ["", [Validators.required, Validators.maxLength(50)]],
      password2: ["", [Validators.required, Validators.maxLength(50)]],
      terms: [false, [Validators.requiredTrue]]
    },{
      validators: [this.checkPasswords]
    });

  }

  ngOnInit(): void {
    this.subs.add(
      this.form.valueChanges.subscribe({
        next: value =>  {
          this.test();
        }
      })
    )
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

    let user = {
      "username": this.form.controls['name'].value,
      "password": this.form.controls['password'].value,
      "email": this.form.controls['email'].value,
    }

    this.subs.add(
      this.service.postUser(user).subscribe(
        {
          next: value => {
            cAlert("success","La usuario fue registrado con éxito").then((value)=>{
              this.exit();
            });
            // alert("La usuario fue guardado con éxito");
          },
          error: err => {

              cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
          }
        }
      )
    );
  }
  test(){
    let user = {
      "username": this.form.controls['name'].value,
      "password": this.form.controls['password'].value,
      "email": this.form.controls['email'].value
    }
    this.subs.add(
      this.service.postTestUser(user).subscribe(
        {
          next: value => {
            this.tips=value;
          }
        }
      )
    );
  }

  exit(){
    this.router.navigate(["/login"]);
  }

  checkPasswords: ValidatorFn = (group: AbstractControl):  ValidationErrors | null => {
    let pass = group.get('password')?.value;
    let confirmPass = group.get('password2')?.value
    return pass === confirmPass ? null : { notSame: true }
  }
  checkName: ValidatorFn = (group: AbstractControl):  ValidationErrors | null => {
    const conditionRegex = /([^a-zA-Z0-9_\.\/\(\)\-\s])/g;
    return !conditionRegex.test(group.value) ? null : { invalidName: true }
  }
  //
  // createValidator(): AsyncValidatorFn {
  //   return (control: AbstractControl): Observable<ValidationErrors|null> => {
  //     let user = {
  //       "username": this.form.controls['name'].value,
  //       "password": this.form.controls['password'].value,
  //       "email": this.form.controls['email'].value,
  //     }
  //     return this.service.postTestUser(user).pipe(
  //       map((value: any) => value ? null : {invalidAsync: true})
  //     );
  //   };
  // }
}

