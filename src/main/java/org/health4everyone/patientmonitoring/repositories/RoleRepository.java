package org.health4everyone.patientmonitoring.repositories;

import java.util.Optional;

import org.health4everyone.patientmonitoring.models.ERole;
import org.health4everyone.patientmonitoring.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findByName(ERole name);
}