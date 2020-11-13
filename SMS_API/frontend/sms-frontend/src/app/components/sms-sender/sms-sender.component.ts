import { Component, OnInit } from '@angular/core';
import { Sms } from '../../models/sms';
import { SmsService } from '../../service/sms.service';

@Component({
  selector: 'app-sms-sender',
  templateUrl: './sms-sender.component.html',
  styleUrls: ['./sms-sender.component.css']
})

export class SmsSenderComponent implements OnInit {

  phoneNumber: string;
  message: string;

  constructor(private smsService: SmsService) { }

  ngOnInit(): void {
  }

  onSubmit() {
    const sms = {
      phoneNumber: this.phoneNumber,
      message: this.message
    }
    console.log(sms);
    this.smsService.sendSms(sms).subscribe(newSms => console.log(newSms));
  }

}
