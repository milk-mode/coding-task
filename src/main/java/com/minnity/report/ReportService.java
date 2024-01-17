package com.minnity.report;

import com.minnity.exceptions.RequestNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class ReportService {

    //task 1: Return number of requests that were made for each company. (e.g. companyId -> requestNumber)
    public Map<Integer, Long> calculateNumberOfRequestsPerCompany(List<RequestLog> requestLogs)  {

        return requestLogs.stream()
                .filter(rl -> (rl.getRequestMethod().equals("PUT")
                        || rl.getRequestMethod().equals("GET")
                        || rl.getRequestMethod().equals("POST")))
                .collect(groupingBy(RequestLog::getCompanyId, Collectors.counting()));
    }

    //task 2: Count and return requests per company that finished with an error HTTP response code (>=400)
    public Map<Integer, List<RequestLog>> findRequestsWithError(List<RequestLog> requestLogs) {
        return requestLogs.stream()
                .filter(rl -> rl.getRequestStatus() >= 400)
                .collect(groupingBy(RequestLog::getCompanyId, Collectors.toList()));
    }

    //task 3: find and print API (requests path) that on average takes the longest time to process the request.
    public String findRequestPathWithLongestDurationTime(List<RequestLog> requestLogs) throws RequestNotFoundException {
        //First get average durations for each endpoint
        Map<String, Double> averageDurations = requestLogs.stream()
                .collect(groupingBy(RequestLog::getRequestPath, Collectors.averagingLong(RequestLog::getRequestDuration)));

        //Then find the max
        return averageDurations.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new RequestNotFoundException("No request matching condition found"));


    }
}
