package com.team.shopping.Services.Module;

import com.team.shopping.Domains.Alarm;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public Alarm save(SiteUser target, String message, String sender, String url, boolean isRead) {
        return alarmRepository.save(Alarm.builder().target(target).message(message).sender(sender).url(url).isRead(isRead).build());
    }

    public List<Alarm> getList(String username) {
        return alarmRepository.getList(username);
    }

    public Optional<Alarm> get(Long alarm_id) {
        return alarmRepository.findById(alarm_id);
    }

    public void delete(Alarm alarm) {
        alarmRepository.delete(alarm);
    }
}
