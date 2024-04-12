import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {PublicationMin} from "../../../models/publication/publication-min";
import {PublicationsService} from "../../../services/publications/publications.service";
import {Router} from "@angular/router";
import {User} from "../../../models/user/user";
import {UserService} from "../../../services/user/user.service";

@Component({
  selector: 'app-list-users',
  standalone: true,
    imports: [
        NgForOf,
        NgIf,
        ReactiveFormsModule
    ],
  templateUrl: './list-users.component.html',
  styleUrl: './list-users.component.css'
})
export class ListUsersComponent implements OnInit,OnDestroy {

  private subs: Subscription = new Subscription();
  form: FormGroup = this.fb.group({});

  data: any ;

  list: User[] = [
  ];

  constructor(private fb: FormBuilder, private service: UserService, private router: Router) {
    this.form = this.fb.group({
      text: ["", [Validators.maxLength(200 )]]
    });
  }
  ngOnInit(): void {
    this.charge()
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  charge(){
    if(this.form.invalid){
      alert("El formulario es invalido");
      this.form.markAllAsTouched();
      return;
    }

    this.data = this.form.controls['text'].value;


    this.subs.add(
      this.service.getAll(this.data).subscribe(
        {
          next: value => {
            this.list=value
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
