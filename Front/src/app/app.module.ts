import {NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { StadisticsComponent } from './components/stadistics/stadistics.component';
import { TestComponent } from './components/test/test.component';
import {HttpClientModule, provideHttpClient, withInterceptors} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgxEchartsDirective, provideEcharts} from "ngx-echarts";
import { RegisterComponent } from './components/user/register/register.component';
import { LoginComponent } from './components/user/login/login.component';
import { ListPublicationsComponent } from './components/publications/list-publications/list-publications.component';
import { AddPublicationComponent } from './components/publications/add-publication/add-publication.component';
import { ShowPublicationComponent } from './components/publications/show-publication/show-publication.component';
import {NgOptimizedImage} from "@angular/common";
import {SweetAlert2Module} from "@sweetalert2/ngx-sweetalert2";
import {YouTubePlayer} from "@angular/youtube-player";
import {authInterceptor} from "./services/user/auth.interceptor";
import {ListUsersComponent} from "./components/user/list-users/list-users.component";
import {ShowUserComponent} from "./components/user/show-user/show-user.component";
import {ModUserComponent} from "./components/user/mod-user/mod-user.component";
import {CommentsComponent} from "./components/comments/comments.component";
import {ModPublicationComponent} from "./components/publications/mod-publication/mod-publication.component";
import {UserStadisticsComponent} from "./components/stadistics/user-stadistics/user-stadistics.component";
import {PubStadisticsComponent} from "./components/stadistics/pub-stadistics/pub-stadistics.component";
import {DateCustomPipe} from "./pipes/date-custom.pipe";
import {HomeComponent} from "./components/home/home.component";
import {ListPurchasesComponent} from "./components/purchases/list-purchases/list-purchases.component";
import {ShowPurchaseComponent} from "./components/purchases/show-purchase/show-purchase.component";
import {ShowSellComponent} from "./components/sells/show-sell/show-sell.component";
import {ListSellsComponent} from "./components/sells/list-sells/list-sells.component";
import {ListDeliveriesComponent} from "./components/deliveries/list-deliveries/list-deliveries.component";
import {ShowDeliveryComponent} from "./components/deliveries/show-delivery/show-delivery.component";
import {SellStadisticsComponent} from "./components/stadistics/sell-stadistics/sell-stadistics.component";
import {CartComponent} from "./components/cart/cart.component";
import {
  ListPublicationsMineComponent
} from "./components/publications/list-publications-mine/list-publications-mine.component";
import {DraftsComponent} from "./components/publications/drafts/drafts.component";
import {ResetPasswordComponent} from "./components/user/reset-password/reset-password.component";
import {FormPublicationComponent} from "./components/publications/form-publication/form-publication.component";
import {NotificationsComponent} from "./components/notifications/notifications.component";


@NgModule({
  declarations: [
    AppComponent,
    StadisticsComponent,
    TestComponent,
    RegisterComponent,
    LoginComponent,
    ListPublicationsComponent,
    AddPublicationComponent,
    ShowPublicationComponent,
    ListUsersComponent,
    ShowUserComponent,
    ModUserComponent,
    CommentsComponent,
    ModPublicationComponent,
    UserStadisticsComponent,
    PubStadisticsComponent,
    ListPurchasesComponent,
    ShowPurchaseComponent,
    ListSellsComponent,
    ShowSellComponent,
    ListDeliveriesComponent,
    ShowDeliveryComponent,
    SellStadisticsComponent,
    CartComponent,
    ListPublicationsMineComponent,
    DraftsComponent,
    ResetPasswordComponent,
    FormPublicationComponent,
    NotificationsComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgxEchartsDirective,
    NgOptimizedImage,
    // SweetAlert2Module.forRoot(),
    YouTubePlayer,
    DateCustomPipe,
    FormsModule,
  ],
  providers: [
    provideHttpClient(withInterceptors([authInterceptor])),
    provideEcharts()
    // ,
    // {
    //   provide: APP_INITIALIZER,
    //   multi: true,
    //   useFactory: initializeApp,
    //   deps: [AuthService]
    // }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
