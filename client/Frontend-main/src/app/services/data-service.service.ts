import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import {  users } from 'src/app/models/getUser'; // Adjust the path accordingly
import { getBooking } from 'src/app/models/getBooking'; // Adjust the path accordingly

@Injectable({
  providedIn: 'root'
})
export class DataServiceService {
  private baseUrl = 'http://localhost:9090/mealBooking';
  private bookingApi = '/meal/getBooking';
  private userApi = '/home/getUser';

  private bookingSubject = new BehaviorSubject<getBooking[]>([]);
  private userSubject = new BehaviorSubject<users[]>([]);

  constructor(private http: HttpClient) {}

  fetchBookingData(token: string): Observable<getBooking[]> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    this.http.get<getBooking[]>(this.baseUrl + this.bookingApi, { headers }).subscribe(
      (response: getBooking[]) => {
        this.bookingSubject.next(response);
      },
      (error: any) => {
        console.error('Booking API error:', error);
      }
    );

    return this.bookingSubject.asObservable();
  }

  fetchUserData(token: string): Observable<users[]> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    this.http.get<users[]>(this.baseUrl + this.userApi, { headers }).subscribe(
      (response: users[]) => {
        this.userSubject.next(response);
      },
      (error: any) => {
        console.error('User API error:', error);
      }
    );

    return this.userSubject.asObservable();
  }
}