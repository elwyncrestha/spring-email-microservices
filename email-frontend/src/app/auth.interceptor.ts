import { Injectable } from '@angular/core';
import {HttpEvent, HttpInterceptor, HttpHandler, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs/observable';
import {AuthService} from './auth.service';


@Injectable()
export class AuthInterceptor implements HttpInterceptor{
  constructor(private auth: AuthService) {}

  intercept (req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>{
    const authHeader = this.auth.GetAuthHeader();
    const authReq = req.clone({
      headers: req.headers.set('Authorization', authHeader)
    })
    return next.handle(authReq);
  }
}
