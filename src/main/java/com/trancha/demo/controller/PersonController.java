package com.trancha.demo.controller;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.trancha.demo.model.Person;
import com.trancha.demo.model.PersonFormWrapper;
import com.trancha.demo.repository.PersonRepository;
import com.trancha.demo.service.FilesStorageService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class PersonController {

	@Autowired
	PersonRepository personRepository;

	@Autowired
	FilesStorageService storageService;

	/**
	 * GET ALL PEOPLE
	 * 
	 * @param birthdate
	 * @return
	 */
	@CrossOrigin(origins = "http://localhost:8449")
	@GetMapping("/people")
	public ResponseEntity<List<Person>> getAllPersons(@RequestParam(required = false) String birthdate) {
		try {
			List<Person> persons = new ArrayList<Person>();
			if (birthdate == null)
				personRepository.findAll().forEach(persons::add);
			else
				personRepository.findByBirthdateContaining(birthdate).forEach(persons::add);
			if (persons.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(persons, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * GET PERSON BY ID
	 * 
	 * @param id
	 * @return
	 */
	@CrossOrigin(origins = "http://localhost:8449")
	@GetMapping("/person/{id}")
	public ResponseEntity<Person> getPersonById(@PathVariable("id") long id) {
		Optional<Person> personData = personRepository.findById(id);
		if (personData.isPresent()) {
			return new ResponseEntity<>(personData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * CREATE PERSON
	 * 
	 * @param person
	 * @return
	 */
	@CrossOrigin(origins = "http://localhost:8449")
	@PostMapping("/person")
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
		try {
			Person _person = personRepository.save(new Person(person.getFirstname(), person.getLastname(),
					person.getBirthdate(), person.getGender(), person.getDocuments()));
			return new ResponseEntity<>(_person, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * CREATE PERSON WITH MULTI FORM DATA
	 * 
	 * @param model
	 * @return
	 */
	@CrossOrigin(origins = "http://localhost:8449")
	@PostMapping("/person/multi/model")
	public ResponseEntity<Person> createPersonMultiModel(@ModelAttribute PersonFormWrapper model) {
		try {

			// save person first
			Person _person = personRepository.save(new Person(model.getFirstname(), model.getLastname(),
					model.getBirthdate(), model.getGender(), model.getDocuments()[0].getName()));

			// save files and update person with fileInfos
			if (_person != null) {
				List<String> fileInfos = processUploadedFile(model.getDocuments(), _person.getId());
				_person.setDocuments(StringUtils.join(fileInfos, ","));
				_person = personRepository.save(_person);
			}

			return new ResponseEntity<>(_person, HttpStatus.CREATED);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * HELPER: SAVE UPLOADED FILES
	 * 
	 * @param files
	 * @param id
	 * @return list of fileInfos: name & url
	 * @throws IOException
	 */
	private List<String> processUploadedFile(MultipartFile[] files, long id) throws IOException {

		Path root = Paths.get("uploads/" + id);
		Files.createDirectories(root);

		// Save all files to dir
		try {
			for (MultipartFile file : files) {
				Path path = root.resolve(file.getOriginalFilename());
				Files.copy(file.getInputStream(), path);
			}
		} catch (Exception e) {
			if (e instanceof FileAlreadyExistsException) {
				throw new RuntimeException("A file of that name already exists.");
			}

			throw new RuntimeException(e.getMessage());
		}

		// build fileInfos for return
		List<String> fileInfos = storageService.loadAll(root).map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(FilesController.class, "getDoc", path.getFileName().toString(), id).build()
					.toString();

			return filename + "|" + url;
		}).collect(Collectors.toList());

		return fileInfos;
	}

	/*
	 * 
	 * @PostMapping("/api/upload/multi/model") public ResponseEntity<?>
	 * multiUploadFileModel(@ModelAttribute FormWrapper model) { try { // Save as
	 * you want as per requiremens saveUploadedFile(model.getImage());
	 * formRepo.save(mode.getTitle(), model.getDescription()); } catch (IOException
	 * e) { return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }
	 * 
	 * return new ResponseEntity("Successfully uploaded!", HttpStatus.OK); }
	 * 
	 * private void saveUploadedFile(MultipartFile file) throws IOException { if
	 * (!file.isEmpty()) { byte[] bytes = file.getBytes(); Path path =
	 * Paths.get(UPLOADED_FOLDER + file.getOriginalFilename()); Files.write(path,
	 * bytes); } }
	 */

	/**
	 * UPDATE PERSON
	 * 
	 * @param id
	 * @param person
	 * @return
	 */
	@CrossOrigin(origins = "http://localhost:8449")
	@PutMapping("/person/{id}")
	public ResponseEntity<Person> updatePerson(@PathVariable("id") long id, @RequestBody Person person) {
		Optional<Person> personData = personRepository.findById(id);
		if (personData.isPresent()) {
			Person _person = personData.get();
			_person.setFirstname(person.getFirstname());
			_person.setLastname(person.getLastname());
			_person.setBirthdate(person.getBirthdate());
			_person.setGender(person.getGender());
			_person.setDocuments(person.getDocuments());
			return new ResponseEntity<>(personRepository.save(_person), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * DELETE PERSON
	 * 
	 * @param id
	 * @return
	 */
	@CrossOrigin(origins = "http://localhost:8449")
	@DeleteMapping("/person/{id}")
	public ResponseEntity<HttpStatus> deletePerson(@PathVariable("id") long id) {
		try {
			// delete record
			personRepository.deleteById(id);

			// delete docs
			Path root = Paths.get("uploads/" + id);
			storageService.deleteAll(root);

			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * DELETE ALL PERSONS
	 * 
	 * @return
	 */
	@CrossOrigin(origins = "http://localhost:8449")
	@DeleteMapping("/people")
	public ResponseEntity<HttpStatus> deleteAllPersons() {
		try {
			personRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * FIND BY FIRST NAME
	 * 
	 * @param firstname
	 * @return
	 */
	@CrossOrigin(origins = "http://localhost:8449")
	@GetMapping("/person/firstname")
	public ResponseEntity<List<Person>> findByFirstname(@RequestParam(required = false) String firstname) {
		try {
			List<Person> persons = personRepository.findByFirstname(firstname);
			if (persons.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(persons, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
