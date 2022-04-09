package com.example.new_firebase;

public class PaperApproval {
    private String subject_Name;
    private String year_Name;
    private String paper_Name;
    private String id;
    private int status;
    UploadPdf Pdf;

    public UploadPdf getPdf() {
        return Pdf;
    }

    public void setPdf(UploadPdf Pdf) {
        this.Pdf = Pdf;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PaperApproval()
    {}

    public PaperApproval(String id,String subject_Name, String year_Name, String paper_Name,int status,UploadPdf Pdf) {
        this.id = id;
        this.subject_Name = subject_Name;
        this.year_Name = year_Name;
        this.paper_Name = paper_Name;
        this.status = status;
        this.Pdf = Pdf;
    }

    public String getSubject_Name() {
        return subject_Name;
    }

    public void setSubject_Name(String subject_Name) {
        this.subject_Name = subject_Name;
    }

    public String getYear_Name() {
        return year_Name;
    }

    public void setYear_Name(String year_Name) {
        this.year_Name = year_Name;
    }

    public String getPaper_Name() {
        return paper_Name;
    }

    public void setPaper_Name(String paper_Name) {
        this.paper_Name = paper_Name;
    }
}
