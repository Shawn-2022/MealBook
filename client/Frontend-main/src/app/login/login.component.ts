import { Component } from '@angular/core';
import{AuthenticateService}from 'src/app/services/authenticate.service'
import { authenticate } from 'src/app/models/authenticate';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  credentials: authenticate = { email: '', password: '' };

  constructor(private authService: AuthenticateService,private router: Router) {}

  isVisible: boolean = true;

  login(): void {
  this.authService.login(this.credentials).subscribe(
    (response) => {
      this.isVisible = false;
      // Store the token
      // localStorage.setItem('token', response.token);
      // Navigate to the desired route
      this.router.navigate(['home']);
      console.log('Login successful');
    },
    (error) => {
      this.isVisible = true;
      console.error('Login failed', error);
    }
  );
}
}