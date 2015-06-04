package ro.sapientia2015.story.service;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.AcceptanceTestTestUtil;
import ro.sapientia2015.story.dto.AcceptanceTestDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.AcceptanceTest;
import ro.sapientia2015.story.repository.AcceptanceTestRepository;




import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * @author Csonka Erik
 */
public class RepositoryAcceptanceTestServiceTest {

    private RepositoryAcceptanceTestService service;

    private AcceptanceTestRepository repositoryMock;

    @Before
    public void setUp() {

    	service = new RepositoryAcceptanceTestService();
        repositoryMock = mock(AcceptanceTestRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }


    @Test
    public void findAll() {
        List<AcceptanceTest> models = new ArrayList<AcceptanceTest>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<AcceptanceTest> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }

    @Test
    public void addTest1() {
        
    	AcceptanceTestDTO dto = new AcceptanceTestDTO();
    	dto.setTitle("AcceptanceTestTitle");
    	dto.setDescription("AcceptanceTestDescription");
    	AcceptanceTest.Builder builder = mock(AcceptanceTest.Builder.class);
    	
    	
    	AcceptanceTest model = AcceptanceTest.getBuilder(dto.getTitle())
                .description(dto.getDescription())
                .build();
    	dto.setBuilder(builder);
    	when(builder.build()).thenReturn(model);
    	when(builder.setTitle(dto.getTitle())).thenReturn(builder);
    	when(builder.description(dto.getDescription())).thenReturn(builder);
    	
    	model.setCreationTime(DateTime.now());
    	model.setId(0l);
    	model.setModificationTime(DateTime.now());
    	model.setVersion(0);
    	
        when(repositoryMock.save(model)).thenReturn(model);

        AcceptanceTest actual = service.add(dto);

        verify(repositoryMock, times(1)).save(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }
    
    @Test
    public void deleteById() throws NotFoundException {
    	AcceptanceTest model = AcceptanceTestTestUtil.createModel(AcceptanceTestTestUtil.ID, AcceptanceTestTestUtil.DESCRIPTION, AcceptanceTestTestUtil.TITLE);
        when(repositoryMock.findOne(AcceptanceTestTestUtil.ID)).thenReturn(model);

        AcceptanceTest actual = service.deleteById(AcceptanceTestTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(AcceptanceTestTestUtil.ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(AcceptanceTestTestUtil.ID)).thenReturn(null);

        service.deleteById(AcceptanceTestTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(AcceptanceTestTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void findById() throws NotFoundException {
    	AcceptanceTest model = AcceptanceTestTestUtil.createModel(AcceptanceTestTestUtil.ID, AcceptanceTestTestUtil.DESCRIPTION, AcceptanceTestTestUtil.TITLE);
        when(repositoryMock.findOne(AcceptanceTestTestUtil.ID)).thenReturn(model);

        AcceptanceTest actual = service.findById(AcceptanceTestTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(AcceptanceTestTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(AcceptanceTestTestUtil.ID)).thenReturn(null);

        service.findById(AcceptanceTestTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(AcceptanceTestTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void update() throws NotFoundException {
    	AcceptanceTestDTO dto = AcceptanceTestTestUtil.createFormObject(AcceptanceTestTestUtil.ID, AcceptanceTestTestUtil.DESCRIPTION_UPDATED, AcceptanceTestTestUtil.TITLE_UPDATED);
    	AcceptanceTest model = AcceptanceTestTestUtil.createModel(AcceptanceTestTestUtil.ID, AcceptanceTestTestUtil.DESCRIPTION, AcceptanceTestTestUtil.TITLE);
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        AcceptanceTest actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getDescription(), actual.getDescription());
        assertEquals(dto.getTitle(), actual.getTitle());
    }
    
 }
