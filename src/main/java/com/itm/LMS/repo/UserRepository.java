package com.itm.LMS.repo;

import com.itm.LMS.exceptions.DatabaseException;
import com.itm.LMS.exceptions.UserNotFoundException;
import com.itm.LMS.model.QUser;
import com.itm.LMS.model.Role;
import com.itm.LMS.model.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory queryFactory;

    private final QUser qUser = QUser.user;

    // Save or Update
    public User save(User user) {
        try {
            return em.merge(user);
        } catch (PersistenceException ex) {
            throw new DatabaseException("Failed to save user: " + user.getUsername(), ex);
        }
    }

    // Delete
    public void delete(User user) {
        try {
            em.remove(em.contains(user) ? user : em.merge(user));
        } catch (PersistenceException ex) {
            throw new DatabaseException("Failed to delete user: " + user.getUsername(), ex);
        }
    }

    // Find by ID
    public User findById(Long id) {
        try {
            User user = queryFactory.selectFrom(qUser)
                    .where(qUser.id.eq(id))
                    .fetchOne();
            if (user == null) throw new UserNotFoundException(id);
            return user;
        } catch (PersistenceException ex) {
            throw new DatabaseException("Error fetching user by ID: " + id, ex);
        }
    }

    // Find by username
    public Optional<User> findByUsername(String username) {
        try {
            User user = queryFactory.selectFrom(qUser)
                    .where(qUser.username.eq(username))
                    .fetchOne();
            return Optional.ofNullable(user); // Wrap User into Optional
        } catch (PersistenceException ex) {
            throw new DatabaseException("Error fetching user by username: " + username, ex);
        }
    }

    public List<User> findByRoleIn(List<Role> roles) {
        try {
            return queryFactory.selectFrom(qUser)
                    .where(qUser.role.in(roles))
                    .fetch();
        } catch (PersistenceException ex) {
            throw new DatabaseException("Error fetching users by roles", ex);
        }
    }

    // Find all users
    public List<User> findAll() {
        try {
            return queryFactory.selectFrom(qUser).fetch();
        } catch (PersistenceException ex) {
            throw new DatabaseException("Error fetching all users", ex);
        }
    }

    //find all user with pagination
    public Page<User> findAllUsersPaginated(Pageable pageable) {
        try {
            // total count
            long total = queryFactory.selectFrom(qUser).fetchCount();

            // fetch paginated data
            List<User> users = queryFactory.selectFrom(qUser)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            return new PageImpl<>(users, pageable, total);

        } catch (PersistenceException ex) {
            throw new RuntimeException("Database error while fetching users", ex);
        }
    }

    // Find users by role
    public List<User> findByRole(Role role) {
        try {
            return queryFactory.selectFrom(qUser)
                    .where(qUser.role.eq(role))
                    .fetch();
        } catch (PersistenceException ex) {
            throw new DatabaseException("Error fetching users by role: " + role, ex);
        }
    }
}
