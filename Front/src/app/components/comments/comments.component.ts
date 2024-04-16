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

  list:Comment[]=[];
  countTotal:number=0;

  form: FormGroup = this.fb.group({});
  constructor(private fb: FormBuilder,private service: CommentService) {
    this.form = this.fb.group({
      text: ["", [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.subs.add(
      this.service.getAll(this.idPub).subscribe(
        {
          next: value => {
            this.countTotal=value["countTotal"]
            this.list=value["list"]
          }
        }
      )
    )
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  submit(){
    let data = {
      "pub":this.idPub,
      "userId":this.idUser,
      "text":this.form.controls["text"].value
    }

    this.subs.add(
      this.service.post(data).subscribe(
        {
          next: value => {
            this.countTotal=value["countTotal"]
            this.list=value["list"]
          }
        }
      )
    )
  }

}
