import { Component } from '@angular/core';
import {AuthenticationRequest} from "../../services/models/authentication-request";
import {FormsModule} from "@angular/forms";
import {register} from "../../services/fn/authentication/register";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {TokenService} from "../../token/token.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  authRequest: AuthenticationRequest = {email:"", password:''};
  errorMsg: Array<string> =[];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokeService: TokenService) {

  }

  login() {
   this.errorMsg = [];
  }

  register() {
    this.router.navigate(['register']);
    this.authService.authenticate({
      body: this.authRequest
    }).subscribe({
      next: (res)=>{
        this.tokeService.token = res.token as string
        this.router.navigate(['books'])
      },
      error: (err)=>{
        console.log(err);
        if (err.error.validationErrors){
          this.errorMsg = err.error.validationErrors;
        }else {
          this.errorMsg.push(err.error.error);
        }
      }
    })

  }
}
