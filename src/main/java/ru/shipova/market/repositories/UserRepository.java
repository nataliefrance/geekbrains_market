package ru.shipova.market.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shipova.market.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findOneByUserName (String username);
}
