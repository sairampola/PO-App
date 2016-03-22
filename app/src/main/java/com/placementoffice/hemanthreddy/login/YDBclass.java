package com.placementoffice.hemanthreddy.login;

/**
 * Created by hemanthreddy on 3/21/2016.
 */
public class YDBclass {

    String companyname,lastdate,designation,jobid;
    boolean isEligible;
    public YDBclass()
    {

    }

    public YDBclass(String companyname, String lastdate, String designation, boolean isEligible,String jobid) {
        this.companyname = companyname;
        this.lastdate = lastdate;
        this.designation = designation;
        this.isEligible = isEligible;
        this.jobid = jobid;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public boolean isEligible() {
        return isEligible;
    }

    public void setIsEligible(boolean isEligible) {
        this.isEligible = isEligible;
    }

    public String getCompanyname() {

        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getLastdate() {
        return lastdate;
    }

    public void setLastdate(String lastdate) {
        this.lastdate = lastdate;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
