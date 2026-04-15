package com.devix.service;

import com.devix.domain.CompaniaTheme;
import com.devix.repository.CompaniaThemeRepository;
import com.devix.security.company.CompanyContextService;
import java.util.Locale;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class CompanyThemeService {

    private static final String DEFAULT_THEME = "litera";

    private static final Set<String> ALLOWED_THEMES = Set.of(
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

    private final CompanyContextService companyContextService;
    private final CompaniaThemeRepository companiaThemeRepository;
    private final FileStorageService fileStorageService;
    private final CompanyFilePathService companyFilePathService;

    public CompanyThemeService(
        CompanyContextService companyContextService,
        CompaniaThemeRepository companiaThemeRepository,
        FileStorageService fileStorageService,
        CompanyFilePathService companyFilePathService
    ) {
        this.companyContextService = companyContextService;
        this.companiaThemeRepository = companiaThemeRepository;
        this.fileStorageService = fileStorageService;
        this.companyFilePathService = companyFilePathService;
    }

    @Transactional(readOnly = true)
    public CompaniaTheme getCurrentThemeOrDefault() {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        return companiaThemeRepository
            .findOneByNoCia(noCia)
            .orElseGet(() -> {
                CompaniaTheme theme = new CompaniaTheme();
                theme.setNoCia(noCia);
                theme.setThemeName(DEFAULT_THEME);
                return theme;
            });
    }

    public CompaniaTheme upsertCurrentTheme(String themeName) {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        String normalized = normalizeTheme(themeName);

        CompaniaTheme theme = companiaThemeRepository
            .findOneByNoCia(noCia)
            .orElseGet(() -> {
                CompaniaTheme created = new CompaniaTheme();
                created.setNoCia(noCia);
                created.setThemeName(DEFAULT_THEME);
                return created;
            });
        theme.setThemeName(normalized);
        return companiaThemeRepository.save(theme);
    }

    public CompaniaTheme uploadAssets(MultipartFile logo, MultipartFile background) {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        CompaniaTheme theme = companiaThemeRepository
            .findOneByNoCia(noCia)
            .orElseGet(() -> {
                CompaniaTheme created = new CompaniaTheme();
                created.setNoCia(noCia);
                created.setThemeName(DEFAULT_THEME);
                return created;
            });

        if (logo != null && !logo.isEmpty()) {
            validateImageFile(logo);
            java.nio.file.Path rootLocation = companyFilePathService.resolveCurrentCompanyRootLocationOrThrow();
            theme.setLogoPath(fileStorageService.store(logo, rootLocation));
        }
        if (background != null && !background.isEmpty()) {
            validateImageFile(background);
            java.nio.file.Path rootLocation = companyFilePathService.resolveCurrentCompanyRootLocationOrThrow();
            theme.setBackgroundPath(fileStorageService.store(background, rootLocation));
        }

        return companiaThemeRepository.save(theme);
    }

    public CompaniaTheme clearAssets(boolean clearLogo, boolean clearBackground) {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        CompaniaTheme theme = companiaThemeRepository
            .findOneByNoCia(noCia)
            .orElseGet(() -> {
                CompaniaTheme created = new CompaniaTheme();
                created.setNoCia(noCia);
                created.setThemeName(DEFAULT_THEME);
                return created;
            });

        if (clearLogo) {
            theme.setLogoPath(null);
        }
        if (clearBackground) {
            theme.setBackgroundPath(null);
        }

        return companiaThemeRepository.save(theme);
    }

    public static boolean isAllowedTheme(String themeName) {
        if (themeName == null) {
            return false;
        }
        return ALLOWED_THEMES.contains(themeName.trim().toLowerCase());
    }

    private String normalizeTheme(String themeName) {
        if (themeName == null || themeName.isBlank()) {
            return DEFAULT_THEME;
        }
        String normalized = themeName.trim().toLowerCase();
        if (!ALLOWED_THEMES.contains(normalized)) {
            return DEFAULT_THEME;
        }
        return normalized;
    }

    private void validateImageFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("Archivo invalido");
        }
        String lower = originalFilename.toLowerCase(Locale.ROOT);
        if (
            !(lower.endsWith(".png") ||
                lower.endsWith(".jpg") ||
                lower.endsWith(".jpeg") ||
                lower.endsWith(".gif") ||
                lower.endsWith(".webp"))
        ) {
            throw new IllegalArgumentException("Solo se permiten imagenes png/jpg/jpeg/gif/webp");
        }
    }
}
