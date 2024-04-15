import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {UserService} from "../../../services/user/user.service";
import {User} from "../../../models/user/user";

@Component({
  selector: 'app-list-users',
  templateUrl: './list-users.component.html',
  styleUrl: './list-users.component.css'
})
export class ListUsersComponent implements OnInit,OnDestroy {

  private subs: Subscription = new Subscription();
  form: FormGroup = this.fb.group({});

  data: any ;

  list: User[] = [
  ];

  countTotal=1;
  size=3;
  page=0;
  constructor(private fb: FormBuilder, private service: UserService, private router: Router) {
    this.form = this.fb.group({
      text: ["", [Validators.maxLength(200 )]]
    });
  }
  ngOnInit(): void {
    this.charge(0)
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  get pages(){
    return Array(Math.ceil(this.countTotal/this.size)).fill(0).map((x,i)=>i);
  }

  charge(page: number){
    this.page=page;
    if(page<0){
      this.page=0;
    }

    if(page>Math.ceil(this.countTotal/this.size)-1){
      this.page=Math.ceil(this.countTotal/this.size)-1;
    }

    if(this.form.invalid){
      alert("El formulario es invalido");
      this.form.markAllAsTouched();
      return;
    }

    this.data = this.form.controls['text'].value


    this.subs.add(
      this.service.getAll(this.data).subscribe(
        {
          next: value => {
            this.countTotal=value["countTotal"]
            this.list=value["list"]
          },
          error: err => {
            console.log(err)
            alert("Hubo un error al buscar"); }
        }
      )
    );
  }

  go(id:string){
    this.router.navigate(["/user/"+id])

  }
}
