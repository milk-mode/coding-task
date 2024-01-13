package com.minnity.report;

import com.minnity.exceptions.RequestNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportService {

    //task 1: Return number of requests that were made for each company. (e.g. companyId -> requestNumber)
    public Map<Integer, Long> calculateNumberOfRequestsPerCompany(List<RequestLog> requestLogs) throws RequestNotFoundException {
        Map<Integer, Long> numberOfRequestsMadeToCompany = new HashMap<>();

        for (RequestLog requestLog : requestLogs) {
            Integer companyId = requestLog.getCompanyId();
            Long totalNumberOfRequestsPerCompany = requestLogs.stream()
                    .filter(rl -> rl.getCompanyId() == companyId)
                    .filter(rl -> (rl.getRequestMethod().equals("PUT")
                            || rl.getRequestMethod().equals("GET")
                            || rl.getRequestMethod().equals("POST"))).count();

            numberOfRequestsMadeToCompany.put(companyId, totalNumberOfRequestsPerCompany);
        }

        if (numberOfRequestsMadeToCompany.size() >= 1)
            return numberOfRequestsMadeToCompany;
        throw new RequestNotFoundException("No request matching condition is found");

    }

    //task 2: Count and return requests per company that finished with an error HTTP response code (>=400)
    public Map<Integer, List<RequestLog>> findRequestsWithError(List<RequestLog> requestLogs) throws RequestNotFoundException {
        /*Changed method return parameter to Map<Integer,  List<RequestLog>>  to match companyId with requests per
          company that finished with an error HTTP response code (>=400)*/
        Map<Integer, List<RequestLog>> requestsWithError = new HashMap<>();

        for (RequestLog requestLog : requestLogs) {
            Integer companyId = requestLog.getCompanyId();
            List<RequestLog> reqLog = requestLogs.stream()
                    .filter(rl -> rl.getCompanyId() == companyId)
                    .filter(rl -> rl.getRequestStatus() >= 400).collect(Collectors.toList());

            requestsWithError.put(companyId, reqLog);
        }

        if (requestsWithError.size() >= 1)
            return requestsWithError;
        throw new RequestNotFoundException("No request matching condition is found");
    }

    //task 3: find and print API (requests path) that on average takes the longest time to process the request.
    public String findRequestPathWithLongestDurationTime(List<RequestLog> requestLogs) throws RequestNotFoundException {
        //First get average durations for each endpoint
        Map<String, Double> averageDurations = requestLogs.stream()
                .collect(Collectors.groupingBy(RequestLog::getRequestPath, Collectors.averagingLong(RequestLog::getRequestDuration)));

        //Then find the max
        return averageDurations.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new RequestNotFoundException("No request matching condition found"));


    }
}
