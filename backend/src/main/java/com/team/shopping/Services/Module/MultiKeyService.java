package com.team.shopping.Services.Module;

import com.team.shopping.Domains.MultiKey;
import com.team.shopping.Repositories.MultiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MultiKeyService {
    private final MultiKeyRepository multiKeyRepository;

    public Optional<MultiKey> get(String key) {
        return multiKeyRepository.findByKey(key);
    }

    public MultiKey save(String key, String value) {
        List<String> valueList = new ArrayList<>();
        valueList.add(value);
        return multiKeyRepository.save(MultiKey.builder()
                .k(key)
                .vs(valueList)
                .build());
    }

    public void add(MultiKey multiKey, String value) {
        multiKey.getVs().add(value);
        multiKeyRepository.save(multiKey);
    }

    public void delete(MultiKey multiKey) {
        multiKeyRepository.delete(multiKey);
    }
}
