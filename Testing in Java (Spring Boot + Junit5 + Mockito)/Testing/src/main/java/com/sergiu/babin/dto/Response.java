package com.sergiu.babin.dto;

import lombok.Builder;

@Builder
public record Response<T>(String message, Integer status, T payload) {
}
