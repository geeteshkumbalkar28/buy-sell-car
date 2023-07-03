package com.spring.jwt.controller;

import com.spring.jwt.dto.ChangePasswordDto;
import com.spring.jwt.dto.DealerDto;
import com.spring.jwt.dto.RegisterDto;
import com.spring.jwt.service.DealerService;
import com.spring.jwt.utils.BaseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dealer")
@RequiredArgsConstructor
public class DealerController {
    private final DealerService dealerService;

    @PutMapping("/updateDealer/{userId}")
    public ResponseEntity<BaseResponseDTO> updateDealer(@PathVariable("userId") Integer userId, @RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(dealerService.updateDealer(userId, registerDto));
    }

    @GetMapping("/allDealers")
    public ResponseEntity<List<DealerDto>> getAllDealers() {
        List<DealerDto> dealers = dealerService.getAllDealers();
        return ResponseEntity.ok(dealers);
    }

    @GetMapping("/{dealerId}")
    public ResponseEntity<DealerDto> getDealerById(@PathVariable("dealerId") Integer dealerId) {
        DealerDto dealer = dealerService.getDealerById(dealerId);
        if (dealer != null) {
            return ResponseEntity.ok(dealer);
        } else {
            return ResponseEntity.notFound().build();
        }

    }
    @DeleteMapping("/delete/{dealerId}")
    public ResponseEntity<BaseResponseDTO> deleteDealer(@PathVariable("dealerId") Integer dealerId) {
        return ResponseEntity.ok(dealerService.deleteDealer(dealerId));
    }

    @PutMapping("/changePassword/{userId}")
    public ResponseEntity<BaseResponseDTO> changePassword(
            @PathVariable("userId") Integer userId,
            @RequestBody ChangePasswordDto changePasswordDto
    ) {
        return ResponseEntity.ok(dealerService.changePassword(userId, changePasswordDto));
    }
}
