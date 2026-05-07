package com.errolito.mycrud.entity;

import com.errolito.mycrud.enums.AuditStatus;
import com.errolito.mycrud.enums.AuditType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(
        name = "fund_transfer_audits",
        indexes = {
                @Index(name = "idx_fund_transfer_audit_reference_id", columnList = "referenceId"),
                @Index(name = "idx_fund_transfer_audit_reference_id_audit_type", columnList = "referenceId,auditType"),
                @Index(name = "idx_fund_transfer_audit_origin_acccount_number", columnList = "originAccountNumber"),
                @Index(name = "idx_fund_transfer_audit_destination_acccount_number", columnList = "destinationAccountNumber"),
                @Index(name = "idx_fund_transfer_created_date", columnList = "createdDate DESC")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FundTransferAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String referenceId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    private String originAccountNumber;
    private String destinationAccountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditType auditType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditStatus auditStatus;

    @CreatedDate
    @ColumnDefault("NOW()")
    @Column(nullable = false)
    private Instant createdDate;
}