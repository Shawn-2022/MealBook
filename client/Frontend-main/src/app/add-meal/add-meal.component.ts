import { ChangeDetectorRef, Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { DateAdapter } from '@angular/material/core';
import { MatCalendarCellCssClasses, MatDatepickerInputEvent } from '@angular/material/datepicker';
import { DatePipe } from '@angular/common';
import { AbstractControl, ValidatorFn } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field'
@Component({
  selector: 'app-add-meal',
  templateUrl: './add-meal.component.html',
  styleUrls: ['./add-meal.component.css'],
  providers: [DatePipe], // Add DatePipe to providers

})
export class AddMealComponent implements OnInit {
  @Output() formSubmitAdd = new EventEmitter<any>();
  totalSelectedDays: number = 0;
  dateRangeForm: any;
  totalDaysSelected!: number;

  ngOnInit(): void {
    console.log('ngOnInit method is executed!');

  }
  mealFormAdd: FormGroup;

  constructor(private fb: FormBuilder, private datePipe: DatePipe,private cdr: ChangeDetectorRef) {
    this.mealFormAdd = this.fb.group({
      dateRange: this.fb.group({
        //startDate: [null,[Validators.required]],
        // startDate: (''),
        // endDate: (''),
        matStartDate: [null, [Validators.required]],
        matEndDate: [null, [Validators.required]],
      }),
      //totalSelectedDays: [null, Validators.required],

    });
    // this.subscribeToDateRangeChanges();

  }

  dateRangeDisabled = false;

  dateFilter(date: Date | null): boolean {

    if (!date) {

      return false; // handle null case if needed
    }

    const day = date.getDate();
    const month = date.getMonth() + 1; // Month is zero-based, so add 1

    const today = new Date();
    today.setHours(0, 0, 0, 0); // Used to disable the past dates

    // Calculate the timestamp for 3 months from now
    const threeMonthsFromNow = new Date();
    threeMonthsFromNow.setMonth(threeMonthsFromNow.getMonth() + 3);
    threeMonthsFromNow.setHours(0, 0, 0, 0);


    //disable past dates
    if (date <= today) {
      return false;
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

    //return true;
    if (date >= today && date <= threeMonthsFromNow) {
      return true;
    }
    else {
      return false; // Disable selection for dates outside the specified range
    }

  }

  onSubmitAdd(): void {
    const dateRangeControl = this.mealFormAdd.get('dateRange');
    if (dateRangeControl && dateRangeControl.valid) {
      const matStartDate = dateRangeControl.get('matStartDate')?.value;
      const matEndDate = dateRangeControl.get('matEndDate')?.value;

      // Additional validation if needed
      if (matStartDate && matEndDate && matStartDate <= matEndDate) {
        // Format the dates using DatePipe
        const formattedStartDate = this.datePipe.transform(matStartDate, 'yyyy-MM-dd');
        const formattedEndDate = this.datePipe.transform(matEndDate, 'yyyy-MM-dd');

        // Form is valid, perform the necessary actions
        console.log('Valid form submitted:', { formattedStartDate, formattedEndDate });



        const dateRange = { startDate: formattedStartDate, endDate: formattedEndDate };
        console.log('Total Days Selected:');

        // Calculate the total days excluding weekends and certain days
        this.totalDaysSelected = this.calculateTotalDays(matStartDate, matEndDate);
        this.cdr.detectChanges(); // Manually trigger change detection

        console.log('Total Days Selected:', this.totalDaysSelected);

        this.formSubmitAdd.emit({ dateRange, totalDaysSelected: this.totalDaysSelected });
      } else {
        console.error('Invalid date range.');
      }
    } else {
      console.error('Date range control is null, undefined, or invalid.');
    }

  }
  private calculateTotalDays(startDate: Date, endDate: Date): number {
    let totalDays = 0;
    const currentDate = new Date(startDate);

    while (currentDate <= endDate) {
      if (this.dateFilter(currentDate)) {
        totalDays++;
      }
      currentDate.setDate(currentDate.getDate() + 1);
    }

    return totalDays;

  }
}









 


