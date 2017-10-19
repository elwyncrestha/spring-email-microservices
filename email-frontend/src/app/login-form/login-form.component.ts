import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../user.service';
import { AuthService } from '../auth.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

interface TokenResponse{
  access_token: string,
  token_type: string,
  refresh_token: string,
  expires_in: number,
  scope: string,
  jti: string
}

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})

export class LoginFormComponent implements OnInit {

  hint_text: string

  constructor(private router:Router,
              private user:UserService,
              private http:HttpClient,
              private auth:AuthService) { }

  ngOnInit() {
    this.hint_text = "hint: user/password"
  }

  login(e){
    e.preventDefault();
    var username = e.target.elements[0].value;
    var password = e.target.elements[1].value;

    let body = new URLSearchParams();
    body.set('username', username);
    body.set('password', password);
    body.set('grant_type', this.auth.GetGrandType());
    body.set('scope', this.auth.GetScope());
    body.set('client_secret', this.auth.GetClientSecret());
    body.set('client_id', this.auth.GetClientId());

    let options = {
        headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    };

    const req = this.http.post<TokenResponse>('http://localhost:9191/uaa/oauth/token', body.toString(), options)
    .subscribe(
      res => {
        if(res.access_token){
          let header = "Bearer " + res.access_token;
          this.auth.SetAuthHeader(header);
          this.user.setUserLoggedIn();
          this.router.navigate(['home']);
        } else {
          this.hint_text = "no access token provided";
          console.error("no access token provided");
        }
      },
      er => {
        console.error("Error occured");
        this.hint_text = "Error: " + er.message;
      }
    )

    return false;
  }
}
