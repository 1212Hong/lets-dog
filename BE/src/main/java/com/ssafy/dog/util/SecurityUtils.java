package com.ssafy.dog.util;

import com.ssafy.dog.security.SecurityUserDto;
import org.springframework.security.core.context.SecurityContextHolder;

/*
 *  Security Context의 인증 객체로부터 다양한 정보를 뽑아서 제공하는 클래스
 * */
public abstract class SecurityUtils {

    public static Long getUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SecurityUserDto) {
            return ((SecurityUserDto) principal).getUserId();
        } else {
            throw new IllegalArgumentException("======================SecurityUserDto 형변환 에러======================");
        }
//		return ((SecurityUserDto)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUserId();
    }

    public static String getUserLoginId() {
        return ((SecurityUserDto) (SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())).getUserLoginId();
    }

    public static SecurityUserDto getUser() {
        return (SecurityUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

