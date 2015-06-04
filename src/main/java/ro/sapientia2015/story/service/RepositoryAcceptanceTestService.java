package ro.sapientia2015.story.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.AcceptanceTestDTO;
import ro.sapientia2015.story.dto.SprintDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.AcceptanceTest;
import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.repository.AcceptanceTestRepository;
import ro.sapientia2015.story.repository.SprintRepository;

/**
 * @author Csonka Erik
 */
@Service
public class RepositoryAcceptanceTestService implements AcceptanceTestService {

    @Resource
    private AcceptanceTestRepository repository;

    @Transactional
    @Override
    public AcceptanceTest add(AcceptanceTestDTO added) {

    	AcceptanceTest model = added.getBuilder().setTitle(added.getTitle())
                .description(added.getDescription())
                .build();

        return repository.save(model);
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public AcceptanceTest deleteById(Long id) throws NotFoundException {
    	AcceptanceTest deleted = findById(id);
        repository.delete(deleted);
        return deleted;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AcceptanceTest> findAll() {
       return repository.findAll();
    }

    @Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
    @Override
    public AcceptanceTest findById(Long id) throws NotFoundException {
    	AcceptanceTest found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public AcceptanceTest update(AcceptanceTestDTO updated) throws NotFoundException {
    	AcceptanceTest model = findById(updated.getId());
        model.update(updated.getDescription(), updated.getTitle());

        return model;
    }

}
