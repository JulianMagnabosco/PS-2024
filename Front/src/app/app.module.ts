import {APP_INITIALIZER, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { StadisticsComponent } from './components/stadistics/stadistics.component';
import { TestComponent } from './components/test/test.component';
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import {NgxEchartsDirective, provideEcharts} from "ngx-echarts";
import { RegisterComponent } from './components/user/register/register.component';
import { LoginComponent } from './components/user/login/login.component';
import { ListPublicationsComponent } from './components/publications/list-publications/list-publications.component';
import { AddPublicationComponent } from './components/publications/add-publication/add-publication.component';
import { ShowPublicationComponent } from './components/publications/show-publication/show-publication.component';
import {UserService} from "./services/user/user.service";
import {NgOptimizedImage} from "@angular/common";
import {SweetAlert2Module} from "@sweetalert2/ngx-sweetalert2";

export function initializeApp(initService: UserService) {
  return () => initService.init();
}

@NgModule({
  declarations: [
    AppComponent,
    StadisticsComponent,
    TestComponent,
    RegisterComponent,
    LoginComponent,
    ListPublicationsComponent,
    AddPublicationComponent,
    ShowPublicationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgxEchartsDirective,
    NgOptimizedImage,
    SweetAlert2Module.forRoot()
  ],
  providers: [
    provideEcharts(),
    {
      provide: APP_INITIALIZER,
      multi: true,
      useFactory: initializeApp,
      deps: [UserService]
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
