import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Sms } from '../models/sms';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
}

@Injectable({
  providedIn: 'root'
})
export class SmsService {

  baseUrl: string = '//localhost:8080/sms';

  constructor(private http: HttpClient) { }

  public sendSms(sms: Sms): Observable<string> {
    console.log(`{\n\t\"phoneNumber\":\"${sms.phoneNumber}\",\n\t\"message\":\"${sms.message}\"\n}`);
    return this.http.post<string>(this.baseUrl, `{\n\t\"phoneNumber\":\"${sms.phoneNumber}\",\n\t\"message\":\"${sms.message}\"\n}`, httpOptions);
  }


}
