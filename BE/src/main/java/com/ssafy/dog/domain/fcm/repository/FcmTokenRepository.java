package com.ssafy.dog.domain.fcm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.dog.domain.fcm.entity.FcmToken;

@Repository
public interface FcmTokenRepository extends CrudRepository<FcmToken, Long> {

}
