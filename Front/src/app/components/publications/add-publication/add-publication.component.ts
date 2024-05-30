import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Subscription} from "rxjs";
import {
  FormArray,
  FormBuilder,
  FormGroup,
  Validators
} from "@angular/forms";
import {AuthService} from "../../../services/user/auth.service";
import {Router} from "@angular/router";
import {PublicationsService} from "../../../services/publications/publications.service";
import {Section} from "../../../models/publication/section";
import {Publication} from "../../../models/publication/publication";
import {cAlert} from "../../../services/custom-alert/custom-alert.service"
import {FormPublicationComponent} from "../form-publication/form-publication.component";

@Component({
  selector: 'app-add-publication',
  templateUrl: './add-publication.component.html',
  styleUrls: ['./add-publication.component.css']
})
export class AddPublicationComponent implements OnInit,OnDestroy {
  selectDraft = true;

  private subs: Subscription = new Subscription();

  @ViewChild(FormPublicationComponent) fp?: FormPublicationComponent;

  constructor(private service: PublicationsService,
              private router: Router) {

  }
  get form(){
    return this.fp?.form
  }

  charge(id:number){
    this.fp?.charge(id);
    this.selectDraft=false;
  }
  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  onSubmit(draft:boolean){
    this.fp?.onSubmit(draft?"draft":"add")
  }

}
