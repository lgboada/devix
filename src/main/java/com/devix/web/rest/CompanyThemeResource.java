package com.devix.web.rest;

import com.devix.domain.CompaniaTheme;
import com.devix.security.AuthoritiesConstants;
import com.devix.service.CompanyThemeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/company-theme")
public class CompanyThemeResource {

    private final CompanyThemeService companyThemeService;

    public CompanyThemeResource(CompanyThemeService companyThemeService) {
        this.companyThemeService = companyThemeService;
    }

    @GetMapping("/current")
    public ResponseEntity<CompanyThemeVM> getCurrent() {
        CompaniaTheme theme = companyThemeService.getCurrentThemeOrDefault();
        return ResponseEntity.ok(toVm(theme));
    }

    @PutMapping("/current")
    @PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
    public ResponseEntity<CompanyThemeVM> updateCurrent(@Valid @RequestBody UpdateCompanyThemeVM body) {
        if (!CompanyThemeService.isAllowedTheme(body.themeName())) {
            return ResponseEntity.badRequest().body(new CompanyThemeVM("litera", null, null));
        }
        CompaniaTheme theme = companyThemeService.upsertCurrentTheme(body.themeName());
        return ResponseEntity.ok(toVm(theme));
    }

    @PostMapping(value = "/current/assets", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
    public ResponseEntity<CompanyThemeVM> uploadAssets(
        @RequestPart(value = "logo", required = false) MultipartFile logo,
        @RequestPart(value = "background", required = false) MultipartFile background
    ) {
        CompaniaTheme theme = companyThemeService.uploadAssets(logo, background);
        return ResponseEntity.ok(toVm(theme));
    }

    @GetMapping("/themes")
    public ResponseEntity<Map<String, Object>> getAllowedThemes() {
        List<String> themes = List.of(
            "cerulean",
            "cosmo",
            "cyborg",
            "darkly",
            "flatly",
            "journal",
            "litera",
            "lumen",
            "lux",
            "materia",
            "minty",
            "morph",
            "pulse",
            "quartz",
            "sandstone",
            "simplex",
            "sketchy",
            "slate",
            "solar",
            "spacelab",
            "superhero",
            "united",
            "vapor",
            "yeti",
            "zephyr"
        );
        return ResponseEntity.ok(Map.of("default", "litera", "themes", themes));
    }

    private CompanyThemeVM toVm(CompaniaTheme theme) {
        String logoUrl = theme.getLogoPath() == null ? null : "/api/files/" + theme.getLogoPath();
        String backgroundUrl = theme.getBackgroundPath() == null ? null : "/api/files/" + theme.getBackgroundPath();
        return new CompanyThemeVM(theme.getThemeName(), logoUrl, backgroundUrl);
    }

    public record CompanyThemeVM(String themeName, String logoUrl, String backgroundUrl) {}

    public record UpdateCompanyThemeVM(@NotNull @NotBlank String themeName) {}
}
