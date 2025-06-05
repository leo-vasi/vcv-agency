package com.leo.vcv.repository;

import com.leo.vcv.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByEmailAndIdNot(String email, Long id);
}