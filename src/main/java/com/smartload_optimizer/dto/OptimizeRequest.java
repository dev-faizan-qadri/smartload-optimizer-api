package com.smartload_optimizer.dto;

import com.smartload_optimizer.domain.Order;
import com.smartload_optimizer.domain.Truck;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OptimizeRequest(
        @NotNull Truck truck,
        @NotEmpty List<Order> orders
        ){ }
