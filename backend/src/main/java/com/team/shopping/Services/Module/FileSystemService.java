package com.team.shopping.Services.Module;

import com.team.shopping.Domains.FileSystem;
import com.team.shopping.Repositories.FileSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileSystemService {
    private final FileSystemRepository fileSystemRepository;

    public void save(String imageFolderName, String valueKey) {
        FileSystem fileSystem = new FileSystem();
        fileSystem.setK(imageFolderName);
        fileSystem.setV(valueKey);
        fileSystemRepository.save(fileSystem);
    }

    public void updateFile(FileSystem fileSystem, String valueKey) {
        fileSystem.setV(valueKey);
        fileSystemRepository.save(fileSystem);
    }


    public FileSystem get(String key) {
        return fileSystemRepository.findByK(key).orElse(null);
    }

    public Optional<FileSystem> getOptional(String key) {
        return fileSystemRepository.findByK(key);
    }

    public void delete(FileSystem fileSystem) {
        fileSystemRepository.delete(fileSystem);
    }
}
