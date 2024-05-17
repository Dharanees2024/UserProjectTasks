package com.ums.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static com.ums.utils.Constants.ROUNDING_PRECISION;

public class HelperFunctions {
    public BigDecimal calculatePercentage(BigDecimal amount, BigDecimal percent) {
        return amount.multiply(percent).divide(new BigDecimal("100"), ROUNDING_PRECISION, RoundingMode.UP);
    }

    /** Function to generate random role code */
    public String generateRoleCode() {
        UUID uuid = UUID.randomUUID();
        String roleCode = "ROLE_" + uuid.toString().replace("-", "").substring(0, 4);
        return roleCode;
    }
}
