package com.devix.web.rest;

import com.devix.service.SmtpMailService;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/mail")
public class MailResource {

    private static final Logger LOG = LoggerFactory.getLogger(MailResource.class);

    private final SmtpMailService smtpMailService;

    public MailResource(SmtpMailService smtpMailService) {
        this.smtpMailService = smtpMailService;
    }

    @PostMapping(value = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> sendEmail(
        @RequestParam(value = "from", required = false) String from,
        @RequestParam("to") String to,
        @RequestParam(value = "cc", required = false) String cc,
        @RequestParam("subject") String subject,
        @RequestParam("htmlMessage") String htmlMessage,
        @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments
    ) throws Exception {
        LOG.debug("REST request to send email to {} with subject {}", to, subject);

        List<File> tempFiles = new ArrayList<>();
        try {
            List<File> filesToSend = toTempFiles(attachments, tempFiles);
            smtpMailService.sendHtmlEmail(from, to, cc, subject, htmlMessage, filesToSend);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Correo enviado correctamente");
            response.put("to", to);
            response.put("subject", subject);
            response.put("attachmentsCount", filesToSend.size());
            return ResponseEntity.ok(response);
        } finally {
            cleanupTempFiles(tempFiles);
        }
    }

    private List<File> toTempFiles(List<MultipartFile> attachments, List<File> tempFiles) throws Exception {
        List<File> filesToSend = new ArrayList<>();
        if (CollectionUtils.isEmpty(attachments)) {
            return filesToSend;
        }

        for (MultipartFile multipartFile : attachments) {
            if (multipartFile == null || multipartFile.isEmpty()) {
                continue;
            }

            String safePrefix = "mail-attachment-";
            String safeSuffix = extractFileSuffix(multipartFile.getOriginalFilename());
            File tempFile = File.createTempFile(safePrefix, safeSuffix);
            multipartFile.transferTo(tempFile);

            tempFiles.add(tempFile);
            filesToSend.add(tempFile);
        }
        return filesToSend;
    }

    private String extractFileSuffix(String originalFilename) {
        if (originalFilename == null) {
            return ".tmp";
        }
        int lastDot = originalFilename.lastIndexOf('.');
        if (lastDot < 0 || lastDot == originalFilename.length() - 1) {
            return ".tmp";
        }
        String ext = originalFilename.substring(lastDot);
        if (ext.length() > 10) {
            return ".tmp";
        }
        return ext;
    }

    private void cleanupTempFiles(List<File> tempFiles) {
        for (File tempFile : tempFiles) {
            if (tempFile == null) {
                continue;
            }
            try {
                Files.deleteIfExists(tempFile.toPath());
            } catch (Exception ex) {
                LOG.warn("No se pudo eliminar archivo temporal {}", tempFile.getAbsolutePath());
            }
        }
    }
}
