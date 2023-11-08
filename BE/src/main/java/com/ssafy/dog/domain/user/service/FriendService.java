package com.ssafy.dog.domain.user.service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.user.dto.response.FriendRequestResDto;
import com.ssafy.dog.domain.user.dto.response.UserReadRes;

import java.util.List;

public interface FriendService {
    Api<FriendRequestResDto> sendFriendRequest(Long senderId, String receiverNickname);

    Api<FriendRequestResDto> declineFriendRequest(Long declinerId, String requesterNickname);

    Api<FriendRequestResDto> acceptFriendRequest(Long accepterId, String requesterNickname);

    Api<String> unfriend(Long userId, String friendNickname);

    Api<List<UserReadRes>> getUsersSentFriendRequests(Long userId);

    Api<List<UserReadRes>> getUsersReceivedFriendRequests(Long userId);

    Api<List<UserReadRes>> getFriendsList(Long userId);
}
