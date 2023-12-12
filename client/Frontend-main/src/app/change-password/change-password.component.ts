import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent {
  currentPassword: string = '';
  newPassword: string = '';
  confirmPassword: string = '';

  changePassword() {
    // Add your password change logic here
    if (this.newPassword === this.confirmPassword) {
      // Perform password change operation
      console.log('Password changed successfully');
    } else {
      console.log('New password and confirm password do not match');
    }
  }
}
