import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Publication} from "../../models/publication/publication";
import {PublicationsService} from "../../services/publications/publications.service";
import {AuthService} from "../../services/user/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DomSanitizer} from "@angular/platform-browser";
import {Comment} from "../../models/comment/comment";
import {CommentService} from "../../services/comment/comment.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrl: './comments.component.css'
})
export class CommentsComponent implements OnInit, OnDestroy{

  private subs: Subscription = new Subscription();

  @Input() idUser:any;
  @Input() idPub:any;

  list:Comment[]=[{
    id: 1,
    pub: 1,
    userId: 1,
    username: 'PAblo',
    userIconUrl: '',
    text: 'Comentario1',
    fatherText: '',
    childs: [{
      id: 2,
      pub: 1,
      userId: 1,
      username: 'Roller',
      userIconUrl: '',
      text: 'Comentario2',
      fatherText: '',
      childs: []
    }]
  }];
  countTotal:number=0;
  textList:string[]=[];

  form: FormGroup = this.fb.group({});
  constructor(private fb: FormBuilder,private service: CommentService) {
    this.form = this.fb.group({
      text: ["", [Validators.required]],
    });
  }

  ngOnInit(): void {
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
            console.log(value["list"])
          }
        }
      )
    )
  }
  submit(textInput:HTMLElement|null, id:number, grandid:number){
    if(!textInput){
      return
    }
    if(!(textInput instanceof HTMLInputElement)){
      return
    }

    let data = {
      "pub":this.idPub,
      "user":this.idUser,
      "father":id,
      "grandfather":grandid,
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

  protected readonly document = document;
}
