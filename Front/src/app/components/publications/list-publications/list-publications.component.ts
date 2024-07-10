import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PublicationsService} from "../../../services/publications/publications.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {PublicationMin} from "../../../models/publication/publication-min";
import {cAlert} from "../../../services/custom-alert/custom-alert.service"

@Component({
  selector: 'app-list-publications',
  templateUrl: './list-publications.component.html',
  styleUrls: ['./list-publications.component.css']
})
export class ListPublicationsComponent  implements OnInit,OnDestroy {

  private subs: Subscription = new Subscription();
  form: FormGroup = this.fb.group({});

  data: any ;

  list: PublicationMin[] = [
  ];
  elements=1;
  pages=0;

  size=10;
  page=0;

  constructor(private fb: FormBuilder, protected service: PublicationsService,
              private router: Router, private activatedRoute: ActivatedRoute) {
    this.form = this.fb.group({
      text: [""],
      materials: [""],
      type: ["NONE"],
      diffMin: ["1"],
      diffMax: ["4"],
      sort: ["CALF"],
      points: ["0"],
      mine: [false]
    });
  }
  ngOnInit(): void {
    this.subs.add(
      this.activatedRoute.queryParams.subscribe({
        next: value => {
          this.form.patchValue(value)
          this.charge(value["page"])
        }
      })
    )
    this.subs.add(
      this.form.get("diffMin")?.valueChanges.subscribe({
        next: value => {
          if(value>this.form.get("diffMax")?.value){
            this.form.get("diffMax")?.setValue(this.form.get("diffMin")?.value)
          }
        }
      })
    )
    this.subs.add(
      this.form.get("diffMax")?.valueChanges.subscribe({
        next: value => {
          if(value<this.form.get("diffMin")?.value){
            this.form.get("diffMin")?.setValue(this.form.get("diffMax")?.value)
          }
        }
      })
    )
    this.charge(0)
  }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }


  clear(){
    this.form.patchValue({
      text: "",
      materials: "",
      type: "NONE",
      diffMin: "1",
      diffMax: "4",
      points: "0",
      mine: false
    })
  }
  changeSort(sortType:string){
    this.form.controls['sort'].setValue(sortType);
    this.charge(0)
  }
  charge(page: number){
    this.page=page;

    if(this.page>this.pages-1){
      this.page=this.pages-1;
    }
    if(this.page<=0){
      this.page=0;
    }

    this.data = {
      "text": this.form.controls['text'].value,
      "materials": this.form.controls['materials'].value,
      "type": this.form.controls['type'].value,
      "diffMin": this.form.controls['diffMin'].value,
      "diffMax": this.form.controls['diffMax'].value,
      "points": this.form.controls['points'].value,
      "mine": this.form.controls['mine'].value,
      "sort": this.form.controls['sort'].value,
      "page": this.page,
      "size": this.size
    }

    var newParams: {[k: string]: any} = {};
    if( this.data.text != "") newParams["text"] = this.data.text
    if( this.data.materials != "") newParams["materials"] = this.data.materials
    if( this.data.type != "NONE") newParams["type"] = this.data.type
    if( this.data.diffMin != "1") newParams["diffMin"] = this.data.diffMin
    if( this.data.diffMax != "4") newParams["diffMax"] = this.data.diffMax
    if( this.data.points != "0") newParams["points"] = this.data.points
    if( this.data.mine) newParams["mine"] = true
    newParams["sort"] = this.data.sort
    newParams["page"] = this.page


    this.router.navigate([],{
      relativeTo: this.activatedRoute,
      queryParams: newParams as Params,
      replaceUrl: true
    })

    this.subs.add(
      this.service.search(this.data).subscribe(
        {
          next: value => {
            this.elements=value["elements"]
            this.pages=value["pages"]
            this.list=value["list"]
          },
          error: err => {
            console.log(err)
            cAlert("error","Error inesperado en el servidor, revise su conexion a internet");
          }
        }
      )
    );
  }

}
