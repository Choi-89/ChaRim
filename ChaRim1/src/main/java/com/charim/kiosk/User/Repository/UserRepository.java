package com.charim.kiosk.User.Repository;

import com.charim.kiosk.User.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean findByName(String name);
}
