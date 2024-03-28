import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { StadisticsComponent } from './components/stadistics/stadistics.component';
import { CatalogoComponent } from './components/catalogo/catalogo.component';
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import {NgxEchartsDirective, provideEcharts} from "ngx-echarts";
import { RegisterComponent } from './components/user/register/register.component';
import { LoginComponent } from './components/user/login/login.component';
import { ListPublicationsComponent } from './components/publications/list-publications/list-publications.component';
import { AddPublicationComponent } from './components/publications/add-publication/add-publication.component';
@NgModule({
  declarations: [
    AppComponent,
    StadisticsComponent,
    CatalogoComponent,
    RegisterComponent,
    LoginComponent,
    ListPublicationsComponent,
    AddPublicationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgxEchartsDirective
  ],
  providers: [
    provideEcharts()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
