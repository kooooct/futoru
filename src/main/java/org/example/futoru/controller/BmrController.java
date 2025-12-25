package org.example.futoru.controller;

import org.example.futoru.dto.BmrRequest;
import org.example.futoru.dto.BmrResponse;
import org.example.futoru.service.BmrService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/simulations")
@RequiredArgsConstructor
public class BmrController {
    private final BmrService bmrService;

    @PostMapping("/calculate")
    public BmrResponse calculate(@RequestBody BmrRequest bmrRequest) {
        return bmrService.calculate(bmrRequest);
    }
}
