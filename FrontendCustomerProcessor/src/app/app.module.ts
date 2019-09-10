import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {UploadCustomerStatementComponent} from "./upload/upload-customer-statement.component";
import {UploadService} from "./service/UploadService";
import {HttpClientModule} from "@angular/common/http";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

@NgModule({
  declarations: [
    AppComponent,
    UploadCustomerStatementComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    NgbModule
  ],
  providers: [UploadService],
  bootstrap: [AppComponent]
})
export class AppModule { }
