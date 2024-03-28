import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {UserService} from "../../../services/user/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit,OnDestroy {
  private subs: Subscription = new Subscription();
  form: FormGroup = this.fb.group({});

  constructor(private fb: FormBuilder, private service: UserService, private router: Router) {
    this.form = this.fb.group({
      name: ["", [Validators.required, Validators.maxLength(50 )]],
      email: ["", [Validators.required, Validators.email]],
      password: ["", [Validators.required, Validators.maxLength(50)]],
      password2: ["", [Validators.required, Validators.maxLength(50)]]
    },{
      validators: this.checkPasswords
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
      "name": this.form.controls['name'].value,
      "email": this.form.controls['email'].value,
      "password": this.form.controls['password'].value
    }

    console.log(user);

    this.subs.add(
      this.service.postUser(user).subscribe(
        {
          next: value => {
            alert("La yerba fue guardada con éxito");
            this.router.navigate(["/herbs/list"]);
          },
          error: err => { alert("Hubo un error al guardar"); }
        }
      )
    );
  }

  checkPasswords: ValidatorFn = (group: AbstractControl):  ValidationErrors | null => {
    let pass = group.get('password')?.value;
    let confirmPass = group.get('password2')?.value
    return pass === confirmPass ? null : { notSame: true }
  }
}

