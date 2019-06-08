package com.vaznoe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.vaznoe.model.JobDetails;
import com.vaznoe.model.JobOutput;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.vaznoe.utils.Utils.RESULT;
import static com.vaznoe.utils.Utils.WAGE_DATA;

public class AppHelper {

    public static void writeResult(List<String> uniqueNameWithMostPaidJob) throws IOException {
        FileWriter fileWriter = new FileWriter(RESULT);
        fileWriter.write(String.valueOf(uniqueNameWithMostPaidJob));
        fileWriter.close();
    }

    public static List<String> getUniqueNameWithMostPaidJob(List<JobDetails> jobDetails) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> uniqueNameWithMostPaidJob = new ArrayList<>();
        Map<String, JobOutput> uniqueNameMostPaidJob = getUniqueNameMostPaidJob(jobDetails);
        for (Map.Entry<String, JobOutput> records : uniqueNameMostPaidJob.entrySet()) {
            String record = objectMapper.writeValueAsString(records);
            uniqueNameWithMostPaidJob.add(record);
        }
        return uniqueNameWithMostPaidJob;
    }

    public static List<JobDetails> readFromFile() throws IOException {
        List<JobDetails> jobs = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(WAGE_DATA))) {
            String[] values = csvReader.readNext();
            while ((values = csvReader.readNext()) != null) {
                JobDetails job = JobDetails.builder()
                        .department(values[0])
                        .lastName(values[1])
                        .firstName(values[2])
                        .jobTitle(values[3])
                        .hourlyRate(Double.parseDouble(values[4]))
                        .build();
                jobs.add(job);
            }
        }
        List<JobDetails> result = jobs.stream()
                .filter(record -> !record.getJobTitle().contains("III"))
                .filter(record -> !record.getJobTitle().contains("II"))
                .filter(record -> !record.getJobTitle().contains("Sr"))
                .collect(Collectors.toList());
        return result;
    }

    private static Map<String, JobOutput> getUniqueNameMostPaidJob(List<JobDetails> jobDetails) {
        Map<String, JobOutput> theMostPaidJob = new HashMap<>();
        Map<String, List<JobDetails>> jobsByName = jobDetails.stream()
                .collect(Collectors.groupingBy(JobDetails::getFirstName));

        jobsByName.entrySet().forEach(oneNameEntry -> {
            Map<String, List<JobDetails>> jobByTitles = oneNameEntry.getValue().stream().collect(Collectors.groupingBy(JobDetails::getJobTitle));
            List<JobOutput> result = new ArrayList<>();
            jobByTitles.entrySet().forEach(tEntry -> {
                JobOutput jobOutput = JobOutput.builder()
                        .department(tEntry.getValue().get(0).getDepartment())
                        .jobTitle(tEntry.getKey())
                        .averageHourlyRate(tEntry.getValue().stream().mapToDouble(JobDetails::getHourlyRate).average().getAsDouble())
                        .build();
                result.add(jobOutput);
            });
            JobOutput theBestJob = result.stream()
                    .max(Comparator.comparing(JobOutput::getAverageHourlyRate))
                    .get();
            theMostPaidJob.put(oneNameEntry.getKey(), theBestJob);
        });
        return theMostPaidJob;
    }
}
