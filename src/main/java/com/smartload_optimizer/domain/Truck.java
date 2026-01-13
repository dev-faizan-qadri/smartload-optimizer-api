package com.smartload_optimizer.domain;

public record Truck(
        String id,
        long max_weight_lbs,
        long max_volume_cuft
){};
