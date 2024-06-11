package com.team.shopping.Services.Module;

import com.team.shopping.Domains.FileSystem;
import com.team.shopping.Enums.ImageKey;
import com.team.shopping.Repositories.FileSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public String get(String username) {
        FileSystem fileSystem = fileSystemRepository.findByK(ImageKey.Temp.getKey(username));
        return fileSystem != null ? fileSystem.getV() : null;
    }

    public void delete(String username) {
        FileSystem fileSystem = fileSystemRepository.findByK(ImageKey.Temp.getKey(username));
        fileSystemRepository.delete(fileSystem);
    }
}
