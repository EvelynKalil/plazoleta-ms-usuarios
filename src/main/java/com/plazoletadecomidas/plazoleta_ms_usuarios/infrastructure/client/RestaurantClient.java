package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "plazoleta-ms", url = "${plazoleta.client.url}")
public interface RestaurantClient {

    @GetMapping("/restaurants/validate-ownership")
    Boolean isOwnerOfRestaurant(@RequestParam("restaurantId") UUID restaurantId,
                                @RequestParam("ownerId") UUID ownerId);

    @PostMapping("/restaurants/{restaurantId}/employees")
    void addEmployeeToRestaurant(@PathVariable UUID restaurantId,
                                 @RequestBody AddEmployeeRequest body,
                                 @RequestHeader("Authorization") String bearer);

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class AddEmployeeRequest {
        private UUID employeeId;
    }
}
