package ro.sapientia2015.story.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.dto.AcceptanceTestDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.AcceptanceTest;
import ro.sapientia2015.story.service.AcceptanceTestService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Csonka Erik
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class AcceptanceTestControllerTest {

    private AcceptanceTestController controller;

    private AcceptanceTestService serviceMock;

    @Resource
    private Validator validator;
    
    @Before
    public void setUp() {
        controller = new AcceptanceTestController();

        serviceMock = mock(AcceptanceTestService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }

    @Test
    public void acceptancetestList1() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.listAcceptanceTest(model);

        assertEquals("acceptancetest/list", view);
     }

    @Test
    public void acceptancetestList2() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        controller.listAcceptanceTest(model);

        List<AcceptanceTest> acceptancetests =  (List<AcceptanceTest>)model.asMap().get("acceptancetests");
        assertNotNull(acceptancetests);
     }

    @Test
    public void acceptancetestList3() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        controller.listAcceptanceTest(model);

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
     }
    
    @Test
    public void acceptancetestList4() {
        BindingAwareModelMap model = new BindingAwareModelMap();       
        List<AcceptanceTest> list = new ArrayList<AcceptanceTest>();
        list.add(new AcceptanceTest());
        when(serviceMock.findAll()).thenReturn(list);

        controller.listAcceptanceTest(model);
        
        List<AcceptanceTest> acceptancetests =  (List<AcceptanceTest>)model.asMap().get("acceptancetests");

        assertEquals(1, acceptancetests.size());
     }
    private BindingResult bindAndValidate(HttpServletRequest request, Object formObject) {
        WebDataBinder binder = new WebDataBinder(formObject);
        binder.setValidator(validator);
        binder.bind(new MutablePropertyValues(request.getParameterMap()));
        binder.getValidator().validate(binder.getTarget(), binder.getBindingResult());
        return binder.getBindingResult();
    }

    @Test
    public void add() {
    	AcceptanceTestDTO formObject = new AcceptanceTestDTO();

        formObject.setTitle("title");
        formObject.setDescription("desc");
        
        AcceptanceTest model = AcceptanceTest.getBuilder("title")
        		.description("desc").build();
        
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/acceptancetest/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();


        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = "redirect:/acceptancetest/list";
        assertEquals(expectedView, view);
    }

    
    @Test
    public void addEmptyStory1() {
    	
    	AcceptanceTestDTO formObject = new AcceptanceTestDTO();

        formObject.setTitle("");
        formObject.setDescription("");
       
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/acceptancetest/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        assertEquals(AcceptanceTestController.VIEW_ADD, view);
    }
    
    @Test
    public void addTooLongStoryTitle() {
    	
    	AcceptanceTestDTO formObject = new AcceptanceTestDTO();

        formObject.setTitle("TooLongTitleeeeeeeeeeeeee");
        formObject.setDescription("");
       
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/acceptancetest/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        assertEquals(AcceptanceTestController.VIEW_ADD, view);
    }
    
    
    
    
}
