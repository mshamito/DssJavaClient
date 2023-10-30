package ru.cryptopro.support.DssJavaClient.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.cryptopro.support.DssJavaClient.entity.interfaces.Expirable;

import java.util.Date;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "confirmations", indexes = @Index(columnList = "guid,username"))
public class ConfirmationEntity implements Expirable {
    @Id
    private UUID guid;
    private String username;
    private Date expireAt;
    private String status;
    private String error;
    private String errorDescription;
}
