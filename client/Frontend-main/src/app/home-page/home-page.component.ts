import { Component, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import {  users } from 'src/app/models/getUser'; // Adjust the path accordingly
import { getBooking } from 'src/app/models/getBooking'; // Adjust the path accordingly
import { DataServiceService } from '../services/data-service.service';


@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {
  faXmark = faXmark;
  selected: Date | any;
  dateFilter: any;
  bookingData!: getBooking[];
  userData!: users[];

  constructor(private dataService: DataServiceService) {}

  ngOnInit(): void {
    // Retrieve the token from localStorage
    const token = localStorage.getItem('token');

    // Check if token exists
    if (token) {
      // Make API calls using the DataService
      this.dataService.fetchBookingData(token).subscribe(
        (data: getBooking[]) => {
          // Handle successful response
          this.bookingData = data;
          console.log('Booking data:', this.bookingData);
        },
        (error: any) => {
          // Handle error
          console.error('Booking API error:', error);
        }
      );

      this.dataService.fetchUserData(token).subscribe(
        (data: users[]) => {
          // Handle successful response
          this.userData = data;
          console.log('User data:', this.userData);
        },
        (error: any) => {
          // Handle error
          console.error('User API error:', error);
        }
      );
    }
  }
  
  showBookingData(): void {
    // You can customize this method based on your actual data structure
    console.log('Booking Data:', this.bookingData);
  }

  showFormFlag = false;
  showFormFlagAdd=false;
  showFormFlagHistory=false;

  showForm(): void {
    this.showFormFlag = true;
  }
    closeForm(): void {
    this.showFormFlag = false;
  }
//for history
  showFormFlagHistoryLog(): void {
    this.showFormFlagHistory = true;
  }
    closeFormHistory(): void {
    this.showFormFlagHistory = false;
  }
  submitFormHistory(formData: any):void{
    this.showFormFlagHistory = false;

  }

  submitForm(formData: any): void {
   // debugger;
   // console.log(formData); // Handle form submission logic here
    this.showFormFlag = false; // Close the form after submission
  }
  //method to disable the button 
  isQuickMealButtonDisabled(): boolean {
    // const currentHour = new Date().getHours();
    // // Disable the "Quick Meal" button after 8 PM (20:00)
    // return currentHour >= 20;

    const now = new Date();

    // Calculate the time for 8 AM and 8 PM
    const eightAM = new Date(now);
    eightAM.setHours(8, 0, 0, 0);

    const eightPM = new Date(now);
    eightPM.setHours(24, 0, 0, 0);

    // The button is disabled if the current time is before 8 AM or after 8 PM
    return now < eightAM || now >= eightPM;
  }

//for Add meal
  showFormAdd(): void {
    this.showFormFlagAdd = true;
 }
 closeFormAdd(): void {
    this.showFormFlagAdd = false;
 }

 submitFormAdd(formData: any): void {
  // debugger;
  // console.log(formData); // Handle form submission logic here
   this.showFormFlagAdd = false; // Close the form after submission
 }



  isWeekend(date: Date): boolean {
    const day = date.getDay();
    return day === 0 || day === 6; // Sunday (0) or Saturday (6)
  }

  // dateFilter = (date: Date): boolean => {
  //   // Disable past dates and weekends
  //   const currentDate = new Date();
  //   return date >= currentDate && !this.isWeekend(date);
  // };

  onDateSelection(selectedDate: Date | null): void {
    if (selectedDate !== null) {
      // Handle date selection
      console.log('Selected date:', selectedDate);
    } else {
      // Handle the case where null is selected
      console.log('No date selected');
    }
  }
  
  DateFilter(date: Date | null): boolean {
    if (!date) {
      return false; // handle null case if needed
    }
    const month = date.getMonth() + 1; // Month is zero-based, so add 1
    const day = date.getDate();
    const today = new Date();
    today.setHours(0, 0, 0, 0);
 // Disable weekends (Saturday and Sunday)
 if (date.getDay() === 0 || date.getDay() === 6) {
  return false;
}
// Disable past dates
if (date < today) {
  return false;
}
if (
  (month === 12 && (day === 25 || day === 31)) ||
  (month === 1 && (day === 15 || day === 26))
) {
 return false;
}
return true;
  }
}
 