package com.irembo.useraccountmanagement.repository;

import com.irembo.useraccountmanagement.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by USER on 5/6/2023.
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
}
