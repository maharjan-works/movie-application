import { Routes } from '@angular/router';
import { HomeComponent } from './component/home/home.component';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { AddMovieComponent } from './component/add-movie/add-movie.component';

export const routes: Routes = [
  {path:'', title:"MovieApp | Home", component:HomeComponent},
  {path:'home', title:"MovieApp | Home", component:HomeComponent},
  {path:'register', title:"MovieApp | Register", component:RegisterComponent},
  {path:'login', title:"MovieApp | Login", component: LoginComponent},
  {path:'add-movie', title:"MovieApp | Add Movie", component: AddMovieComponent},

];
