package com.example.new_firebase;

public class Request {
    private String uniqueId;
    private String request_subject_name;
    private String request_paper_year;
    private String request_paper_name;
    private String faculty_name;
    private int status;

    public String getFaculty_name() {
        return faculty_name;
    }

    public void setFaculty_name(String faculty_name) {
        this.faculty_name = faculty_name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Request()
    {

    }

    public Request(String uniqueId, String request_subject_name, String request_paper_year, String request_paper_name,int status,String faculty_name) {
        this.uniqueId = uniqueId;
        this.request_subject_name = request_subject_name;
        this.request_paper_year = request_paper_year;
        this.request_paper_name = request_paper_name;
        this.status = status;
        this.faculty_name = faculty_name;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getRequest_subject_name() {
        return request_subject_name;
    }

    public void setRequest_subject_name(String request_subject_name) {
        this.request_subject_name = request_subject_name;
    }

    public String getRequest_paper_year() {
        return request_paper_year;
    }

    public void setRequest_paper_year(String request_paper_year) {
        this.request_paper_year = request_paper_year;
    }

    public String getRequest_paper_name() {
        return request_paper_name;
    }

    public void setRequest_paper_name(String request_paper_name) {
        this.request_paper_name = request_paper_name;
    }
}
