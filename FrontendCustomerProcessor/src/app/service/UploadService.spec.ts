import {TestBed, async, inject} from '@angular/core/testing';
import {UploadService} from "./UploadService";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {ValidationReport} from "../model/ValidationReport";


describe("UploadService", () => {
  let uploadService: UploadService;
  let httpTestController: HttpTestingController;

  beforeEach(async (() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UploadService]
    }).compileComponents();
  }));

  beforeEach(() => {
    uploadService = TestBed.get(UploadService);
    httpTestController = TestBed.get(HttpTestingController);
  })

  afterEach(() => {
    httpTestController.verify();
  });

  it("should create UploadService", () => {
    expect(uploadService).toBeDefined();
  });

  it("should POST a file to validate", () => {
    let emptyFile: File = new File([], "filename.xml");

    uploadService.uploadFile(emptyFile);

    let testRequest = httpTestController.expectOne("http://localhost:8080/customerstatement/validate");
    expect(testRequest.request.method).toEqual("POST");
    testRequest.flush(new ValidationReport());
  });

  it("should post the provided file", () => {
    // TODO
  });
});
