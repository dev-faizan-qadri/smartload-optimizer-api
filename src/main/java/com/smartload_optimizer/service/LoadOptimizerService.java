package com.smartload_optimizer.service;

import com.smartload_optimizer.domain.Order;
import com.smartload_optimizer.domain.Truck;
import com.smartload_optimizer.dto.OptimizeRequest;
import com.smartload_optimizer.dto.OptimizeResponse;
import org.hibernate.validator.constraints.Currency;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoadOptimizerService {
  private long bestPayout;
  private  int bestMask;
  public OptimizeResponse optimize(OptimizeRequest request){
    Truck truck = request.truck();
    List<Order> orders = filterCompatibleOrders(request.orders());

    int order_size = orders.size();
    if(order_size == 0 ){
      return emptyResult(truck);
    }
    long[] weights = new long[order_size];
    long[] volumes = new long[order_size];
    long[] payouts = new long[order_size];

    for(int i=0; i<order_size; i++){
      Order order = orders.get(i);
      weights[i] = order.weight_lbs();
      volumes[i] = order.volume_cuft();
      payouts[i] = order.payout_cents();
    }

    long[] suffixMax = new long[order_size + 1];
    for(int i=order_size -1; i>=0; i--){
      suffixMax[i] = suffixMax[i+1] +payouts[i];
    }

    bestPayout = 0;
    bestMask = 0;

    dfs(0, 0, 0, 0, 0,
            weights, volumes, payouts, truck.max_weight_lbs(), truck.max_volume_cuft(), suffixMax);

    return buildResponse(truck, orders);
  }

  private void dfs(int idx, int mask, long w, long v, long p,
                   long[] weights, long[] volumes, long[] payouts,
                   long maxW, long maxV, long[] suffixMax) {

    if (w > maxW || v > maxV) return;
    if (p + suffixMax[idx] <= bestPayout) return;

    if (idx == weights.length) {
      if (p > bestPayout) {
        bestPayout = p;
        bestMask = mask;
      }
      return;
    }

    dfs(idx + 1, mask, w, v, p,
            weights, volumes, payouts, maxW, maxV, suffixMax);

    dfs(idx + 1,
            mask | (1 << idx),
            w + weights[idx],
            v + volumes[idx],
            p + payouts[idx],
            weights, volumes, payouts, maxW, maxV, suffixMax);
  }
  private List<Order> filterCompatibleOrders(List<Order> orderList){
    if(orderList.isEmpty())
      return List.of();

    Order firstOrder = orderList.get(0);
    boolean isHazmat = firstOrder.is_hazmat();
    return orderList.stream()
            .filter(order -> order.origin().equals(firstOrder.origin()))
            .filter(order -> order.destination().equals(firstOrder.destination()))
            .filter(order -> order.is_hazmat() == isHazmat)
            .filter(order -> !order.pickup_date().isAfter(firstOrder.delivery_date()))
            .toList();
  }

  private OptimizeResponse emptyResult(Truck truck) {
    return new OptimizeResponse(
            truck.id(), List.of(),
            0, 0, 0, 0.0, 0.0
    );}
  private OptimizeResponse buildResponse(Truck truck, List<Order> orders) {
    List<String> ids = new ArrayList<>();
    long w = 0, v = 0;

    for (int i = 0; i < orders.size(); i++) {
      if ((bestMask & (1 << i)) != 0) {
        Order o = orders.get(i);
        ids.add(o.id());
        w += o.weight_lbs();
        v += o.volume_cuft();
      }
    }

    return new OptimizeResponse(
            truck.id(),
            ids,
            bestPayout,
            w,
            v,
            round2(w * 100.0 / truck.max_weight_lbs()),
            round2(v * 100.0 / truck.max_volume_cuft())
    );
  }

  private double round2(double v) {
    return Math.round(v * 100.0) / 100.0;
  }

 }
