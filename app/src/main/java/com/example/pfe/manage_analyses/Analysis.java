package com.example.pfe.manage_analyses;

public class Analysis {
    private String analysis_name;
    private String analysis_date;
    private String result, owner;

    public Analysis(String analysis_name, String analysis_date, String result, String owner) {
        this.analysis_name=analysis_name;
        this.analysis_date=analysis_date;
        this.result=result;
        this.owner=owner;
    }

    public String getAnalysis_name() {
        return analysis_name;
    }

    public void setAnalysis_name(String analysis_name) {
        this.analysis_name = analysis_name;
    }

    public String getAnalysis_date() {
        return analysis_date;
    }

    public void setAnalysis_date(String analysis_date) {
        this.analysis_date = analysis_date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
