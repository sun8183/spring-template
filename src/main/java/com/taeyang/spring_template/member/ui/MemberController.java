package com.taeyang.spring_template.member.ui;

import com.taeyang.spring_template.common.response.ApiResponse;
import com.taeyang.spring_template.common.response.enums.SuccessCode;
import com.taeyang.spring_template.member.application.MemberService;
import com.taeyang.spring_template.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("")
    public ApiResponse<Member> getMember(){
        Member member = null;
        return ApiResponse.success(SuccessCode.OK, member);
    }
}
