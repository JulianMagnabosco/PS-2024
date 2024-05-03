import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {AuthService} from "../../../services/user/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit,OnDestroy {
  private subs: Subscription = new Subscription();
  form: FormGroup = this.fb.group({});

  constructor(private fb: FormBuilder, private service: AuthService, private router: Router) {
    this.form = this.fb.group({
      name: ["", [Validators.required, Validators.maxLength(50 )]],
      email: ["", [Validators.required, Validators.email]],
      password: ["", [Validators.required, Validators.maxLength(50)]],
      password2: ["", [Validators.required, Validators.maxLength(50)]]
    },{
      validators: [this.checkPasswords, this.checkName]
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

    let user = {
      "username": this.form.controls['name'].value,
      "password": this.form.controls['password'].value,
      "email": this.form.controls['email'].value,
      "role": "USER"
    }

    console.log(user);

    this.subs.add(
      this.service.postUser(user).subscribe(
        {
          next: value => {
            alert("La usuario fue guardado con éxito");
            this.exit();
          },
          error: err => { alert("Hubo un error al guardar"); }
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
    let namecorrect:string = group.get('name')?.value;
    return !namecorrect.includes("@") ? null : { invalidName: true }
  }
}

