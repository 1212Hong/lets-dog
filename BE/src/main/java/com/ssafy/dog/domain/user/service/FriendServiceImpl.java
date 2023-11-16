package com.ssafy.dog.domain.user.service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.fcm.dto.FCMDto;
import com.ssafy.dog.domain.fcm.service.FirebaseService;
import com.ssafy.dog.domain.user.dto.request.FriendRequestReqDto;
import com.ssafy.dog.domain.user.dto.response.FriendReadRes;
import com.ssafy.dog.domain.user.dto.response.FriendRequestResDto;
import com.ssafy.dog.domain.user.dto.response.UserReadRes;
import com.ssafy.dog.domain.user.entity.FriendRequest;
import com.ssafy.dog.domain.user.entity.Friendship;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.model.FriendRequestStatus;
import com.ssafy.dog.domain.user.repository.FriendRequestRepository;
import com.ssafy.dog.domain.user.repository.FriendshipRepository;
import com.ssafy.dog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FriendServiceImpl implements FriendService { // 리팩토링 시 거절 당하면 다시 친추 가능하게 하기
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;
    private final FirebaseService firebaseService;

    @Transactional
    @Override
    public Api<FriendRequestResDto> sendFriendRequest(Long senderId, String receiverNickname) {
        // 1. 입력값으로부터 발신자와 수신자를 찾음
        User sender = userRepository.findByUserId(senderId)
                .orElseThrow(() -> new ApiException(UserErrorCode.SENDER_NOT_FOUND));

        User receiver = userRepository.findByUserNickname(receiverNickname)
                .orElseThrow(() -> new ApiException(UserErrorCode.RECEIVER_NOT_FOUND));

        log.info("senderId, receiverId = {}, {}", sender.getUserId(), receiver.getUserId());

        if (sender.equals(receiver)) { // 2. 발신자와 수신자가 동일한지 검사
            throw new ApiException(UserErrorCode.I_AM_MY_OWN_FRIEND);
        }

        // 3. 기존의 친구 요청이 있는지 검사
        Optional<FriendRequest> myFriendRequest = friendRequestRepository.findBySenderUserIdAndReceiverUserId(
                sender.getUserId(), receiver.getUserId());
        if (myFriendRequest.isPresent()) {
            // 3.1 상태에 따른 분기 처리
            FriendRequestStatus status = myFriendRequest.get().getStatus();
            switch (status) {
                case PENDING:
                    throw new ApiException(UserErrorCode.REQUEST_ALREADY_SENT);
                case ACCEPTED:
                    throw new ApiException(UserErrorCode.ALREADY_FRIENDS);
                case DECLINED:
                    throw new ApiException(UserErrorCode.PREVIOUSLY_DECLINED);
                default:
                    throw new ApiException(UserErrorCode.WRONG_STATUS_TYPE);
            }
        }

        // 4. 양방향 검사
        Optional<FriendRequest> theirFriendRequest = friendRequestRepository.findBySenderUserIdAndReceiverUserId(
                receiver.getUserId(), sender.getUserId());
        if (theirFriendRequest.isPresent()) {
            // 4.1 상태에 따른 분기 처리
            FriendRequestStatus status = myFriendRequest.get().getStatus();
            switch (status) {
                case PENDING:
                    throw new ApiException(UserErrorCode.THEIR_REQUEST_ALREADY_SENT);
                case ACCEPTED:
                    throw new ApiException(UserErrorCode.THEIR_ALREADY_FRIENDS);
                case DECLINED:
                    throw new ApiException(UserErrorCode.THEIR_PREVIOUSLY_DECLINED);
                default:
                    throw new ApiException(UserErrorCode.THEIR_WRONG_STATUS_TYPE);
            }
        }

        FriendRequestReqDto friendRequestReqDto = new FriendRequestReqDto();

        FriendRequest friendRequest = friendRequestReqDto.toEntity(sender, receiver, FriendRequestStatus.PENDING);

        // 5. Save FriendRequest entity
        friendRequestRepository.save(friendRequest);

        // 6. return
        FriendRequestResDto friendRequestResDto = new FriendRequestResDto(friendRequest.getSender().getUserNickname(),
                friendRequest.getReceiver().getUserNickname(), friendRequest.getStatus());

        // 친구 요청 알림 receiver에게
        firebaseService.sendNotification(
                FCMDto.createFriendRequest(receiver.getUserId(), sender.getUserNickname()));

        return Api.ok(friendRequestResDto);
    }

    @Transactional
    @Override
    public Api<FriendRequestResDto> declineFriendRequest(Long declinerId, String requesterNickname) {
        // 1. 입력값으로 거부자와 신청자 User 가져오기
        User decliner = userRepository.findByUserId(declinerId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
        User requester = userRepository.findByUserNickname(requesterNickname)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        // 2. 신청자가 보낸 친구 요청 찾기
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findBySenderUserIdAndReceiverUserId(
                requester.getUserId(), decliner.getUserId());

        if (!friendRequestOptional.isPresent()) {
            throw new ApiException(UserErrorCode.REQUEST_NOT_FOUND);
        }

        FriendRequest friendRequest = friendRequestOptional.get();

        // 3. 상태 확인 및 삭제
        if (friendRequest.getStatus() == FriendRequestStatus.PENDING) {
            friendRequestRepository.delete(friendRequest);
        } else {
            // 이미 수락되었거나 거절된 요청에 대해서는 처리할 수 없음
            throw new ApiException(UserErrorCode.CANNOT_DECLINE_PROCESSED_REQUEST);
        }

        // 4. 결과 반환
        FriendRequestResDto friendRequestResDto = new FriendRequestResDto(friendRequest.getSender().getUserNickname(),
                friendRequest.getReceiver().getUserNickname(), FriendRequestStatus.DECLINED);

        // 친구 거절 되었다는 알림 requester에게
        firebaseService.sendNotification(
                FCMDto.rejectFriendRequest(requester.getUserId(), decliner.getUserNickname()));

        return Api.ok(friendRequestResDto);
    }

    @Transactional
    @Override
    public Api<FriendRequestResDto> acceptFriendRequest(Long accepterId, String requesterNickname) {
        User accepter = userRepository.findByUserId(accepterId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        User requester = userRepository.findByUserNickname(requesterNickname)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findBySenderUserIdAndReceiverUserId(
                requester.getUserId(), accepter.getUserId());

        if (!friendRequestOptional.isPresent()) {
            throw new ApiException(UserErrorCode.REQUEST_NOT_FOUND);
        }

        FriendRequest friendRequest = friendRequestOptional.get();

        if (friendRequest.getStatus() != FriendRequestStatus.PENDING) {
            throw new ApiException(UserErrorCode.REQUEST_NOT_FOUND);
        }

        // Accept the friend request
        friendRequest.changeStatus(FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(friendRequest);

        // Add each other as friends in the friendship table
        Friendship accepterToRequesterFriendship = Friendship.builder().user(accepter).friend(requester).build();

        Friendship requesterToAccepterFriendship = Friendship.builder().user(requester).friend(accepter).build();

        friendshipRepository.save(accepterToRequesterFriendship);
        friendshipRepository.save(requesterToAccepterFriendship);

        FriendRequestResDto friendRequestResDto = new FriendRequestResDto(friendRequest.getSender().getUserNickname(),
                friendRequest.getReceiver().getUserNickname(), friendRequest.getStatus());

        // 친구 수락 되었다는 알림 requester에게
        firebaseService.sendNotification(
                FCMDto.acceptFriendRequest(requester.getUserId(), accepter.getUserNickname()));

        return Api.ok(friendRequestResDto);
    }

    @Transactional
    @Override
    public Api<Map<String, String>> unfriend(Long userId, String friendNickname) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        User friend = userRepository.findByUserNickname(friendNickname)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        // Attempt to find the friendships in both directions
        Optional<Friendship> userToFriendFriendship = friendshipRepository.findByUserAndFriend(user, friend);
        Optional<Friendship> friendToUserFriendship = friendshipRepository.findByUserAndFriend(friend, user);

        // If either friendship is not found, throw an exception
        if (!userToFriendFriendship.isPresent() || !friendToUserFriendship.isPresent()) {
            throw new ApiException(UserErrorCode.NOT_FRIENDS);
        }

        // Delete both friendships
        userToFriendFriendship.ifPresent(friendshipRepository::delete);
        friendToUserFriendship.ifPresent(friendshipRepository::delete);

        // Additionally, delete any associated FriendRequest in both directions
        friendRequestRepository.findBySenderUserIdAndReceiverUserId(userId, friend.getUserId())
                .ifPresent(friendRequestRepository::delete);
        friendRequestRepository.findBySenderUserIdAndReceiverUserId(friend.getUserId(), userId)
                .ifPresent(friendRequestRepository::delete);

        Map<String, String> ret = new HashMap<>();
        ret.put("userNickname", user.getUserNickname());
        ret.put("friendNickname", friend.getUserNickname());

        return Api.ok(
                ret);
    }

    @Transactional
    @Override
    public Api<List<UserReadRes>> getUsersSentFriendRequests(Long userId) {
        // Retrieve all friend requests sent by the user
        List<FriendRequest> sentRequests = friendRequestRepository.findAllBySenderUserId(userId);

        // Transform the list of FriendRequest entities into UserReadRes DTOs
        List<UserReadRes> sentRequestDto = sentRequests.stream()
                .filter(request -> request.getStatus() == FriendRequestStatus.PENDING)
                .map(request -> UserReadRes.builder()
                        .userId(request.getReceiver().getUserId())
                        .userLoginId(request.getReceiver().getUserLoginId())
                        .userNickname(request.getReceiver().getUserNickname())
                        .userPhone(request.getReceiver().getUserPhone())
                        .userPicture(request.getReceiver().getUserPicture())
                        .userAboutMe(request.getReceiver().getUserAboutMe())
                        .userGender(request.getReceiver().getUserGender())
                        .userLatitude(request.getReceiver().getUserLatitude())
                        .userLongitude(request.getReceiver().getUserLongitude())
                        .userAddress(request.getReceiver().getUserAddress())
                        .userIsRemoved(request.getReceiver().getUserIsRemoved())
                        .build())
                .collect(Collectors.toList());

        return Api.ok(sentRequestDto);
    }

    @Transactional
    @Override
    public Api<List<UserReadRes>> getUsersReceivedFriendRequests(Long userId) {
        // Retrieve all users who requested this user
        List<FriendRequest> receivedRequests = friendRequestRepository.findAllByReceiverUserId(userId);

        // Transform the list of FriendRequest entities into UserReadREs DTOs
        List<UserReadRes> receivedRequestDto = receivedRequests.stream()
                .filter(request -> request.getStatus() == FriendRequestStatus.PENDING)
                .map(request -> UserReadRes.builder()
                        .userId(request.getSender().getUserId())
                        .userLoginId(request.getSender().getUserLoginId())
                        .userNickname(request.getSender().getUserNickname())
                        .userPhone(request.getSender().getUserPhone())
                        .userPicture(request.getSender().getUserPicture())
                        .userAboutMe(request.getSender().getUserAboutMe())
                        .userGender(request.getSender().getUserGender())
                        .userLatitude(request.getSender().getUserLatitude())
                        .userLongitude(request.getSender().getUserLongitude())
                        .userAddress(request.getSender().getUserAddress())
                        .userIsRemoved(request.getReceiver().getUserIsRemoved())
                        .build())
                .collect(Collectors.toList());

        return Api.ok(receivedRequestDto);
    }

    @Transactional
    @Override
    public Api<List<FriendReadRes>> getFriendsList(Long userId) {
        List<Friendship> friendships = friendshipRepository.findAllByUserUserId(userId);

        List<FriendReadRes> friendsList = friendships.stream()
                .map(friendship -> {
                    User friend = friendship.getFriend();
                    return FriendReadRes.builder()
                            .userNickname(friend.getUserNickname())
                            .userPicture(friend.getUserPicture())
                            .build();
                })
                .collect(Collectors.toList());
        return Api.ok(friendsList);
    }
}
