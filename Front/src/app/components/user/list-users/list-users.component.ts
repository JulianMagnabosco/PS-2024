import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {UserService} from "../../../services/user/user.service";
import {User} from "../../../models/user/user";
import {UserMin} from "../../../models/user/user-min";
import {cAlert, cFire} from "../../../services/custom-alert/custom-alert.service";

@Component({
  selector: 'app-list-users',
  templateUrl: './list-users.component.html',
  styleUrl: './list-users.component.css'
})
export class ListUsersComponent implements OnInit,OnDestroy {

  private subs: Subscription = new Subscription();
  form: FormGroup = this.fb.group({});

  list: UserMin[] = [
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

    let data = {text:this.form.controls["text"].value,
      page:this.page,
      size:this.size}


    this.subs.add(
      this.service.getAll(data).subscribe(
        {
          next: value => {
            this.countTotal=value["countTotal"]
            this.list=value["list"]
          },
          error: err => {
            cAlert("error","Error inesperado en el servidor, revise su conexion a internet"); }
        }
      )
    );
  }

  go(id:string){
    this.router.navigate(["/user/"+id])

  }
  edit(user:UserMin){
    var options:any = {};
    if(user.role!="ADMIN")
      options["ADMIN"] = "ADMIN";
    if(user.role!="USER")
      options["USER"] = "USER";
    if(user.role!="DELIVERY")
      options["DELIVERY"] = "DELIVERY";

    cFire({
      title: "¿Quiere cambiar el rol del usuario? (Actualmente: "+user.role+")",
      input: "select",
      inputOptions: options,
      showConfirmButton: true,
      showCancelButton: true
    }).then(value => {
      if(!value.isConfirmed) return;

      this.subs.add(
        this.service.putRole({id:user.id,role:value.value}).subscribe({
          next: value => {
            this.charge(this.page)
          },
          error:err => {
            cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
            // alert("Hubo un error al eliminar");
          }
        })
      )
    })

  }
}
