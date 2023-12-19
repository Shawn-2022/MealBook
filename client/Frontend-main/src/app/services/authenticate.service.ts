import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { authenticate } from 'src/app/models/authenticate';

@Injectable({
  providedIn: 'root'
})
export class AuthenticateService {
  private readonly API_URL = 'http://localhost:9090/mealBooking';

  constructor(private http: HttpClient) {}

  login(credentials: authenticate): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(`${this.API_URL}/login`, credentials, { headers }).pipe(
      catchError((error) => {
        console.error('Error during login:', error);
        throw error;
      }),
      map((response) => {
        const token = response.token; // Extract the token from the response object
        localStorage.setItem('token', token); // Store the token in local storage
        return response; // Return the entire response object
      })
    );
  }
}
