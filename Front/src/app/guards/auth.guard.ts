import {CanActivateFn, CanDeactivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {UserService} from "../services/user/user.service";

export const authGuard: CanActivateFn = (route, state) => {
  const service = inject(UserService)
  const router = inject(Router)
  if(service.usuarioActual!=null){
    return true
  }
  router.navigate(["/login"])
  return false
};

export const authGuardLogin: CanDeactivateFn<any> = (route, state) => {
  const service = inject(UserService)
  if(service.usuarioActual!=null){
    return true
  }
  return false
};

