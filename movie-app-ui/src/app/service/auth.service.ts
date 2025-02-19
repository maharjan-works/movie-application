import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private BASE_URL = 'http://localhost:8080/api/v1/auth';
  constructor(private http: HttpClient) { }


  register(registerRequest: RegisterRequest): Observable<AuthResponse>{
    return this.http.post<AuthResponse>(`${this.BASE_URL}/register`, registerRequest);
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
