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
  id=0;

  private subs: Subscription = new Subscription();

  @ViewChild(FormPublicationComponent) fp?: FormPublicationComponent;

  constructor() {

  }

  charge(id:number){
    this.id=id
    this.selectDraft=false;
  }
  ngOnInit(): void {
    this.charge(0);
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }


}
