package ru.cryptopro.support.DssJavaClient.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.cryptopro.support.DssJavaClient.entity.interfaces.AccessToken;

import java.util.Date;
import java.util.UUID;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delegated_token", indexes = @Index(columnList = "username"))
public class DelegatedTokenEntity implements AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID guid;
    @Column(columnDefinition="TEXT")
    private String token;
    private String username;
    private Date expireAt;
}

