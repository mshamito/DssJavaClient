package ru.cryptopro.support.DssJavaClient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.cryptopro.support.DssJavaClient.entity.interfaces.AccessToken;

import java.util.Date;
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "operator_token")
public class OperatorAccessTokenEntity implements AccessToken {
        @Id
        private final long operator = 1;
        @Column(columnDefinition="TEXT")
        private String token;
        private Date expireAt;
}
