package com.ddr.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ddr.adapter.Orders;
import com.ddr.exception.ResourceNotFoundException;
import com.ddr.model.Tutorial;
import com.ddr.payload.response.Response;
import com.ddr.repository.TutorialRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TutorialController {
	@Autowired
	TutorialRepository tutorialRepository;

	@GetMapping("/tutorials")
	public ResponseEntity<Map<String, Object>> getAllTutorials(@RequestParam(required = false) String title,
			@RequestParam(required = false) String description, 
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "id,asc") String[] sort) {


			Orders order = new Orders(sort);
			Pageable paging = PageRequest.of(page, size, Sort.by(order.getOrders()));

			Page<Tutorial> pageTuts;

			if ((title != null) && (description != null)) {
				pageTuts = tutorialRepository.findByTitleAndDescriptionContaining(title, description, paging);
			} else {
				if ((title == null) && (description == null)) {
					pageTuts = tutorialRepository.findAll(paging);
				} else {
					if (title != null)
						pageTuts = tutorialRepository.findByTitleContaining(title, paging);
					else
						pageTuts = tutorialRepository.findByDescriptionContaining(description, paging);
				}
			}

			Response<Tutorial> response = new Response<Tutorial>(pageTuts);

			return new ResponseEntity<>(response.getResponse(), HttpStatus.OK);

	}

	@GetMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
		Tutorial tutorial = tutorialRepository.findById(id)
		        .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + id));
		    return new ResponseEntity<>(tutorial, HttpStatus.OK);
	}

	@PostMapping("/tutorials")
	public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
		try {
			Tutorial _tutorial = tutorialRepository
					.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
			return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
		if (tutorialData.isPresent()) {
			Tutorial _tutorial = tutorialData.get();
			_tutorial.setTitle(tutorial.getTitle());
			_tutorial.setDescription(tutorial.getDescription());
			_tutorial.setPublished(tutorial.isPublished());
			return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/tutorials/{id}")
	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
		try {
			tutorialRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/tutorials")
	public ResponseEntity<HttpStatus> deleteAllTutorials() {
		try {
			tutorialRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/tutorials/published")
	public ResponseEntity<Map<String, Object>> findByPublished(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		try {
			Pageable paging = PageRequest.of(page, size);
			Page<Tutorial> pageTuts = tutorialRepository.findByPublished(true, paging);
			Response<Tutorial> response = new Response<Tutorial>(pageTuts);
			return new ResponseEntity<>(response.getResponse(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}