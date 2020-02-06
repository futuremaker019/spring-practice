package com.example.study.model.network.response;

import com.example.study.model.enumClass.OrderType;
import com.example.study.model.enumClass.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderGroupApiResponse {

    private Long id;

    private String status;

    private OrderType orderType;

    private String revAddress;

    private String revName;

    private PaymentType paymentType;

    private BigDecimal totalPrice;

    private Integer totalQuantity;

    private LocalDateTime orderAt;

    private LocalDateTime arrivalDate;

    private Long userId;
}
