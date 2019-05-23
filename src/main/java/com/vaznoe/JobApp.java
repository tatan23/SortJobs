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

public class JobApp {

    private static final String fileName = "City_of_Seattle_Wage_Data.csv";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        List<JobDetails> jobDetails = readFromFile();
        List<String> uniqueNameWithMostPaidJob = getUniqueNameWithMostPaidJob(jobDetails);
        writeResult(uniqueNameWithMostPaidJob);
    }

    private static void writeResult(List<String> uniqueNameWithMostPaidJob) throws IOException {
        FileWriter fileWriter = new FileWriter("result.json");
        fileWriter.write(String.valueOf(uniqueNameWithMostPaidJob));
        fileWriter.close();
    }

    private static List<String> getUniqueNameWithMostPaidJob(List<JobDetails> jobDetails) throws IOException {
        List<String> uniqueNameWithMostPaidJob = new ArrayList<>();
        Map<String, JobOutput> uniqueNameMostPaidJob = getUniqueNameMostPaidJob(jobDetails);
        for (Map.Entry<String, JobOutput> records : uniqueNameMostPaidJob.entrySet()) {
            String record = objectMapper.writeValueAsString(records);
            uniqueNameWithMostPaidJob.add(record);
        }
        return uniqueNameWithMostPaidJob;
    }

    private static Map<String, JobOutput> getUniqueNameMostPaidJob(List<JobDetails> jobDetails) {
        Map<String, JobOutput> uniqueNameMostPaidJob = new HashMap<>();
        Map<String, List<JobDetails>> jobsByName = jobDetails.stream()
                .collect(Collectors.groupingBy(JobDetails::getFirstName));

        jobsByName.entrySet().forEach(oneNameEntry -> {
            Map<String, List<JobDetails>> titles = oneNameEntry.getValue().stream().collect(Collectors.groupingBy(JobDetails::getJobTitle));
            List<JobOutput> jobOutputs = new ArrayList<>();
            titles.entrySet().forEach(oneTitleEntry -> {
                JobOutput jobOutput = new JobOutput();
                jobOutput.department = oneTitleEntry.getValue().get(0).getDepartment();
                jobOutput.jobTitle = oneTitleEntry.getKey();
                jobOutput.averageHourlyRate = oneTitleEntry.getValue().stream().mapToDouble(JobDetails::getHourlyRate).average().getAsDouble();
                jobOutputs.add(jobOutput);
            });
            JobOutput theBestJob = jobOutputs.stream()
                    .max(Comparator.comparing(JobOutput::getAverageHourlyRate))
                    .get();
            uniqueNameMostPaidJob.put(oneNameEntry.getKey(), theBestJob);
        });
        return uniqueNameMostPaidJob;
    }

    private static List<JobDetails> readFromFile() throws IOException {
        List<JobDetails> jobs = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            String[] values = csvReader.readNext();
            while ((values = csvReader.readNext()) != null) {
                JobDetails job = new JobDetails();
                job.department = values[0];
                job.lastName = values[1];
                job.firstName = values[2];
                job.jobTitle = values[3];
                job.hourlyRate = Double.parseDouble(values[4]);
                jobs.add(job);
            }
        }
        List<JobDetails> result = jobs.stream()
                .filter(producer -> !producer.getJobTitle().contains("III"))
                .filter(producer -> !producer.getJobTitle().contains("II"))
                .filter(producer -> !producer.getJobTitle().contains("Sr"))
                .collect(Collectors.toList());
        return result;
    }
}
