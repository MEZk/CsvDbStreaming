package io.github.mezk.csv.services.impl;

import java.io.PrintWriter;
import java.io.Writer;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.mezk.csv.models.Operation;
import io.github.mezk.csv.services.OperationStorageService;
import io.github.mezk.csv.services.OperationsExportService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class CsvOperationsExportService implements OperationsExportService {

    private static final int PAGE_SIZE = 300;

    private static final char DELIMETER = ';';

    private static final String HEADERS = String.format(
        "ID%1$sTRANSACTION DATE%1$sSTATUS%1$sPAY METHOD%1$sPAY SYSTEM%1$sTERMINAL ID", DELIMETER);

    private final OperationStorageService storageService;
    private final EntityManager entityManager;

    @Override
    public void writeIterable(PrintWriter writer) {

        write(writer, HEADERS);

        final Iterable<Operation> operations = storageService.getAll();
        operations.forEach(operation -> write(writer, toCsvLine(operation)));
        writer.flush();
    }

    @Override
    @SneakyThrows
    @Transactional(readOnly = true)
    public void writePaging(PrintWriter writer) {

        write(writer, HEADERS);

        Pageable pageable = new PageRequest(
            0, PAGE_SIZE, new Sort(Sort.Direction.DESC, "transactionDate"));

        Page<Operation> page = null;

        while (shouldWritePage(page, writer)) {

            page = storageService.getOperationsPage(pageable);
            writeOperationsToCsvStream(page, writer);
            pageable = page.nextPageable();

        }

    }

    @Override
    @Transactional(readOnly = true)
    public void writeStreaming(PrintWriter writer) {
        write(writer, HEADERS);

        try(Stream<Operation> operationsStream = storageService.getOperationsStream()) {
            operationsStream.forEach(operation -> writeAndDetach(writer, operation));
        }

        writer.flush();

    }

    private boolean shouldWritePage(Page<Operation> operations, PrintWriter writer) {
        if (operations == null) {
            return true;
        }

        if (operations.isLast()) {
            return false;
        }

        // Cut down on amount of flush operations via checkError invokation
        if (operations.getNumber() % 10 == 0 ) {
            // Check for download cancellation by client
            if (writer.checkError()) {
                return false;
            }
        }

        return true;
    }

    @SneakyThrows
    private void writeOperationsToCsvStream(Page<Operation> operations, Writer writer) {

        for (Operation operation : operations.getContent()) {
            writeAndDetach(writer, operation);
        }

        writer.flush();
    }

    @SneakyThrows
    private void writeAndDetach(Writer writer, Operation operation) {
        write(writer, toCsvLine(operation));
        entityManager.detach(operation);
    }

    @SneakyThrows
    private void write(Writer writer, String headersStr) {
        writer.write(headersStr);
        writer.write(System.lineSeparator());
    }

    private String toCsvLine(Operation operation) {
        final StringBuilder sb = new StringBuilder();

        sb.append(operation.getId())
            .append(DELIMETER)
            .append(operation.getTransactionDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .append(DELIMETER)
            .append(operation.getStatus())
            .append(DELIMETER)
            .append(operation.getPaySystem())
            .append(DELIMETER)
            .append(operation.getPayMethod())
            .append(DELIMETER)
            .append(operation.getTerminalId())
            .append(DELIMETER);

        return sb.toString();
    }

}
