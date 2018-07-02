package io.github.mezk.csv.services.impl;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.mezk.csv.models.Operation;
import io.github.mezk.csv.repositories.OperationRepository;
import io.github.mezk.csv.services.OperationStorageService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DbOperationStorageService implements OperationStorageService {

    private final OperationRepository repository;

    @Override
    public Page<Operation> getOperationsPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Stream<Operation> getOperationsStream() {
        return repository.streamAll();
    }

    @Override
    public Iterable<Operation> getAll() {
        return repository.findAll();
    }
}
