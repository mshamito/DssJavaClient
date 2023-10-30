package ru.cryptopro.support.DssJavaClient.dto.sign;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import ru.cryptopro.support.DssJavaClient.enums.*;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
@Builder
@Data
public class SignatureParamsDto {
    @JsonProperty("CADESType")
    private CADESTypeEnum cadesType;
    @JsonProperty("XMLDSigType")
    private XMLDSigTypeEnum xmlDSigType;
    @JsonProperty("PDFFormat")
    private PdfFormatEnum pdfFormat;
    @JsonProperty("IsDetached")
    private boolean isDetached;
    private boolean hash;
    @JsonProperty("TSPAddress")
    private String tspAddress;
    private CmsSignatureTypeEnum cmsSignatureType;
    private ContentEncodingEnum contentEncoding;
    // TODO OriginalDocument
    private String signatureIndex;
    private Map<String,String> authenticatedAttributes;
    private boolean includeCertChain;
    private boolean includeDocumentName;
    // TODO http://dss.cryptopro.ru/docs/articles/rest/signserver/structs/sigparams.html#a-idcades-params-%D0%BF%D0%BE%D0%B4%D0%BF%D0%B8%D1%81%D1%8C-%D1%84%D0%BE%D1%80%D0%BC%D0%B0%D1%82%D0%B0-cades-%D0%B8%D0%BB%D0%B8-cms
    private int hashAlgorithm;

    @JsonProperty("PDFReason")
    private String pdfReason;
    @JsonProperty("PDFLocation")
    private String pdfLocation;
    private String pdfSignatureAppearance;
    private String pdfSignatureTemplateId;
    @JsonProperty("PDFCertificationLevel")
    private String pdfCertificationLevel;
}
