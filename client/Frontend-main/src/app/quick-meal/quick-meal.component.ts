import { Component ,EventEmitter, OnInit, Output  } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { DateAdapter } from '@angular/material/core';
import { MatCalendarCellCssClasses } from '@angular/material/datepicker';
import { DatePipe } from '@angular/common';
@Component({
  selector: 'app-quick-meal',
  templateUrl: './quick-meal.component.html',
  styleUrls: ['./quick-meal.component.css'],
  providers: [DatePipe], // Add DatePipe to providers

})
export class QuickMealComponent implements OnInit{
  @Output() formSubmit = new EventEmitter<any>();

  mealForm!: FormGroup;
  today: Date = new Date();
  minDate: Date;
  // formattedDate: string;
  formattedDate!: string;
  // datePipe: any;

  constructor(private fb: FormBuilder ,private dateAdapter: DateAdapter<Date>, private datePipe: DatePipe )  {
   // debugger;

    this.mealForm = this.fb.group( {
      // mealType: this.fb.control,
      // datePick: this.fb.control
      datePick: [null, Validators.required],

    })


    this.minDate = this.today;
    this.formattedDate = ''; // Initialize with an empty string
  
    // Format the current date on component initialization
    this.formattedDate = this.formatDate(this.today);
  }

  getNextDay(): string {
    const today = new Date();
    today.setDate(today.getDate() + 1);
    return today.toISOString().split('T')[0];
  }
  


  onSubmit(): void {
    if (this.mealForm.valid) {
      const datePickControl = this.mealForm.get('datePick');
  
      if (datePickControl && datePickControl.value) {
        const selectedDate = datePickControl.value;
        const formattedDate = this.formatDate(selectedDate);
        console.log('Selected Date:', formattedDate);
  
        // Emit the formatted date to the parent component
        this.formSubmit.emit({ ...this.mealForm.value, datePick: formattedDate });
      } else {
        console.error('Date is null or undefined.');
      }
    } else {
      console.error('Meal form is invalid.');
    }
  }



  ngOnInit(): void {

  }
  
  private formatDate(date: Date): string {
    // Format the date to 'yyyy-MM-dd'
    if (!this.datePipe) {
      //console.error('DatePipe is undefined');
      return ''; // or handle the error in a way that fits your application
    }
  
    return this.datePipe.transform(date, 'yyyy-MM-dd') || '';
  }


  dateFilter(date: Date | null): boolean {
    if (!date) {
      return false; // handle null case if needed
    }

    const day = date.getDate();
    const month = date.getMonth() + 1; // Month is zero-based, so add 1
    const today = new Date();
  today.setHours(0, 0, 0, 0);
  // console.log(today);

  const nextDayTimestamp = today.getTime() + 24 * 60 * 60 * 1000;

  if (date.getTime() > nextDayTimestamp) {
    return false; // Allow selection for the next day or later
  }
  
    // Disable weekends (Saturday and Sunday)
    if (date.getDay() === 0 || date.getDay() === 6) {
      return false;
    }

    // Disable specific dates (25th December, 31st December, 15th January, 26th January)
    if (
      (month === 12 && (day === 25 || day === 31)) ||
      (month === 1 && (day === 15 || day === 26))
    ) {
     return false;
    }

    //for selecting next day by default
    const tomorrow = new Date(today);
    tomorrow.setDate(today.getDate() + 1);
  // console.log(tomorrow);   //printtt only
    // Check if the selected date is tomorrow
    if (
      date.getDate() === tomorrow.getDate() &&
      date.getMonth() === tomorrow.getMonth() &&
      date.getFullYear() === tomorrow.getFullYear()
    ) {
      return true; // Allow selection for tomorrow
    } else {
      return false; // Disable selection for any other date
    }

     return true;
  }


}
