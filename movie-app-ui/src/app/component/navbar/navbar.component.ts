import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  name = sessionStorage.getItem('name');
  isLoggedIn = signal<boolean>(false);

  constructor(private authService: AuthService, private router: Router){}

  ngOnInit(): void{
    this.isLoggedIn = this.authService.getLoggedIn();
  }


  logout(){
    this.authService.logout();
    this.authService.setLoggedIn(false);
    this.router.navigate(['login']);
  }

}
