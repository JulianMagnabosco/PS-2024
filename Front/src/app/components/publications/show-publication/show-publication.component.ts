import {Component, OnDestroy, OnInit} from '@angular/core';
import {Publication} from "../../../models/publication/publication";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {PublicationsService} from "../../../services/publications/publications.service";
import {DomSanitizer} from "@angular/platform-browser";
import {AuthService} from "../../../services/user/auth.service";
import {PurchaseService} from "../../../services/purchase/purchase.service";
import {cAlert, cConfirm} from "../../../services/custom-alert/custom-alert.service"

@Component({
  selector: 'app-show-publication',
  templateUrl: './show-publication.component.html',
  styleUrls: ['./show-publication.component.css']
})
export class ShowPublicationComponent implements OnInit, OnDestroy{
  notfound=false;

  private subs: Subscription = new Subscription();

  countBuy = 1;
  publication:Publication={
    deleted: false,
    dateTime: "",
    difficultyValue: 0,
    video: "",
    userIconUrl: "", username: "",
    id: 1,
    name: "",
    description: "",
    type: "",
    difficulty: "",
    userId: 1,
    calification: 0,
    myCalification: 0,
    sections: [  ],
    canSold: false,
    price: 0,
    count: 0
  };
  constructor(private service: PublicationsService, public userService: AuthService,
              private activeRoute:ActivatedRoute, private router: Router,
              private purchaseService:PurchaseService) {

  }
  ngOnInit(): void {
    this.charge();
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  // imageGet(baseImage:any){
  //   // let retrieveResonse = baseImage;
  //   // let base64Data = retrieveResonse.picByte;
  //   // return  'data:image/png;base64,' + base64Data;
  //   console.log(JSON.stringify(baseImage));
  //   let objectURL = 'data:image/png;base64,' + baseImage;
  //
  //   return  this.sanitizer.bypassSecurityTrustUrl(objectURL);
  // }

  get photos(){
    return this.publication.sections.filter((s) => s.type=="PHOTO")
  }

  get conditions(){
    return this.publication.sections.filter((s) => s.type=="COND")
  }
  get materials(){
    return this.publication.sections.filter((s) => s.type=="MAT")
  }

  get steps(){
    return this.publication.sections
      .filter((s) => s.type=="STEP")
      .sort((a,b) => a.number-b.number)
  }

  calificate(points :number){
    this.subs.add(
      this.service.postCalification({
        pubId:this.publication.id,
        value:points
      }).subscribe(
        {
          next: value => {
            // Swal.fire({
            //   title: "Exito",
            //   text: "Error inesperado en el servidor, revise su conexion a internet",
            //   icon: "error"
            // });
            // alert("Calificado")
            this.charge()
          }
        }
      )
    );
  }

  goUser(){
    this.router.navigate(["/user/"+this.publication.userId])
  }

  buy(){
    let data = {
      items: [
        {
          idPub: this.publication.id,
          count: this.countBuy
        }
      ]
    }
    this.subs.add(
      this.purchaseService.postSale(data).subscribe(
        {
          next: value => {
            // console.log(value["preference"]["initPoint"])
            window.location.href = value["preference"]["initPoint"]
          },
          error: err => {
            console.log(err)
            if(err.status==400){
              cAlert("error","El usuario no posee los datos de compra completos");
            }else {
              cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
            }
          }
        }
      )
    );
  }

  charge(){
    let id="" ;
    this.subs.add(
      this.activeRoute.params.subscribe(
        {
          next: value => {
            id = value["id"]
            this.subs.add(
              this.service.get(id).subscribe(
                {
                  next: value => {
                    this.publication=value
                    console.log(value)
                  },
                  error: err => {
                    this.notfound=true;
                  }
                }
              )
            );
          }
        }
      )
    );
  }

  delete(){
    cConfirm("¿Quieres eliminar el borrador?").then((value)=>{
      if(value.isConfirmed){
        this.subs.add(
          this.service.delete(this.publication.id.toString()).subscribe({
            next: value => {
              this.router.navigate(["/explore"])
            },
            error:err => {
              cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
              // alert("Hubo un error al eliminar");
            }
          })
        )
      }
    })
  }

  addCart(){
    let data = {
      pubId: this.publication.id,
      value: this.countBuy
    }
    this.subs.add(
      this.service.postCart(data).subscribe({
        next: value => {
          cAlert("success","Añadido");
          // alert("Añadido al carrito");
        },
        error:err => {
          // alert("Hubo un error al añadir al carrito");
          cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
        }
      })
    )

  }

  checkValue(){
    this.countBuy=this.countBuy<1||!this.countBuy?1:this.countBuy
  }
}
