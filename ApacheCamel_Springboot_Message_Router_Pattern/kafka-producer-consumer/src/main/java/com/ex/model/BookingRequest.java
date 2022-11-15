package com.ex.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookingRequest {
    private String origin;
    private String destination;
    private String date;
    private String shipper;
    private String consignee;
}
