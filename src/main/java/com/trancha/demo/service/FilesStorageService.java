package com.trancha.demo.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
  public void init();

  public void save(MultipartFile file, Path dir);

  public Resource load(String filename, Path dir);
  
  public boolean delete(String filename,  Path dir);

  public void deleteAll( Path dir);

  public Stream<Path> loadAll( Path dir);
}
