import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {catchError, Observable, throwError} from "rxjs";
import {inject} from "@angular/core";
import {Router} from "@angular/router";
import {AuthService} from "./auth.service";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(AuthService);

  let token = sessionStorage.getItem("app.token");
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      },
    });
  }

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => handleErrorRes(error))
  );
};

export function handleErrorRes(error: HttpErrorResponse): Observable<never> {
  const router = inject(Router);
  if (error.status === 401) {
    router.navigateByUrl("/login", {replaceUrl: true});
  }
  return throwError(() => error);
}
