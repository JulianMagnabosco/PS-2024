import {Component, OnDestroy, OnInit} from '@angular/core';
import {Publication} from "../../../models/publication/publication";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {PublicationsService} from "../../../services/publications/publications.service";
import {DomSanitizer} from "@angular/platform-browser";
import {AuthService} from "../../../services/user/auth.service";

@Component({
  selector: 'app-show-publication',
  templateUrl: './show-publication.component.html',
  styleUrls: ['./show-publication.component.css']
})
export class ShowPublicationComponent implements OnInit, OnDestroy{

  private subs: Subscription = new Subscription();

  publication:Publication={
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
              private sanitizer:DomSanitizer) {

  }
  ngOnInit(): void {
    this.charge();
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  imageGet(baseImage:any){
    // let retrieveResonse = baseImage;
    // let base64Data = retrieveResonse.picByte;
    // return  'data:image/png;base64,' + base64Data;
    console.log(JSON.stringify(baseImage));
    let objectURL = 'data:image/png;base64,' + baseImage;

    return  this.sanitizer.bypassSecurityTrustUrl(objectURL);
  }

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
        userId:this.userService.user?.id,
        pubId:this.publication.id,
        value:points
      }).subscribe(
        {
          next: value => {
            alert("Calificado")
            this.charge()
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
              this.service.get(id,this.userService.user?.id||"1").subscribe(
                {
                  next: value => {
                    console.log(value)
                    this.publication=value
                  },
                  error: err => {
                    alert("Hubo un error al cargar");
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
    this.subs.add(
      this.service.delete(this.publication.id.toString()).subscribe({
        next: value => {
          this.router.navigate(["/explore"])
        },
        error:err => {
          alert("Hubo un error al eliminar");
        }
      })
    )
  }

}
