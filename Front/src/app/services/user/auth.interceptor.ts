import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {catchError, finalize, Observable, throwError} from "rxjs";
import {inject} from "@angular/core";
import {Router} from "@angular/router";
import {AuthService} from "./auth.service";
import {LoadingService} from "../loading/loading.service";

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const router = inject(Router);
  const service = inject(AuthService);
  const loadingService = inject(LoadingService);
  let token = sessionStorage.getItem("app.token");
  if (token && !req.url.includes("signin")) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      },
    });
  }

  loadingService.start()
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => handleErrorRes(error,router,service))
  ).pipe(finalize(()=>{loadingService.end()}));
};

export function handleErrorRes(error: HttpErrorResponse,router:Router,service:AuthService): Observable<never> {
  console.log(error.status)
  service.logout();
  if (error.status === 401) {
    router.navigateByUrl("/login", {replaceUrl: true});
  }
  return throwError(() => error);
}
