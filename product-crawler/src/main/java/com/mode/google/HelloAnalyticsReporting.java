package com.mode.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;

import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;

public class HelloAnalyticsReporting {
    private static final String APPLICATION_NAME = "Hello Analytics Reporting";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String KEY_FILE_LOCATION = "/Users/zhaoweiwei/IdeaProjects/data-platform/product-crawler/src/main/java/com/mode/google/Google Api for developer-f2919c6dd641.json";
    private static final String VIEW_ID = "162191270";

    private static int i = 0;
    private static Set<String> set = new HashSet<>();


    public static void main(String[] args) {
        try {
            AnalyticsReporting service = initializeAnalyticsReporting();

//            GetReportsResponse response =
                    getReport(service);
//            printResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes an Analytics Reporting API V4 service object.
     *
     * @return An authorized Analytics Reporting API V4 service object.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private static AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = GoogleCredential
                .fromStream(new FileInputStream(KEY_FILE_LOCATION))
                .createScoped(AnalyticsReportingScopes.all());

        // Construct the Analytics Reporting service object.
        return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
    }

    /**
     * Queries the Analytics Reporting API V4.
     *
     * @param service An authorized Analytics Reporting API V4 service object.
     * @return GetReportResponse The Analytics Reporting API V4 response.
     * @throws IOException
     */
    private static void getReport(AnalyticsReporting service) throws IOException {
        // Create the DateRange object.
        DateRange dateRange = new DateRange();
        dateRange.setStartDate("7DaysAgo");
        dateRange.setEndDate("today");

        // Create the Metrics object.
//        Metric sessions = new Metric()
//                .setExpression("ga:sessions")
//                .setAlias("sessions");

        Metric productDetailViews = new Metric().setExpression("ga:productDetailViews")
                .setAlias("product detail views");
//        Metric products = new Metric().setExpression("ga:products").setAlias("products");

//        Dimension pageTitle = new Dimension().setName("ga:pageTitle");
        Dimension product = new Dimension().setName("ga:productName");
        Dimension date = new Dimension().setName("ga:date");

        List<Dimension> dimensions = new ArrayList<>();
        dimensions.add(product);
        dimensions.add(date);

        String nextPageToken = "";

        while (nextPageToken != null) {
            // Create the ReportRequest object.
            ReportRequest request = new ReportRequest()
                    .setViewId(VIEW_ID)
                    .setDateRanges(Arrays.asList(dateRange))
                    .setMetrics(Arrays.asList(productDetailViews))
                    .setDimensions(dimensions)
                    .setPageSize(10000)
                    .setPageToken(nextPageToken);


            ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
            requests.add(request);


            // Create the GetReportsRequest object.
            GetReportsRequest getReport = new GetReportsRequest()
                    .setReportRequests(requests);

            // Call the batchGet method.
            GetReportsResponse response = service.reports().batchGet(getReport).execute();
            nextPageToken = printResponse(response);
        }

        System.out.println(i);

        for (String s : set) {
            System.out.println(s);
        }
        // Return the response.
//        return response;
    }

    /**
     * Parses and prints the Analytics Reporting API V4 response.
     *
     * @param response An Analytics Reporting API V4 response.
     */
    private static String printResponse(GetReportsResponse response) {
        String nextPageToken = null;
        for (Report report: response.getReports()) {
            System.out.println("token -> " + report.getNextPageToken());
            nextPageToken = report.getNextPageToken();
            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            if (rows == null) {
                System.out.println("No data found for " + VIEW_ID);
                return null;
            }
            System.out.println(rows.size());
            for (ReportRow row: rows) {

                i ++;
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();

                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
//                    System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
                }

                for (int j = 0; j < metrics.size(); j++) {
//                    System.out.print("Date Range (" + j + "): ");

                    DateRangeValues values = metrics.get(j);
                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
//                        System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
                    }
                }
            }
        }
        return nextPageToken;
    }
}
