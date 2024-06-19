package com.team.shopping.Services.Module;

import com.team.shopping.Repositories.EventProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventProductService {

    private final EventProductRepository eventProductRepository;
}
