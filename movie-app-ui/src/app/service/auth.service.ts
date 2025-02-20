import { HttpClient } from '@angular/common/http';
import { Injectable, signal, WritableSignal } from '@angular/core';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private BASE_URL = 'http://localhost:8080/api/v1/auth';
  private loggedIn = signal<boolean>(this.isAuthenticated());

  constructor(private http: HttpClient) { }

  register(registerRequest: RegisterRequest): Observable<AuthResponse>{
    return this.http.post<AuthResponse>(`${this.BASE_URL}/register`, registerRequest);
  }

  login(loginRequest: LoginRequest): Observable<AuthResponse>{
    return this.http.post<AuthResponse>(`${this.BASE_URL}/login`, loginRequest).pipe(
      tap(response => {
        if (response && response.accessToken){
          sessionStorage.setItem('accessToken', response.accessToken);
          sessionStorage.setItem('refreshToken', response.refreshToken);
          sessionStorage.setItem('name', response.firstName + ' '+ response.lastName);
          sessionStorage.setItem('email', response.email);
          sessionStorage.setItem('username', response.username);
        }
      })
    );
  } //end of login()


  logout(): void{
    sessionStorage.removeItem('accessToken');
    sessionStorage.removeItem('refreshToken');
    sessionStorage.removeItem('name');
    sessionStorage.removeItem('email');
    sessionStorage.removeItem('username');
  }

  isAuthenticated():boolean{
    return sessionStorage.getItem('accessToken') != null;
  }

  setLoggedIn(value: boolean){
    this.loggedIn.set(value);
  }

  getLoggedIn(): WritableSignal<boolean>{
    return this.loggedIn;
  }


}

export type RegisterRequest = {
  firstName: string,
  lastName: string,
  email: string,
  username: string,
  password: string,
}

export type AuthResponse = {
  accessToken: string,
  refreshToken: string,
  firstName: string,
  lastName: string,
  email: string,
  username: string,
}

export type LoginRequest = {
  email: string,
  password: string,
}
