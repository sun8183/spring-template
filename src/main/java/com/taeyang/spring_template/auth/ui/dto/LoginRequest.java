package com.taeyang.spring_template.auth.ui.dto;

import com.taeyang.spring_template.common.validation.ValidId;
import com.taeyang.spring_template.common.validation.ValidPw;

public record LoginRequest (
    @ValidId
    String id,

    @ValidPw
    String pw

){};
