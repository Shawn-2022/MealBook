import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
// import { faFaceSmile } from '@fortawesome/free-solid-svg-icons';
import{faBell,faInfoCircle,faFileContract} from'@fortawesome/free-solid-svg-icons';
import{faUser,faLock,faKey,faPowerOff,faHome} from'@fortawesome/free-solid-svg-icons';
import{faBars} from'@fortawesome/free-solid-svg-icons';
import { ChangeDetectionStrategy } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenav } from '@angular/material/sidenav';
import {  EventEmitter, Output } from '@angular/core';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,

})
export class HeaderComponent {
  @ViewChild(MatSidenav) sidenav!: MatSidenav;
  router: any;
  // router: any;


  // faFaceSmile = faFaceSmile;
  faBell=faBell;
  faUser=faUser;
  faBars=faBars;
  faInfoCircle=faInfoCircle;
  faFileContract=faFileContract;
  faLock=faLock;
  faHome=faHome;
  faKey=faKey;
  faPowerOff=faPowerOff;
  isSidebarOpen = false;
  selectedContent = ''; // Variable to track selected content
  showNotificationList = false;
  notifications: string[] = [];
  // notifications: Notification[] = [];

  counter = 0;
i!: number; 

  toggleSidebar() {
    console.log("Clicked, isSidebarOpen:", this.isSidebarOpen);
    this.isSidebarOpen = !this.isSidebarOpen;
    //this.cdr.detectChanges();

  }


  showContent(contentType: string) {
    // Handle the logic to display content based on the selected link
    this.selectedContent = contentType;
    this.isSidebarOpen = false; // Close the sidebar after selecting a link
  }
  // navigateToAboutUs(): void {
  //   this.router.navigateByUrl('/about-us');
  // }
  toggleNotificationList() {
   
    // Toggle the visibility of the notification list
    this.showNotificationList = !this.showNotificationList;

    // Generate a random notification message (replace this with your actual notification logic)
    const notificationMessage = this.generateRandomNotification();

    this.notifications.unshift(notificationMessage); 

    this.notifications = this.notifications.slice(0, 5);
  }

  generateRandomNotification(): string {
    // Replace this with your actual logic to generate different notification messages
    const messages = ['Your meal has been booked!', 'Quick meal is booked', 'Your meal has been cancelled'];
  const index = this.counter % messages.length;
  this.counter++;
  return messages[index];
  }
  removeNotification(index: number) {
    this.notifications.splice(index, 1);
  }
}

