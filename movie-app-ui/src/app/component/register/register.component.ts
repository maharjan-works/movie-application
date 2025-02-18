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
 
  firstName = new FormControl<string>('', [Validators.required]);
  lastName = new FormControl<string>('', [Validators.required]);
  email = new FormControl<string>('', [Validators.required, Validators.email]);
  username = new FormControl<string>('', [Validators.required]);
  password = new FormControl<string>('', [Validators.required, Validators.minLength(5)]);

  registerForm!: FormGroup;

  inlineNotification:{show: boolean, type: string, text: string} = {
    show: false,
    type: '',
    text: '',
  }

  ngOnInit(): void {
    this.registerForm = new FormGroup({
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      username: this.username,
      password: this.password
    })
    
  }


  register(){
    console.log("Register Form Value: " , this.registerForm.value);

  }



}
