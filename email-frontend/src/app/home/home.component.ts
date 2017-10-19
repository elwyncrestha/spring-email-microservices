import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  hint_text: string

  constructor(private http:HttpClient,
              private auth:AuthService) { }

  ngOnInit() {
  }

  send(e){
    e.preventDefault();
    var from = e.target.elements[0].value;
    var to = e.target.elements[1].value;
    var subject = e.target.elements[1].value;
    var text = e.target.elements[1].value;

    if(from && to && subject && text){
      this.hint_text = ""

      let body = new URLSearchParams();
      body.set('from', from);
      body.set('to', to);
      body.set('subject', subject);
      body.set('text', text);
      console.log("header: " + this.auth.GetAuthHeader())
      let options = {
          headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
      };

      const req = this.http.post('http://localhost:9999/api/email/send', body.toString(), options)
      .subscribe(
        res => {
          this.hint_text = "email sent successful";
        },
        er => {
          console.error("Error occured");
          this.hint_text = "Error: " + er.message;
        }
      )

    } else {
      this.hint_text = "provide all fields"
    }

    return false;
  }

}
