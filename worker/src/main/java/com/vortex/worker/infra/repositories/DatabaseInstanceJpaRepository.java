package com.vortex.worker.infra.repositories;

import com.vortex.worker.infra.models.DatabaseInstanceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DatabaseInstanceJpaRepository extends JpaRepository<DatabaseInstanceModel, UUID> {
}