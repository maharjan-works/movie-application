import { Component, OnInit } from '@angular/core';
import { AuthService, LoginRequest } from '../../service/auth.service';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {


  email = new FormControl<string>('', [Validators.required, Validators.email]);
  password = new FormControl<string>('',[Validators.required]);


  loginForm!: FormGroup;

  inlineNotification: {show: boolean, type: string,message: string} ={
    show: false,
    type: '',
    message: '',
  }


  constructor(private authService: AuthService, private router: Router){}

  ngOnInit(): void {
    this.loginForm = new FormGroup(
      {
        email: this.email,
        password: this.password,
      }
    )
  }

  login(){

    if(this.loginForm.valid){

      const loginRequest: LoginRequest = {
        email: this.loginForm.get('email')?.value,
        password: this.loginForm.get('password')?.value,
      }

      this.authService.login(loginRequest).subscribe({
       next: (res: any) => {
        console.log(res);
        this.authService.setLoggedIn(true);
        this.router.navigate(['home']);
       },
       error: (err: any) => {
        this.inlineNotification.show= true;
        this.inlineNotification.message = err.error.detail;
        console.log(err.error);
       } 
      })

    }else{
      this.inlineNotification = {
        show: true,
        type: 'error',
        message: 'Please, fill up all fields carefully!'
      }      
      console.log("Error: ", this.inlineNotification);

    }
  }




}
