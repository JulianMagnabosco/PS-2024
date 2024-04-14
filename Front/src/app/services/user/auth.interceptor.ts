import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {catchError, Observable, throwError} from "rxjs";
import {inject} from "@angular/core";
import {Router} from "@angular/router";
import {AuthService} from "./auth.service";

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const router = inject(Router);
  let token = sessionStorage.getItem("app.token");
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      },
    });
  }

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => handleErrorRes(error,router))
  );
};

export function handleErrorRes(error: HttpErrorResponse,router:Router): Observable<never> {
  if (error.status === 401) {
    router.navigateByUrl("/login", {replaceUrl: true});
  }
  return throwError(() => error);
}
