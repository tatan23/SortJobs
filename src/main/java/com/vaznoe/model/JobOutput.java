package com.vaznoe.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @Builder
public class JobOutput {

    private String department;
    private String jobTitle;
    private double averageHourlyRate;
}
