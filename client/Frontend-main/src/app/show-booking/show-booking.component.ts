import { Component } from '@angular/core';
import { faGift,faTimes } from '@fortawesome/free-solid-svg-icons';
import {  OnDestroy } from '@angular/core';
import * as qrcode from 'qrcode-generator';
import { getBooking } from '../models/getBooking';
import { DataServiceService } from '../services/data-service.service';
@Component({
  selector: 'app-show-booking',
  templateUrl: './show-booking.component.html',
  styleUrls: ['./show-booking.component.css']
})
export class ShowBookingComponent implements OnDestroy {
  faGift = faGift;
  faTimes = faTimes;
  isBookingsVisible: boolean = true;
  qrImage: string = '';

  bookedMeals: getBooking[] = []; // Assuming this is the array you want to display
  isButtonDisabled: boolean = false;
  remainingMinutes: number = 15;
  remainingSeconds: number = 0;
  selectedEmployee: getBooking | null = null; // Initially, no employee is selected

  private timer: any;

  constructor(private dataService: DataServiceService) {}

  ngOnInit(): void {
    this.loadData(); // Load booking data on component initialization
  }

  loadData(): void {
    // Retrieve the token from localStorage
    const token = localStorage.getItem('token');

    // Check if token exists
    if (token) {
      // Make API call using the DataService to fetch booking data
      this.dataService.fetchBookingData(token).subscribe(
        (data: getBooking[]) => {
          // Handle successful response
          this.bookedMeals = data;
        },
        (error: any) => {
          // Handle error
          console.error('Booking API error:', error);
        }
      );
    }
  }

  ngOnDestroy() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  }

  toggleQR(employee: getBooking) {
    this.isBookingsVisible = !this.isBookingsVisible;
    if (this.selectedEmployee !== employee) {
      this.selectedEmployee = employee;
      const qrData = `${employee.bookingId},${employee.bookingDate}`;

      const myQr = qrcode(14, 'L');
      myQr.addData(qrData);
      myQr.make();

      this.qrImage = myQr.createImgTag();
    }
    this.isButtonDisabled = true;

    this.timer = setInterval(() => {
      if (this.remainingSeconds > 0) {
        this.remainingSeconds--;
      } else {
        if (this.remainingMinutes > 0) {
          this.remainingMinutes--;
          this.remainingSeconds = 59;
        } else {
          this.isButtonDisabled = false;
          clearInterval(this.timer);
        }
      }
    }, 1000);
  }
}