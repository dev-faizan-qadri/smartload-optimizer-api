package com.smartload_optimizer.dto;

import java.util.List;

public record OptimizeResponse (
        String truck_id,
        List<String> selected_order_ids,
        long total_payout_cents,
        long total_weight_lbs,
        long total_volume_cuft,
        double utilization_weight_percent,
        double utilization_Volume_percent

){ }
