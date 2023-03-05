package com.devsuperior.bds02.repositories;

import com.devsuperior.bds02.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
