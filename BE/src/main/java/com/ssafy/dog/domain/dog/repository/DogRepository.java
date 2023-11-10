package com.ssafy.dog.domain.dog.repository;

import com.ssafy.dog.domain.dog.entity.Dog;
import com.ssafy.dog.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
    List<Dog> findAllByUser(User user);
}
