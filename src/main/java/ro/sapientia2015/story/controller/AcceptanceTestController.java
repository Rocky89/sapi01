package ro.sapientia2015.story.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.AcceptanceTestDTO;
import ro.sapientia2015.story.model.AcceptanceTest;
import ro.sapientia2015.story.service.AcceptanceTestService;

@Controller
public class AcceptanceTestController {

	@Resource
	private AcceptanceTestService service;
	
	public static final String VIEW_LIST = "acceptancetest/list";
	public static final String VIEW_ADD = "acceptancetest/add";
	public static final String VIEW_UPDATE = "acceptancetest/update";
    public static final String VIEW_VIEW = "acceptancetest/view";

	public static final String MODEL_ATTRIBUTE = "acceptancetest";

	@RequestMapping(value = "/acceptancetest/list", method = RequestMethod.GET)
	public String listAcceptanceTest(Model model) {

		List<AcceptanceTest> acceptancetests = service.findAll();
		model.addAttribute("acceptancetests", acceptancetests);
		return VIEW_LIST;
	}
	
    private String createRedirectViewPath(String requestMapping) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(requestMapping);
        return redirectViewPath.toString();
    }
    
	@RequestMapping(value = "/acceptancetest/add", method = RequestMethod.GET)
	public String showForm(Model model) {

		AcceptanceTestDTO acceptancetest = new AcceptanceTestDTO();
		model.addAttribute("acceptancetest", acceptancetest);
		return VIEW_ADD;
	}

	@RequestMapping(value = "/acceptancetest/add", method = RequestMethod.POST)
	public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) AcceptanceTestDTO dto, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()){
			return VIEW_ADD;
		}
		
		service.add(dto);
		
		return createRedirectViewPath("/acceptancetest/list");
	}
	
}
