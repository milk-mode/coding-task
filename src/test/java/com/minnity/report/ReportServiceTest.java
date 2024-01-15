package com.minnity.report;

import com.minnity.exceptions.RequestNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ReportServiceTest {
    @InjectMocks
    private ReportService reportService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void calculateNumberOfRequestsPerCompanyReturnsCorrectSize() throws RequestNotFoundException {
        RequestLog requestLog1 = SampleDataGenerator.aRequestLog();
        RequestLog requestLog2 = SampleDataGenerator.aRequestLogForCompany2WithId10();

        List<RequestLog> requestLogs = new ArrayList<>(List.of(requestLog1, requestLog2));
        Map<Integer, Long> result = reportService.calculateNumberOfRequestsPerCompany(requestLogs);

        Assert.assertEquals(2, result.size());
    }

    @Test
    public void calculateNumberOfRequestsPerCompanyThrowsException() {
        List<RequestLog> requestLogs = new ArrayList<>();
        Assert.assertEquals(0, reportService.calculateNumberOfRequestsPerCompany(requestLogs).size());
    }

    @Test
    public void findRequestsWithErrorReturnsCorrectSize()  {
        RequestLog requestLog1 = SampleDataGenerator.aRequestLogWithStatusCode401();
        RequestLog requestLog2 = SampleDataGenerator.aRequestLogWithStatusCode404AndCompanyId10();


        List<RequestLog> requestLogs = new ArrayList<>(List.of(requestLog1, requestLog2));
        Map<Integer, List<RequestLog>> result = reportService.findRequestsWithError(requestLogs);

        Assert.assertEquals(2, result.size());
    }

    @Test
    public void findRequestsWithErrorReturnZeroForNoRequestLog() {
        List<RequestLog> requestLogs = new ArrayList<>();
        Assert.assertEquals(0, reportService.findRequestsWithError(requestLogs).size());
    }

    @Test
    public void findRequestPathWithLongestDurationTime() throws RequestNotFoundException {
        RequestLog requestLog1 = SampleDataGenerator.aRequestLogWithRequestPathToUsers();//takes 100 as duration
        RequestLog requestLog2 = SampleDataGenerator.aRequestLogWithRequestPathToPing();//takes 200 as duration


        List<RequestLog> requestLogs = new ArrayList<>(List.of(requestLog1, requestLog2));
        String result = reportService.findRequestPathWithLongestDurationTime(requestLogs);

        Assert.assertEquals("/ping", result);
    }

    @Test
    public void findRequestPathWithLongestDurationTimeThrowsException() {
        List<RequestLog> requestLogs = new ArrayList<>();
        Assert.assertThrows(RequestNotFoundException.class, () -> reportService.findRequestPathWithLongestDurationTime(requestLogs));
    }

}