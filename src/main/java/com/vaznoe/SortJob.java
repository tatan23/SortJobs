package com.vaznoe;

import com.vaznoe.model.JobDetails;

import java.io.IOException;
import java.util.List;

import static com.vaznoe.AppHelper.*;

public class SortJob {

    public static void main(String[] args) throws IOException {
        List<JobDetails> jobDetails = readFromFile();
        List<String> theMostPaidJobByUniqueName = getUniqueNameWithMostPaidJob(jobDetails);
        writeResult(theMostPaidJobByUniqueName);
    }
}
