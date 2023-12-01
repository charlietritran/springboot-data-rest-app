package com.trancha.demo.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.trancha.demo.message.ResponseMessage;
import com.trancha.demo.model.FileInfo;
import com.trancha.demo.service.FilesStorageService;

@Controller
//@CrossOrigin("http://localhost:8849")
@RequestMapping("/api")
public class FilesController {

	@Autowired
	FilesStorageService storageService;

	// @CrossOrigin("http://localhost:8449")
	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			storageService.save(file, null);

			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	// @CrossOrigin("http://localhost:8449")
	@GetMapping("/docs/{id}")
	public ResponseEntity<List<FileInfo>> getDocs(@PathVariable("id") long id) {
		Path root = Paths.get("uploads/" + id);
		List<FileInfo> fileInfos = storageService.loadAll(root).map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(FilesController.class, "getDoc", path.getFileName().toString(), id).build()
					.toString();

			return new FileInfo(filename, url);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	// @CrossOrigin("http://localhost:8449")
	@GetMapping("/docs/{filename:.+}/{id:.+}")
	public ResponseEntity<Resource> getDoc(@PathVariable String filename, long id) {
		Path root = Paths.get("uploads/" + id);
		Resource file = storageService.load(filename, root);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@CrossOrigin("http://localhost:8449")
	@GetMapping("/files")
	public ResponseEntity<List<FileInfo>> getListFiles() {
		List<FileInfo> fileInfos = storageService.loadAll(null).map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

			return new FileInfo(filename, url);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	// @CrossOrigin("http://localhost:8449")
	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = storageService.load(filename, null);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	// @CrossOrigin("http://localhost:8449")
	@DeleteMapping("/files/{filename:.+}")
	public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String filename) {
		String message = "";

		try {
			boolean existed = storageService.delete(filename, null);

			if (existed) {
				message = "Delete the file successfully: " + filename;
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			}

			message = "The file does not exist!";
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not delete the file: " + filename + ". Error: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
		}
	}

	/*
	 * 
	 * @Override public boolean delete(String filename, Path dir) { try {
	 * 
	 * dir = dir != null? dir : root; Path file = dir.resolve(filename); return
	 * Files.deleteIfExists(file); } catch (IOException e) { throw new
	 * RuntimeException("Error: " + e.getMessage()); } }
	 * 
	 * @Override public void deleteAll(Path dir) {
	 * 
	 * dir = dir != null? dir : root;
	 * FileSystemUtils.deleteRecursively(dir.toFile()); }
	 * 
	 * @CrossOrigin("http://localhost:8449")
	 * 
	 * @GetMapping("/documents/{id}") public ResponseEntity<List<FileInfo>>
	 * getDocumentsById() { List<FileInfo> fileInfos =
	 * storageService.loadDocumentsById().map(path -> { String filename =
	 * path.getFileName().toString(); String url = MvcUriComponentsBuilder
	 * .fromMethodName(FilesController.class, "getFile",
	 * path.getFileName().toString()).build().toString();
	 * 
	 * return new FileInfo(filename, url); }).collect(Collectors.toList());
	 * 
	 * return ResponseEntity.status(HttpStatus.OK).body(fileInfos); }
	 * 
	 * 
	 * @CrossOrigin("http://localhost:8449")
	 * 
	 * @GetMapping("/documents/{filename:.+}") public ResponseEntity<Resource>
	 * getDocument(@PathVariable String filename) { Resource file =
	 * storageService.load(filename); return ResponseEntity.ok()
	 * .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
	 * file.getFilename() + "\"").body(file); }
	 */
}
