import {Component, NgModule} from "@angular/core";
import {UploadService} from "../service/UploadService";
import {ValidationReport} from "../model/ValidationReport";

@Component({
  selector: 'upload-customer-statement',
  templateUrl: './upload-customer-statement.component.html'
})
export class UploadCustomerStatementComponent {

  validationReport: ValidationReport;
  showError: boolean = false;

  public constructor(private uploadService: UploadService) {}

  public showReport(): boolean {
    return this.validationReport != null;
  }

  public uploadSuccessful(): boolean {
    return this.validationReport && this.validationReport.faultyStatements.length == 0;
  }

  async uploadFile(selectedFileEvent) {
    if (selectedFileEvent.target.files[0]) {
      let file = selectedFileEvent.target.files[0];
      this.validationReport = await this.uploadService.uploadFile(file)
                .catch(err => {
                  this.showError = true;
                  return null;
                });
      
    }
  }

  resetForm(): void {
    this.validationReport = null;
    this.showError = false;
  }
}
