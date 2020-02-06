package com.example.study.model.enumClass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentType {

    CASH(0, "현금", "현금 결제"),
    CARD(1, "카드", "카드 결제");

    private Integer id;
    private String title;
    private String description;
}
