package ru.cryptopro.support.DssJavaClient.dto.documents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;
import ru.cryptopro.support.DssJavaClient.util.Utils;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class DocumentInfoDto {
    private UUID id;
    private UUID userId;
    private String name;
    private long uploadTime;
    private long lastAccessTime;
    private String filename;
    private Boolean isTemporary = true;
    private String hash;
    private String hashAlgorithm;
    private AdditionalInfoDto additionalInfo = null;
    private String parentDocumentId;
    private String fileType;
    private long fileSize;
    private String content;
    @JsonIgnore
    private byte[] rawData;

    public Date getUploadTime() {
        return new Date(uploadTime * 1000);
    }

    public Date getLastAccessTime() {
        return new Date(lastAccessTime * 1000);
    }

    @SneakyThrows
    public DocumentInfoDto(MultipartFile multipartFile) {
        setFilename(multipartFile.getOriginalFilename());
        setRawData(multipartFile.getBytes());
    }

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
