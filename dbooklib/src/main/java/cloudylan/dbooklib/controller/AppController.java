package cloudylan.dbooklib.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller(value="AppController")
@RequestMapping("/service")
public class AppController {

	/**
	 * 
	 * This page returns exposed rest APIs.
	 */
	@RequestMapping(value="/api", method=RequestMethod.GET)
	public String getAPIs()
	{
		List<String> apis = new ArrayList<String>();
		apis.add("/api");
		apis.add("/api");
		return "api";
	}
}
