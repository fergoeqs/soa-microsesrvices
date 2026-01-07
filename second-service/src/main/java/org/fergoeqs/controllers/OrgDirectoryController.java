package org.fergoeqs.controllers;

import lombok.RequiredArgsConstructor;
import org.fergoeqs.service.SoapClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orgdirectory")
@RequiredArgsConstructor
public class OrgDirectoryController {

    private final SoapClientService soapClientService;

    @PostMapping("/filter/turnover")
    public ResponseEntity<Object> filterByTurnover(@RequestBody Map<String, Object> body) {
        Integer min = null;
        if (body.containsKey("minAnnualTurnover")) {
            Object minObj = body.get("minAnnualTurnover");
            if (minObj instanceof Number) {
                min = ((Number) minObj).intValue();
            } else if (minObj instanceof String) {
                min = Integer.parseInt((String) minObj);
            }
        }
        
        Integer max = null;
        if (body.containsKey("maxAnnualTurnover")) {
            Object maxObj = body.get("maxAnnualTurnover");
            if (maxObj instanceof Number) {
                max = ((Number) maxObj).intValue();
            } else if (maxObj instanceof String) {
                max = Integer.parseInt((String) maxObj);
            }
        }
        
        if (min == null || max == null || min > max) {
            return ResponseEntity.badRequest().body("Invalid turnover range");
        }

        Integer page = 0;
        if (body.containsKey("page")) {
            Object pageObj = body.get("page");
            if (pageObj instanceof Number) {
                page = ((Number) pageObj).intValue();
            } else if (pageObj instanceof String) {
                page = Integer.parseInt((String) pageObj);
            }
        }

        Integer size = 20;
        if (body.containsKey("size")) {
            Object sizeObj = body.get("size");
            if (sizeObj instanceof Number) {
                size = ((Number) sizeObj).intValue();
            } else if (sizeObj instanceof String) {
                size = Integer.parseInt((String) sizeObj);
            }
        }

        Object result = soapClientService.filterByTurnover(
                min,
                max,
                (java.util.List<Map<String, Object>>) body.getOrDefault("filters", java.util.List.of()),
                (java.util.List<Map<String, Object>>) body.getOrDefault("sort", java.util.List.of()),
                page,
                size
        );

        return ResponseEntity.ok(result);
    }

    @PostMapping("/order")
    public ResponseEntity<Object> orderOrganizations(@RequestBody Map<String, Object> body) {
        if (!body.containsKey("sort")) {
            return ResponseEntity.badRequest().body("Missing sort criteria");
        }

        Integer page = 0;
        if (body.containsKey("page")) {
            Object pageObj = body.get("page");
            if (pageObj instanceof Number) {
                page = ((Number) pageObj).intValue();
            } else if (pageObj instanceof String) {
                page = Integer.parseInt((String) pageObj);
            }
        }

        Integer size = 20;
        if (body.containsKey("size")) {
            Object sizeObj = body.get("size");
            if (sizeObj instanceof Number) {
                size = ((Number) sizeObj).intValue();
            } else if (sizeObj instanceof String) {
                size = Integer.parseInt((String) sizeObj);
            }
        }

        Object result = soapClientService.orderOrganizations(
                (java.util.List<Map<String, Object>>) body.get("sort"),
                (java.util.List<Map<String, Object>>) body.getOrDefault("filters", java.util.List.of()),
                page,
                size
        );

        return ResponseEntity.ok(result);
    }
}
