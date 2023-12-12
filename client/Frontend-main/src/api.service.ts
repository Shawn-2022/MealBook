// src/app/api.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private baseUrl = 'http://localhost:9090'; // Update this with your actual API URL

  constructor(private http: HttpClient) {}

  // Example method to make a GET request
  getExample(): Observable<any> {
    const url = `${this.baseUrl}/example`; // Update with your actual endpoint
    return this.http.get(url);
  }

  // Example method to make a POST request
  postExample(data: any): Observable<any> {
    const url = `${this.baseUrl}/example`; // Update with your actual endpoint
    return this.http.post(url, data);
  }

  // Add more methods for other API endpoints as needed
}
