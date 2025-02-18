import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
 
  firstName: string = '';
  lastName: string = '';
  email: string = '';
  username: string = '';
  password: string = '';

  loginForm!: FormGroup;

  ngOnInit(): void {

    this.loginForm = new FormGroup({
      firstName: new FormControl(this.firstName, Validators.required),
      lastName: new FormControl(this.lastName, Validators.required),
      email: new FormControl(this.email, [Validators.required, Validators.email]),
      username: new FormControl(this.username, Validators.required),
      password: new FormControl(this.password, [Validators.required, Validators.minLength(5)]),

    })
    
  }








  register(){
    console.log(this.loginForm.value);

  }



}
