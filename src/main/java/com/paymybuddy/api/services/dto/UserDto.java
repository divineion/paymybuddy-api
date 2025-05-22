package com.paymybuddy.api.services.dto;

import java.math.BigDecimal;

public record UserDto(int id, String username, String email, BigDecimal balance) {}
