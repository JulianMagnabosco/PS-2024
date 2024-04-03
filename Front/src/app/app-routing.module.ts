import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {StadisticsComponent} from "./components/stadistics/stadistics.component";
import {CatalogoComponent} from "./components/catalogo/catalogo.component";
import {RegisterComponent} from "./components/user/register/register.component";
import {LoginComponent} from "./components/user/login/login.component";
import {AddPublicationComponent} from "./components/publications/add-publication/add-publication.component";
import {ListPublicationsComponent} from "./components/publications/list-publications/list-publications.component";
import {ShowPublicationComponent} from "./components/publications/show-publication/show-publication.component";

const routes: Routes = [
  { path: 'uno', component: StadisticsComponent },
  { path: 'dos', component: CatalogoComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'publicate', component: AddPublicationComponent },
  { path: 'explore', component: ListPublicationsComponent },
  { path: 'pub/:id', component: ShowPublicationComponent }
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
