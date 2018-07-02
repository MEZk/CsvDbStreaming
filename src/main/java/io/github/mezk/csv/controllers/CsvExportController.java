package io.github.mezk.csv.controllers;

import java.io.PrintWriter;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.mezk.csv.services.OperationsExportService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/export/csv", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CsvExportController {

    private final OperationsExportService csvWritingService;

    @RequestMapping(method = RequestMethod.GET)
    @SneakyThrows
    public void export(@RequestParam("type") String type, HttpServletResponse response) {

        createHeaders(response);

        try (PrintWriter writer = response.getWriter()) {
            switch (type) {
                case "stream":
                    csvWritingService.writeStreaming(writer);
                    break;
                case "page":
                    csvWritingService.writePaging(writer);
                    break;
                case "iterable":
                    csvWritingService.writeIterable(writer);
                    break;
            }
        }

    }

    private void createHeaders(HttpServletResponse response) {
        final String csvFileName = "operations.csv";
        response.setContentType("text/csv");

        final String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headerValue);
    }

}
