package com.vaznoe.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @Builder
public class JobDetails {

    private String department;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private double hourlyRate;
}
