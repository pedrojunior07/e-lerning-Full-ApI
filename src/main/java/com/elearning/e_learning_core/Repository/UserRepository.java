package com.elearning.e_learning_core.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearning.e_learning_core.model.Usr;

@Repository
public interface UserRepository extends JpaRepository<Usr, Long> {

    Optional<Usr> findByEmail(String email);

    boolean existsByEmail(String username);

}
