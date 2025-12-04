package com.restapp.repo;

import com.restapp.model.entity.RoleRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRequestRepository extends JpaRepository<RoleRequest, UUID> {
}
