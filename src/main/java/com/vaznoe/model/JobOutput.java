package com.vaznoe.model;

public class JobOutput {

    private String department;
    private String jobTitle;
    private double averageHourlyRate;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public double getAverageHourlyRate() {
        return averageHourlyRate;
    }

    public void setAverageHourlyRate(double averageHourlyRate) {
        this.averageHourlyRate = averageHourlyRate;
    }
}
