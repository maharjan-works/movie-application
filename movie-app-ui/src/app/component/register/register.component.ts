import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, RegisterRequest } from '../../service/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
 
  firstName = new FormControl<string>('', [Validators.required]);
  lastName = new FormControl<string>('', [Validators.required]);
  email = new FormControl<string>('', [Validators.required, Validators.email]);
  username = new FormControl<string>('', [Validators.required]);
  password= new FormControl<string>('', [Validators.required, Validators.minLength(5)]);

  registerForm!: FormGroup;

  inlineNotification: {show: boolean, message: string} = {
    show: false,
    message: ''
  } 

  constructor(private authService: AuthService, private router: Router){}

  ngOnInit(): void {
    this.registerForm = new FormGroup({
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      username: this.username,
      password: this.password,
    })
  }


  register(){

    if (this.registerForm.valid){

      console.log("is Register Form Valid: ", this.registerForm.valid);

      //creating registerRequest 
      const registerRequest: RegisterRequest = {
        firstName: this.registerForm.get('firstName')?.value,
        lastName: this.registerForm.get('lastName')?.value,
        email: this.registerForm.get('email')?.value,
        username: this.registerForm.get('username')?.value,
        password: this.registerForm.get('password')?.value,
      }

      //calling authService -> register(req) 
      this.authService.register(registerRequest).subscribe({
        next: (res: any) => {
          this.router.navigate(['login']);
        },
        error: (err: any) => {
         this.inlineNotification.show=true;
         this.inlineNotification.message=err.error.detail;
          console.log(err);
        }
      })//end of subscribe() method
    
    }else{
      this.inlineNotification.show=true;
      this.inlineNotification.message="Please, fill up all fields";
      console.log(this.inlineNotification);
    }

   

  }//end of register() method



}
