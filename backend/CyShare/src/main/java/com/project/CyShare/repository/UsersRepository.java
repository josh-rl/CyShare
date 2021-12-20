package com.project.CyShare.repository;

import com.project.CyShare.app.Role;
import com.project.CyShare.app.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Hugo Alvarez Valdivia
 */
public interface UsersRepository extends CrudRepository<User, Long>
{
    /**
     * Find user by the id number.
     * @param id User's id number.
     * @return User object.
     */
    User findById(long id);

    User findByUserName(String userName);

    /**
     * Delete user by id.
     * @param id User's id number.
     */
    @Transactional
    void deleteById(long id);
}
