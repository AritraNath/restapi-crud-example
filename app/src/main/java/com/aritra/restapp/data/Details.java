package com.aritra.restapp.data;

public class Details {
    private String perName, perLoc, perMob, perSkill, perLink;
    private String eduOrg, eduDegree, eduLoc, eduStartYear, eduEndYear;
    private String proOrg, proDesignation, proStartDate, proEndDate;

    public Details() {
    }

    public void setPersonalDetails(String perName, String perLoc, String perMob, String perSkill, String perLink) {
        this.perName = perName;
        this.perLoc = perLoc;
        this.perMob = perMob;
        this.perSkill = perSkill;
        this.perLink = perLink;
    }

    public void setEducationalDetails(String eduOrg, String eduDegree, String eduLoc, String eduStartYear, String eduEndYear) {
        this.eduOrg = eduOrg;
        this.eduDegree = eduDegree;
        this.eduLoc = eduLoc;
        this.eduStartYear = eduStartYear;
        this.eduEndYear = eduEndYear;
    }

    public void setProfessionalDetails(String proOrg, String proDesignation, String proStartDate, String proEndDate) {
        this.proOrg = proOrg;
        this.proDesignation = proDesignation;
        this.proStartDate = proStartDate;
        this.proEndDate = proEndDate;
    }

    public String getPerName() {
        return perName;
    }

    public void setPerName(String perName) {
        this.perName = perName;
    }

    public String getPerLoc() {
        return perLoc;
    }

    public void setPerLoc(String perLoc) {
        this.perLoc = perLoc;
    }

    public String getPerMob() {
        return perMob;
    }

    public void setPerMob(String perMob) {
        this.perMob = perMob;
    }

    public String getPerSkill() {
        return perSkill;
    }

    public void setPerSkill(String perSkill) {
        this.perSkill = perSkill;
    }

    public String getPerLink() {
        return perLink;
    }

    public void setPerLink(String perLink) {
        this.perLink = perLink;
    }

    public String getEduOrg() {
        return eduOrg;
    }

    public void setEduOrg(String eduOrg) {
        this.eduOrg = eduOrg;
    }

    public String getEduDegree() {
        return eduDegree;
    }

    public void setEduDegree(String eduDegree) {
        this.eduDegree = eduDegree;
    }

    public String getEduLoc() {
        return eduLoc;
    }

    public void setEduLoc(String eduLoc) {
        this.eduLoc = eduLoc;
    }

    public String getEduStartYear() {
        return eduStartYear;
    }

    public void setEduStartYear(String eduStartYear) {
        this.eduStartYear = eduStartYear;
    }

    public String getEduEndYear() {
        return eduEndYear;
    }

    public void setEduEndYear(String eduEndYear) {
        this.eduEndYear = eduEndYear;
    }

    public String getProOrg() {
        return proOrg;
    }

    public void setProOrg(String proOrg) {
        this.proOrg = proOrg;
    }

    public String getProDesignation() {
        return proDesignation;
    }

    public void setProDesignation(String proDesignation) {
        this.proDesignation = proDesignation;
    }

    public String getProStartDate() {
        return proStartDate;
    }

    public void setProStartDate(String proStartDate) {
        this.proStartDate = proStartDate;
    }

    public String getProEndDate() {
        return proEndDate;
    }

    public void setProEndDate(String proEndDate) {
        this.proEndDate = proEndDate;
    }
}
