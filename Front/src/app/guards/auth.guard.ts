import {CanActivateFn, CanDeactivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/user/auth.service";

export const authGuard: CanActivateFn = (route, state) => {
  const service = inject(AuthService)
  const router = inject(Router)
  if(service.user!=null){
    return true
  }
  router.navigate(["/login"])
  return false
};

export const authGuardLogin: CanDeactivateFn<any> = (route, state) => {
  const service = inject(AuthService)
  if(service.user!=null){
    return true
  }
  return false
};

