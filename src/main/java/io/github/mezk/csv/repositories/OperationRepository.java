package io.github.mezk.csv.repositories;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

import java.util.UUID;
import java.util.stream.Stream;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import io.github.mezk.csv.models.Operation;

public interface OperationRepository extends PagingAndSortingRepository<Operation, UUID> {

    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "" + 50))
    @Query(value = "SELECT o FROM Operation AS o")
    Stream<Operation> streamAll();
}
