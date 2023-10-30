package ru.cryptopro.support.DssJavaClient.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.cryptopro.support.DssJavaClient.dto.documents.DocsListDto;
import ru.cryptopro.support.DssJavaClient.dto.documents.GetDocumentInfoResultDto;
import ru.cryptopro.support.DssJavaClient.dto.common.UserCredentialDto;
import ru.cryptopro.support.DssJavaClient.service.DocStoreService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@Log4j2
@RestController
@RequestMapping(value = "/api/doc/", produces = MediaType.APPLICATION_JSON_VALUE)
public class DocsController {
    private final DocStoreService docStoreService;

    public DocsController(DocStoreService docStoreService) {
        this.docStoreService = docStoreService;
    }

    @Operation(summary = "Upload file to DocumentStore")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public DocsListDto upload(
            @RequestHeader String username, @RequestHeader(required = false) String password,
            @RequestParam(value = "file") List<MultipartFile> files
    ) {
        log.info("Received request for user {}", username);
        return docStoreService.upload(new UserCredentialDto(username, password), files);
    }

    @Operation(summary = "Retrieve information about documents pack")
    @PostMapping("/info")
    public Map<UUID, GetDocumentInfoResultDto> getDocInfo(
            @RequestHeader String username, @RequestHeader(required = false) String password,
            @RequestBody List<UUID> docs
    ) {
        log.info("Received request for user {}", username);
        return docStoreService.getDocsInfo(new UserCredentialDto(username, password), docs);
    }

    @Operation(summary = "Download document by id")
    @GetMapping(value = "/download/{doc}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadDocument(
            @RequestHeader String username, @RequestHeader(required = false) String password,
            @PathVariable UUID doc) {
        log.info("Received request for user {}", username);
        return docStoreService.download(new UserCredentialDto(username, password), doc);
    }

    @Operation(summary = "List documents UUID")
    @GetMapping("/list")
    public List<UUID> getAllDocsIds(
            @RequestHeader String username, @RequestHeader(required = false) String password
    ) {
        log.info("Received request for user {}", username);
        return docStoreService.getAllDocs(new UserCredentialDto(username, password));
    }

    @Operation(summary = "Delete documents from DocumentStore")
    @PostMapping("/delete")
    public Map<UUID, String> deleteDocs(
            @RequestHeader String username, @RequestHeader(required = false) String password,
            @RequestBody List<UUID> docs
    ) {
        log.info("Received request for user {}", username);
        return docStoreService.delete(new UserCredentialDto(username, password), docs);
    }
}
