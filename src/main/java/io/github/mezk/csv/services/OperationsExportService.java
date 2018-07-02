package io.github.mezk.csv.services;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public interface OperationsExportService {

    void writePaging(PrintWriter stream);

    void writeStreaming(PrintWriter writer);

    void writeIterable(PrintWriter stream);
}
