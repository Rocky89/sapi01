package ro.sapientia2015.story.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia2015.story.model.AcceptanceTest;
import ro.sapientia2015.story.model.Sprint;

/**
 * @author Csonka Erik
 */
public interface AcceptanceTestRepository extends JpaRepository<AcceptanceTest, Long> {
}
