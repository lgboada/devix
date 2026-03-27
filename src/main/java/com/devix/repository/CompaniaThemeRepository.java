package com.devix.repository;

import com.devix.domain.CompaniaTheme;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompaniaThemeRepository extends JpaRepository<CompaniaTheme, Long> {
    Optional<CompaniaTheme> findOneByNoCia(Long noCia);
}
