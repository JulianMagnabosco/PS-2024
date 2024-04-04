import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {StadisticsComponent} from "./components/stadistics/stadistics.component";
import {CatalogoComponent} from "./components/catalogo/catalogo.component";
import {RegisterComponent} from "./components/user/register/register.component";
import {LoginComponent} from "./components/user/login/login.component";
import {AddPublicationComponent} from "./components/publications/add-publication/add-publication.component";
import {ListPublicationsComponent} from "./components/publications/list-publications/list-publications.component";
import {ShowPublicationComponent} from "./components/publications/show-publication/show-publication.component";
import {authGuard, authGuardLogin} from "./guards/auth.guard";

const routes: Routes = [
  { path: 'uno', component: StadisticsComponent },
  { path: 'dos', component: CatalogoComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent, canDeactivate:[authGuardLogin] },
  { path: 'publicate', component: AddPublicationComponent, canActivate:[authGuard] },
  { path: 'explore', component: ListPublicationsComponent, canActivate:[authGuard] },
  { path: 'pub/:id', component: ShowPublicationComponent, canActivate:[authGuard]}
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
