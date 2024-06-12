import {Component, ElementRef, HostListener, ViewChild} from '@angular/core';
import {AuthService} from "./services/user/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {PublicationsService} from "./services/publications/publications.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'COMOloHAGO';

  @ViewChild('searchSuggs') searchBarE!: ElementRef;
  @HostListener('document:click', ['$event'])
  clickout(event:any) {
    if(this.focusSearch && !this.searchBarE.nativeElement.parentElement.contains(event.target)){
      this.focusSearch=false
    }
  }

  subs:Subscription=new Subscription();
  searchBarVal:string="";
  focusSearch=false;
  suggs:any=[]
  constructor(public service:AuthService, private router:Router, private activeRoute:ActivatedRoute,
              private pubService:PublicationsService) {
    this.subs.add(
      this.activeRoute.queryParams.subscribe({
        next: value => {
          this.searchBarVal=value["text"]||""
        }
        }
      )
    )
  }

  salir(){
    this.service.logout()
  }
  chargeSuggs(value:string){
    this.focusSearch=true;
    this.subs.add(this.pubService.getSuggs(value).subscribe({
      next: list => {
        this.suggs=list;
      }
    }))
  }


  search(value:string){
    this.router.navigate(["explore"], {
      queryParams: {
        "text": value
      },
      replaceUrl: true
    })
  }

  protected readonly console = console;
}
