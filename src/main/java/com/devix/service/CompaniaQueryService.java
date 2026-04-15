package com.devix.service;

import com.devix.domain.*; // for static metamodels
import com.devix.domain.Compania;
import com.devix.repository.CompaniaRepository;
import com.devix.repository.UsuarioCentroRepository;
import com.devix.security.AuthoritiesConstants;
import com.devix.security.SecurityUtils;
import com.devix.service.criteria.CompaniaCriteria;
import com.devix.service.dto.CompaniaDTO;
import com.devix.service.mapper.CompaniaMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Compania} entities in the database.
 * The main input is a {@link CompaniaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CompaniaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompaniaQueryService extends QueryService<Compania> {

    private static final Logger LOG = LoggerFactory.getLogger(CompaniaQueryService.class);

    private final CompaniaRepository companiaRepository;

    private final CompaniaMapper companiaMapper;

    private final UsuarioCentroRepository usuarioCentroRepository;

    public CompaniaQueryService(
        CompaniaRepository companiaRepository,
        CompaniaMapper companiaMapper,
        UsuarioCentroRepository usuarioCentroRepository
    ) {
        this.companiaRepository = companiaRepository;
        this.companiaMapper = companiaMapper;
        this.usuarioCentroRepository = usuarioCentroRepository;
    }

    /**
     * Return a {@link List} of {@link CompaniaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CompaniaDTO> findByCriteria(CompaniaCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Compania> specification = createSpecification(criteria);
        return companiaMapper.toDto(companiaRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompaniaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Compania> specification = createSpecification(criteria);
        return companiaRepository.count(specification);
    }

    /**
     * Function to convert {@link CompaniaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Compania> createSpecification(CompaniaCriteria criteria) {
        Specification<Compania> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Compania_.id),
                buildRangeSpecification(criteria.getNoCia(), Compania_.noCia),
                buildStringSpecification(criteria.getDni(), Compania_.dni),
                buildStringSpecification(criteria.getNombre(), Compania_.nombre),
                buildStringSpecification(criteria.getDireccion(), Compania_.direccion),
                buildStringSpecification(criteria.getEmail(), Compania_.email),
                buildStringSpecification(criteria.getTelefono(), Compania_.telefono),
                buildStringSpecification(criteria.getPathImage(), Compania_.pathImage),
                buildSpecification(criteria.getActiva(), Compania_.activa),
                buildSpecification(criteria.getCentrosId(), root -> root.join(Compania_.centros, JoinType.LEFT).get(Centro_.id))
            );
        }
        return Specification.allOf(specification, restrictToCurrentUserCompanies());
    }

    /**
     * En el listado de compañías (/api/companias), se deben devolver únicamente las compañías a las que
     * el usuario autenticado tiene permiso vía UsuarioCentro (noCia efectivo).
     *
     * Admin queda sin restricción para poder administrar compañías.
     */
    private Specification<Compania> restrictToCurrentUserCompanies() {
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            return null;
        }

        Optional<String> loginOpt = SecurityUtils.getCurrentUserLogin();
        if (loginOpt.isEmpty()) {
            // Si no hay usuario, no debe listar compañías
            return (root, query, cb) -> cb.disjunction();
        }

        String login = loginOpt.get();
        List<Long> allowedNoCias = usuarioCentroRepository
            .findDistinctAccountCompaniesByUserLogin(login)
            .stream()
            .map(p -> p.getEffectiveNoCia())
            .filter(java.util.Objects::nonNull)
            .distinct()
            .toList();

        if (allowedNoCias.isEmpty()) {
            return (root, query, cb) -> cb.disjunction();
        }

        return (root, query, cb) -> root.get(Compania_.noCia).in(allowedNoCias);
    }
}
