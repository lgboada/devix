package com.devix.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SmtpMailService {

    private static final Logger LOG = LoggerFactory.getLogger(SmtpMailService.class);

    private final Environment environment;

    public SmtpMailService(Environment environment) {
        this.environment = environment;
    }

    public void sendHtmlEmail(String from, String to, String cc, String subject, String htmlMessage, List<File> attachments)
        throws Exception {
        if (!StringUtils.hasText(to)) {
            throw new IllegalArgumentException("El parametro 'to' es obligatorio");
        }
        if (!StringUtils.hasText(subject)) {
            throw new IllegalArgumentException("El parametro 'subject' es obligatorio");
        }
        if (!StringUtils.hasText(htmlMessage)) {
            throw new IllegalArgumentException("El parametro 'htmlMessage' es obligatorio");
        }

        JavaMailSenderImpl mailSender = buildMailSender();
        var mimeMessage = mailSender.createMimeMessage();
        boolean hasAttachments = attachments != null && !attachments.isEmpty();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, hasAttachments, StandardCharsets.UTF_8.name());

        String configuredUser = environment.getProperty("mail.smtp.user");
        String effectiveFrom = StringUtils.hasText(from) ? from : configuredUser;
        if (!StringUtils.hasText(effectiveFrom)) {
            throw new IllegalStateException("No se encontro remitente. Envie 'from' o configure mail.smtp.user");
        }

        helper.setFrom(effectiveFrom);
        helper.setTo(parseAddresses(to));
        if (StringUtils.hasText(cc)) {
            helper.setCc(parseAddresses(cc));
        }
        helper.setSubject(subject);
        helper.setText(htmlMessage, true);

        if (hasAttachments) {
            for (File file : attachments) {
                if (file == null) {
                    continue;
                }
                if (!file.exists() || !file.isFile()) {
                    throw new IllegalArgumentException("Archivo adjunto invalido: " + file.getAbsolutePath());
                }
                helper.addAttachment(file.getName(), new FileSystemResource(file));
            }
        }

        mailSender.send(mimeMessage);
        LOG.debug("Correo enviado exitosamente a {}", to);
    }

    private JavaMailSenderImpl buildMailSender() {
        String host = environment.getProperty("mail.smtp.host");
        Integer port = environment.getProperty("mail.smtp.port", Integer.class, 25);
        Boolean starttls = environment.getProperty("mail.smtp.starttls.enable", Boolean.class, false);
        Boolean auth = environment.getProperty("mail.smtp.auth", Boolean.class, false);
        String user = environment.getProperty("mail.smtp.user");
        String password = environment.getProperty("mail.smtp.password");

        if (!StringUtils.hasText(host)) {
            throw new IllegalStateException("No esta configurado mail.smtp.host");
        }

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(user);
        sender.setPassword(password);
        sender.setDefaultEncoding(StandardCharsets.UTF_8.name());

        var properties = sender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", String.valueOf(auth));
        properties.put("mail.smtp.starttls.enable", String.valueOf(starttls));
        properties.put("mail.smtp.connectiontimeout", "5000");
        properties.put("mail.smtp.timeout", "5000");
        properties.put("mail.smtp.writetimeout", "5000");

        return sender;
    }

    private String[] parseAddresses(String addresses) {
        return StringUtils.commaDelimitedListToStringArray(addresses);
    }
}
