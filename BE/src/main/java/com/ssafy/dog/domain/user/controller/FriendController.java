package com.ssafy.dog.domain.user.controller;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.user.dto.response.FriendRequestResDto;
import com.ssafy.dog.domain.user.dto.response.FriendUnfriendRes;
import com.ssafy.dog.domain.user.dto.response.UserReadRes;
import com.ssafy.dog.domain.user.service.FriendService;
import com.ssafy.dog.domain.user.service.UserService;
import com.ssafy.dog.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/friend")
@RequiredArgsConstructor
@RestController
@Slf4j
public class FriendController {
    private final FriendService friendService;
    private final UserService userService;

    @PostMapping("/request/{receiverNickname}")
    @Operation(summary = "친구 신청")
    public Api<FriendRequestResDto> sendFriendRequest(@PathVariable String receiverNickname) {

        return friendService.sendFriendRequest(SecurityUtils.getUserId(), receiverNickname);
    }

    @PostMapping("/decline/{requesterNickname}")
    @Operation(summary = "친구 신청 거절")
    public Api<FriendUnfriendRes> declineFriendRequest(@PathVariable String requesterNickname) {

        return friendService.declineFriendRequest(SecurityUtils.getUserId(), requesterNickname);
    }

    @PutMapping("/accept/{requesterNickname}")
    @Operation(summary = "친구 신청 승낙")
    public Api<FriendRequestResDto> acceptFriendRequest(@PathVariable String requesterNickname) {

        return friendService.acceptFriendRequest(SecurityUtils.getUserId(), requesterNickname);
    }

    @DeleteMapping("/unfriend/{friendNickname}")
    @Operation(summary = "친구 끊기")
    public Api<Map<String, String>> unfriend(@PathVariable String friendNickname) {
        return friendService.unfriend(SecurityUtils.getUserId(), friendNickname);
    }

    @GetMapping("/request/sent")
    @Operation(summary = "내가 친구 신청한 유저 목록 가져오기")
    public Api<List<UserReadRes>> getSentFriendRequests() { // 리팩토링 시 함수 명 변경 (UserReadRes 가져오는 것 표시)
        return friendService.getUsersSentFriendRequests(SecurityUtils.getUserId());
    }

    @GetMapping("/request/received")
    @Operation(summary = "나한테 친구 신청한 유저 목록 가져오기")
    public Api<List<UserReadRes>> getReceivedFriendRequests() { // 리팩토링 시 함수 명 변경 (UserReadRes 가져오는 것 표시)
        return friendService.getUsersReceivedFriendRequests(SecurityUtils.getUserId());
    }

    @GetMapping("/friend-list")
    @Operation(summary = "친구 목록 불러오기")
    public Api<List<UserReadRes>> getFriendsList() {
        return friendService.getFriendsList(SecurityUtils.getUserId());
    }
}
