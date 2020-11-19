package com.brc.als.repository;

import com.brc.als.domain.AlertActivity;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AlertActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlertActivityRepository extends JpaRepository<AlertActivity, Long> {
}
