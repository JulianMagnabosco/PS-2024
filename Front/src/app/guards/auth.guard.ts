import {CanActivateFn, CanDeactivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/user/auth.service";
import {AddPublicationComponent} from "../components/publications/add-publication/add-publication.component";
import {ModPublicationComponent} from "../components/publications/mod-publication/mod-publication.component";
import {cConfirm} from "../services/custom-alert/custom-alert.service";

export const authGuard: CanActivateFn = (route, state) => {
  let token = sessionStorage.getItem("app.token");
  const router = inject(Router)
  if(token){
    return true
  }
  router.navigate(["/login"])
  return false
};

export const authGuardSubmit: CanDeactivateFn<AddPublicationComponent|ModPublicationComponent> = (component,route, state) => {
  if(component.form.dirty){
    return cConfirm("¿Seguro que quieres salir? Se perderan los datos no guardados").then((result) => {
      return result.isConfirmed;
    })
  }else {
    return true
  }
};

export const authGuardLogin: CanDeactivateFn<any> = (route, state) => {
  let token = sessionStorage.getItem("app.token");

  if(token){
    return true
  }
  return false
};

