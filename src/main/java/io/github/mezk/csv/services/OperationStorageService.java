package io.github.mezk.csv.services;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.mezk.csv.models.Operation;

public interface OperationStorageService {

    Page<Operation> getOperationsPage(Pageable pageable);

    Stream<Operation> getOperationsStream();

    Iterable<Operation> getAll();
}
