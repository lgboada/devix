package com.devix.service;

import com.devix.domain.Compania;
import com.devix.repository.CompaniaRepository;
import com.devix.service.dto.CompaniaDTO;
import com.devix.service.mapper.CompaniaMapper;
import com.devix.service.security.AesGcmCryptoService;
import com.devix.service.security.CompanyClientSecretService;
import com.devix.web.rest.errors.BadRequestAlertException;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devix.domain.Compania}.
 */
@Service
@Transactional
public class CompaniaService {

    private static final Logger LOG = LoggerFactory.getLogger(CompaniaService.class);
    private static final String ENTITY_NAME = "compania";

    private final CompaniaRepository companiaRepository;

    private final CompaniaMapper companiaMapper;

    private final CompanyClientSecretService companyClientSecretService;
    private final AesGcmCryptoService aesGcmCryptoService;

    public CompaniaService(
        CompaniaRepository companiaRepository,
        CompaniaMapper companiaMapper,
        CompanyClientSecretService companyClientSecretService,
        AesGcmCryptoService aesGcmCryptoService
    ) {
        this.companiaRepository = companiaRepository;
        this.companiaMapper = companiaMapper;
        this.companyClientSecretService = companyClientSecretService;
        this.aesGcmCryptoService = aesGcmCryptoService;
    }

    /**
     * Save a compania.
     *
     * @param companiaDTO the entity to save.
     * @return the persisted entity.
     */
    public CompaniaDTO save(CompaniaDTO companiaDTO) {
        LOG.debug("Request to save Compania : {}", companiaDTO);
        validateUniqueDni(companiaDTO);
        validateUniqueNoCia(companiaDTO);
        Compania compania = companiaMapper.toEntity(companiaDTO);
        applyClaveCertificadoEncryptionForCreateOrUpdate(compania, companiaDTO.getClaveCertificado());
        compania = companiaRepository.save(compania);
        return companiaMapper.toDto(compania);
    }

    /**
     * Update a compania.
     *
     * @param companiaDTO the entity to save.
     * @return the persisted entity.
     */
    public CompaniaDTO update(CompaniaDTO companiaDTO) {
        LOG.debug("Request to update Compania : {}", companiaDTO);
        validateUniqueDni(companiaDTO);
        validateUniqueNoCia(companiaDTO);
        Compania compania = companiaMapper.toEntity(companiaDTO);
        String incoming = companiaDTO.getClaveCertificado();
        if (incoming == null || incoming.isBlank()) {
            // No sobrescribir la clave existente si el formulario viene vacío
            String existingClave = companiaRepository.findById(companiaDTO.getId()).map(Compania::getClaveCertificado).orElse(null);
            compania.setClaveCertificado(existingClave);
        } else {
            applyClaveCertificadoEncryptionForCreateOrUpdate(compania, incoming);
        }
        compania = companiaRepository.save(compania);
        return companiaMapper.toDto(compania);
    }

    /**
     * Partially update a compania.
     *
     * @param companiaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompaniaDTO> partialUpdate(CompaniaDTO companiaDTO) {
        LOG.debug("Request to partially update Compania : {}", companiaDTO);

        return companiaRepository
            .findById(companiaDTO.getId())
            .map(existingCompania -> {
                companiaMapper.partialUpdate(existingCompania, companiaDTO);
                validateUniqueDni(existingCompania);
                validateUniqueNoCia(existingCompania);

                String incoming = companiaDTO.getClaveCertificado();
                if (incoming != null && !incoming.isBlank()) {
                    applyClaveCertificadoEncryptionForCreateOrUpdate(existingCompania, incoming);
                }
                return existingCompania;
            })
            .map(companiaRepository::save)
            .map(companiaMapper::toDto);
    }

    /**
     * Get one compania by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompaniaDTO> findOne(Long id) {
        LOG.debug("Request to get Compania : {}", id);
        return companiaRepository.findById(id).map(companiaMapper::toDto);
    }

    /**
     * Delete the compania by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Compania : {}", id);
        companiaRepository.deleteById(id);
    }

    private void applyClaveCertificadoEncryptionForCreateOrUpdate(Compania compania, String claveCertificadoPlaintextOrNull) {
        if (claveCertificadoPlaintextOrNull == null || claveCertificadoPlaintextOrNull.isBlank()) {
            // En creación permitimos null; en actualización se maneja arriba para no pisar
            compania.setClaveCertificado(null);
            return;
        }
        SecretKey key = companyClientSecretService.getAesKeyOrThrow(compania);
        compania.setClaveCertificado(aesGcmCryptoService.encrypt(claveCertificadoPlaintextOrNull, key));
    }

    private void validateUniqueDni(CompaniaDTO companiaDTO) {
        if (companiaDTO.getDni() == null) {
            return;
        }
        boolean exists = companiaDTO.getId() == null
            ? companiaRepository.existsByDni(companiaDTO.getDni())
            : companiaRepository.existsByDniAndIdNot(companiaDTO.getDni(), companiaDTO.getId());
        if (exists) {
            throw new BadRequestAlertException("El DNI ya existe para esta compania", ENTITY_NAME, "dniexists");
        }
    }

    private void validateUniqueDni(Compania compania) {
        if (compania.getDni() == null) {
            return;
        }
        boolean exists = compania.getId() == null
            ? companiaRepository.existsByDni(compania.getDni())
            : companiaRepository.existsByDniAndIdNot(compania.getDni(), compania.getId());
        if (exists) {
            throw new BadRequestAlertException("El DNI ya existe para esta compania", ENTITY_NAME, "dniexists");
        }
    }

    private void validateUniqueNoCia(CompaniaDTO companiaDTO) {
        if (companiaDTO.getNoCia() == null) {
            return;
        }
        boolean exists = companiaDTO.getId() == null
            ? companiaRepository.existsByNoCia(companiaDTO.getNoCia())
            : companiaRepository.existsByNoCiaAndIdNot(companiaDTO.getNoCia(), companiaDTO.getId());
        if (exists) {
            throw new BadRequestAlertException("El codigo de compania (noCia) ya esta en uso", ENTITY_NAME, "nociadup");
        }
    }

    private void validateUniqueNoCia(Compania compania) {
        if (compania.getNoCia() == null) {
            return;
        }
        boolean exists = compania.getId() == null
            ? companiaRepository.existsByNoCia(compania.getNoCia())
            : companiaRepository.existsByNoCiaAndIdNot(compania.getNoCia(), compania.getId());
        if (exists) {
            throw new BadRequestAlertException("El codigo de compania (noCia) ya esta en uso", ENTITY_NAME, "nociadup");
        }
    }
}
