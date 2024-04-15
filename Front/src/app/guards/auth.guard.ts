import {CanActivateFn, CanDeactivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/user/auth.service";

export const authGuard: CanActivateFn = (route, state) => {
  let token = sessionStorage.getItem("app.token");

  const router = inject(Router)
  if(token){
    return true
  }
  router.navigate(["/login"])
  return false
};

export const authGuardLogin: CanDeactivateFn<any> = (route, state) => {
  let token = sessionStorage.getItem("app.token");

  if(token){
    return true
  }
  return false
};

