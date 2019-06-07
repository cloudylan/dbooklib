package cloudylan.dbooklib.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookRestController {

	@RequestMapping(value = "/rest")
	public ResponseEntity<String> getHomePage() {
		return new ResponseEntity<String>("Hello Book.", HttpStatus.OK);
	}

}
