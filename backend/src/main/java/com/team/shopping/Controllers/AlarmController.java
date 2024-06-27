package com.team.shopping.Controllers;

import com.team.shopping.DTOs.AlarmRequestDTO;
import com.team.shopping.DTOs.AlarmResponseDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarm")
public class AlarmController {
    private final MultiService multiService;

    //    @PostMapping
//    public ResponseEntity<?> post(@RequestHeader("Authorization") String accessToken) {
//        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
//        if (tokenRecord.isOK()) {
//            String username = tokenRecord.username();
//            return tokenRecord.getResponseEntity(addressResponseDTOList);
//        }
//        return tokenRecord.getResponseEntity();
//    }
    @MessageMapping("/alarm/{username}")
    @SendTo("/api/sub/alarm/{username}")
    public AlarmResponseDTO AlarmMessage(@DestinationVariable("username") String username, AlarmRequestDTO requestDTO) {
        return multiService.postAlarm(username, requestDTO);
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestHeader("Authorization") String accessToken) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            List<AlarmResponseDTO> alarmResponses = this.multiService.getAlarmList(username);
            return tokenRecord.getResponseEntity(alarmResponses);
        }
        return tokenRecord.getResponseEntity();
    }

}
