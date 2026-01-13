package com.smartload_optimizer.api;

import com.smartload_optimizer.dto.OptimizeRequest;
import com.smartload_optimizer.dto.OptimizeResponse;
import com.smartload_optimizer.service.LoadOptimizerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/load-optimizer")
public class LoadOptimizerController {
  private final LoadOptimizerService loadOptimizerService;

  public LoadOptimizerController(LoadOptimizerService loadOptimizerService) {
    this.loadOptimizerService = loadOptimizerService;
  }
  @PostMapping("/optimize")
  public ResponseEntity<OptimizeResponse> optimize(@Valid @RequestBody OptimizeRequest request){
    return ResponseEntity.ok(loadOptimizerService.optimize(request));
  }
}
