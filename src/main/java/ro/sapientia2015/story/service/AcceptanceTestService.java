package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.AcceptanceTestDTO;
import ro.sapientia2015.story.dto.SprintDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.AcceptanceTest;
import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Story;

/**
 * @author Csonka Erik
 */
public interface AcceptanceTestService {

    public AcceptanceTest add(AcceptanceTestDTO added);

    public AcceptanceTest deleteById(Long id) throws NotFoundException;

    public List<AcceptanceTest> findAll();

    public AcceptanceTest findById(Long id) throws NotFoundException;

    public AcceptanceTest update(AcceptanceTestDTO updated) throws NotFoundException;
}
