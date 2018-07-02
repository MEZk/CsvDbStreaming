package io.github.mezk.csv.models;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "operations")
public class Operation {

    @Id
    @Column(name = "operation_id")
    private UUID id;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(name = "status")
    private String status;

    @Column(name = "pay_system")
    private String paySystem;

    @Column(name = "pay_method")
    private String payMethod;

    @Column(name = "terminal_id")
    private UUID terminalId;

}
