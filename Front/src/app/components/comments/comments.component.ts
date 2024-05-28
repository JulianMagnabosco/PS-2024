import {Component, Input, OnChanges, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Publication} from "../../models/publication/publication";
import {PublicationsService} from "../../services/publications/publications.service";
import {AuthService} from "../../services/user/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DomSanitizer} from "@angular/platform-browser";
import {Comment} from "../../models/comment/comment";
import {CommentService} from "../../services/comment/comment.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {cConfirm} from "../../services/custom-alert/custom-alert.service";

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrl: './comments.component.css'
})
export class CommentsComponent implements OnChanges, OnDestroy{
  protected readonly document = document;

  private subs: Subscription = new Subscription();

  @Input() idPub:any;

  list:Comment[]=[];
  // list:Comment[]=[{
  //   id: 1,
  //   pub: 1,
  //   userId: 1,
  //   username: 'PAblo',
  //   userIconUrl: '',
  //   text: 'Comentario1',
  //   fatherText: '',
  //   childs: [{
  //     id: 2,
  //     pub: 1,
  //     userId: 1,
  //     username: 'Roller',
  //     userIconUrl: '',
  //     text: 'Comentario2',
  //     fatherText: '',
  //     childs: [],
  //     deleted:false
  //   }],
  //   deleted:false
  // }];
  countTotal:number=0;
  textList:string[]=[];

  constructor(private service: CommentService) {
  }

  ngOnChanges(): void {
    this.charge();
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  charge(){

    this.subs.add(
      this.service.getAll(this.idPub).subscribe(
        {
          next: value => {
            this.countTotal=value["countTotal"]
            this.textList.fill("",0,this.countTotal-1)
            this.list=value["list"]
          }
        }
      )
    )
  }
  submit(textInput:HTMLElement|null, id:number, gId:number){
    if(!textInput || !(textInput instanceof HTMLInputElement)){
      return
    }

    let data = {
      "pub":this.idPub,
      "father":id,
      "grandfather":gId,
      "text":textInput.value
    }

    this.subs.add(
      this.service.post(data).subscribe(
        {
          next: value => {
            this.charge();
            textInput.value=""
          }
        }
      )
    )
  }

  delete(id:number){
    cConfirm("¿Seguro que quiere eliminar el comentario?").then((value)=>{
      if(value.isConfirmed){
        this.subs.add(
          this.service.delete(id).subscribe(
            {
              next: value => {
                this.charge();
              }
            }
          )
        )
      }
    })
  }

  dissableOnCheck(textInputId:string,buttonInputId:string){
    const textInput = document.getElementById(textInputId)
    const buttonInput = document.getElementById(buttonInputId)
    if(!textInput || !(textInput instanceof HTMLInputElement) ||
      !buttonInput || !(buttonInput instanceof HTMLButtonElement)){
      return
    }
    if(!textInput.checkValidity())
      buttonInput.setAttribute("disabled",'true');
    else
      buttonInput.removeAttribute("disabled");
    return;
  }
}
