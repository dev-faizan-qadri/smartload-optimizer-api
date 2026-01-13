package com.smartload_optimizer.domain;


import java.time.LocalDate;

public record Order(
        String id,
        String origin,
        String destination,
        LocalDate pickup_date,
        LocalDate delivery_date,
        long payout_cents,
        long weight_lbs,
        long volume_cuft,
        boolean is_hazmat

){ }
