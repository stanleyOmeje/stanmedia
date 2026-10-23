package com.stan.product.product.controller;

import com.stan.product.product.config.ApplicationUrl;
import com.stan.product.product.enums.Templates;
import com.stan.product.product.exception.BadRequestException;
import com.stan.product.product.exception.NotFoundException;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Arrays;

@Slf4j
@RequestMapping(ApplicationUrl.TEMPLATE_BASE_URL)
@RestController
public class TemplateController {

    @GetMapping("/{templateName}")
    public ResponseEntity<Resource> downloadTemplate(@PathVariable String templateName) {
        log.info("Inside download template ...{}",templateName);
        try {
            if (isValidPath(templateName)) {
                Templates templates = Templates.valueOf(templateName.toUpperCase());

                InputStream value = getClass().getClassLoader().getResourceAsStream("static/" + templates.path);
                if (value == null) {
                    throw new NotFoundException("No resource found");
                }
                InputStreamResource resource = new InputStreamResource(value);
                return
                    ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + templates.path)
                        .contentType(MediaType.parseMediaType("text/csv"))
                        .body(resource);
            } else {
                throw new BadRequestException("File format incompactible");
            }

        } catch (NotFoundException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.info("Get template request failed => {}", e.getMessage());
            throw new NotFoundException("Could not fetch template", "99");
        }
    }

    private boolean isValidPath(String path) {
        return Arrays.stream(Templates.values()).anyMatch(i -> i.toString().equalsIgnoreCase(path));
    }
}
