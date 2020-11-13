import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SmsSenderComponent } from './components/sms-sender/sms-sender.component'

const routes: Routes = [
  { path: '', component: SmsSenderComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
