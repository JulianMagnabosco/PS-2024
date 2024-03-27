import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {StadisticsComponent} from "./components/stadistics/stadistics.component";
import {CatalogoComponent} from "./components/catalogo/catalogo.component";
import {RegisterComponent} from "./components/user/register/register.component";

const routes: Routes = [
  { path: 'uno', component: StadisticsComponent },
  { path: 'dos', component: CatalogoComponent },
  { path: 'registrar', component: RegisterComponent },
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
