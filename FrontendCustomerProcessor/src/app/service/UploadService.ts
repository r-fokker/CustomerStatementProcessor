import {HttpClient} from "@angular/common/http";
import {ValidationReport} from "../model/ValidationReport";
import {Injectable} from "@angular/core";

@Injectable()
export class UploadService {

   static readonly url: string = 'http://localhost:8080/customerstatement/validate';

  constructor(private http: HttpClient) {}

  async uploadFile(file: File): Promise<ValidationReport> {
    let formData: FormData = new FormData();

    formData.append('customerStatement', file, file.name);

    return await this.http.post<ValidationReport>(UploadService.url, formData).toPromise();
  }

}
