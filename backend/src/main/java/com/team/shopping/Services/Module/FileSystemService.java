package com.team.shopping.Services.Module;

import com.team.shopping.Domains.FileSystem;
import com.team.shopping.Repositories.FileSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileSystemService {
    private final FileSystemRepository fileSystemRepository;

    public void upload(String imageFolderName, String valueKey) {
        FileSystem fileSystem = new FileSystem();
        fileSystem.setK(valueKey);
        fileSystem.setV(imageFolderName);
        fileSystemRepository.save(fileSystem);
    }
}
