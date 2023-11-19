package com.trancha.demo.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

	private final Path root = Paths.get("uploads");

	@Override
	public void init() {
		try {
			Files.createDirectories(root);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	@Override
	public void save(MultipartFile file, Path dir) {
		try {

			dir = dir != null ? dir : root;
			Files.copy(file.getInputStream(), dir.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			if (e instanceof FileAlreadyExistsException) {
				throw new RuntimeException("A file of that name already exists.");
			}

			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public Resource load(String filename, Path dir) {
		try {

			dir = dir != null ? dir : root;
			Path file = dir.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public boolean delete(String filename, Path dir) {
		try {

			dir = dir != null ? dir : root;
			Path file = dir.resolve(filename);
			return Files.deleteIfExists(file);
		} catch (IOException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public void deleteAll(Path dir) {

		dir = dir != null ? dir : root;
		FileSystemUtils.deleteRecursively(dir.toFile());
	}

	@Override
	public Stream<Path> loadAll(Path dir) {
		try {

			final Path walkerDir = dir != null ? dir : root;
			return Files.walk(walkerDir, 1).filter(path -> !path.equals(walkerDir)).map(walkerDir::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}

}
