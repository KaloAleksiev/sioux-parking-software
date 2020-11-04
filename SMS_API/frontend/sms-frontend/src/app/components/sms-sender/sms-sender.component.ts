import { Component, OnInit } from '@angular/core';
import { Sms } from '../../models/sms';
import { SmsService } from '../../service/sms.service';

@Component({
  selector: 'app-sms-sender',
  templateUrl: './sms-sender.component.html',
  styleUrls: ['./sms-sender.component.css']
})
export class SmsSenderComponent implements OnInit {

  constructor(private smsService: SmsService) { }

  ngOnInit(): void {
    const sms = {
      phoneNumber: "+31639020448",
      message: "sms api works"
    }
    console.log(sms);
    this.smsService.sendSms(sms).subscribe(newSms => console.log(newSms));
  }

}
