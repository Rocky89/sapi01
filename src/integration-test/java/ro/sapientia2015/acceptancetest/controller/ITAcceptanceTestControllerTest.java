package ro.sapientia2015.acceptancetest.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ro.sapientia2015.common.controller.ErrorController;
import ro.sapientia2015.config.ExampleApplicationContext;
import ro.sapientia2015.context.WebContextLoader;
import ro.sapientia2015.story.AcceptanceTestTestUtil;
import ro.sapientia2015.story.controller.AcceptanceTestController;
import ro.sapientia2015.story.dto.AcceptanceTestDTO;
import ro.sapientia2015.story.model.AcceptanceTest;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;

/**
 * This test uses the annotation based application context configuration.
 * @author Csonka Erik
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("acceptancetestData.xml")
public class ITAcceptanceTestControllerTest {

    private static final String FORM_FIELD_DESCRIPTION = "description";
    private static final String FORM_FIELD_ID = "id";
    private static final String FORM_FIELD_TITLE = "title";

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @ExpectedDatabase("acceptancetestData.xml")
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/acceptancetest/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(AcceptanceTestController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/acceptancetest/add.jsp"))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("acceptancetestData.xml")
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/acceptancetest/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(AcceptanceTestController.MODEL_ATTRIBUTE, new AcceptanceTestDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(AcceptanceTestController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/acceptancetest/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(AcceptanceTestController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("acceptancetestData.xml")
    public void addWhenTitleAndDescriptionAreTooLong() throws Exception {
        String title = AcceptanceTestTestUtil.createStringWithLength(AcceptanceTest.MAX_LENGTH_TITLE + 1);
        String description = AcceptanceTestTestUtil.createStringWithLength(AcceptanceTest.MAX_LENGTH_DESCRIPTION + 1);

        mockMvc.perform(post("/acceptancetest/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, description)
                .param(FORM_FIELD_TITLE, title)
                .sessionAttr(AcceptanceTestController.MODEL_ATTRIBUTE, new AcceptanceTestDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(AcceptanceTestController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/acceptancetest/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(AcceptanceTestController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attributeHasFieldErrors(AcceptanceTestController.MODEL_ATTRIBUTE, "description"))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("description", is(description))))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("title", is(title))));
    }


    @Test
    @ExpectedDatabase("acceptancetestData.xml")
    public void findById() throws Exception {
        mockMvc.perform(get("/acceptancetest/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(AcceptanceTestController.VIEW_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/acceptancetest/view.jsp"))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("description", is("Lorem ipsum"))))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("title", is("Foo"))));
    }

    @Test
    @ExpectedDatabase("acceptancetestData.xml")
    public void findByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/acceptancetest/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }


    @Test
    @ExpectedDatabase("acceptancetestData.xml")
    public void deleteByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/acceptancetest/delete/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase("acceptancetestData.xml")
    public void showUpdateForm() throws Exception {
        mockMvc.perform(get("/acceptancetest/update/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(AcceptanceTestController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/acceptancetest/update.jsp"))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("description", is("Lorem ipsum"))))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("title", is("Foo"))));
    }

    @Test
    @ExpectedDatabase("acceptancetestData.xml")
    public void showUpdateFormWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/acceptancetest/update/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase("acceptancetestData.xml")
    public void updateEmpty() throws Exception {
        mockMvc.perform(post("/acceptancetest/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "1")
                .sessionAttr(AcceptanceTestController.MODEL_ATTRIBUTE, new AcceptanceTestDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(AcceptanceTestController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/acceptancetest/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(AcceptanceTestController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("acceptancetestData.xml")
    public void updateWhenTitleAndDescriptionAreTooLong() throws Exception {
        String title = AcceptanceTestTestUtil.createStringWithLength(AcceptanceTest.MAX_LENGTH_TITLE + 1);
        String description = AcceptanceTestTestUtil.createStringWithLength(AcceptanceTest.MAX_LENGTH_DESCRIPTION + 1);

        mockMvc.perform(post("/acceptancetest/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, description)
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_TITLE, title)
                .sessionAttr(AcceptanceTestController.MODEL_ATTRIBUTE, new AcceptanceTestDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(AcceptanceTestController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/acceptancetest/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(AcceptanceTestController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attributeHasFieldErrors(AcceptanceTestController.MODEL_ATTRIBUTE, "description"))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("description", is(description))))
                .andExpect(model().attribute(AcceptanceTestController.MODEL_ATTRIBUTE, hasProperty("title", is(title))));
    }


    @Test
    @ExpectedDatabase("acceptancetestData.xml")
    public void updateWhenIsNotFound() throws Exception {
        mockMvc.perform(post("/acceptancetest/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, "description")
                .param(FORM_FIELD_ID, "3")
                .param(FORM_FIELD_TITLE, "title")
                .sessionAttr(AcceptanceTestController.MODEL_ATTRIBUTE, new AcceptanceTestDTO())
        )
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
}

