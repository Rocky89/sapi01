package ro.sapientia2015.story.dto;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.AcceptanceTest;
import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Story;

/**
 * @author Csonka Erik
 */
public class AcceptanceTestDTO {

    private Long id;

    @Length(max = Story.MAX_LENGTH_DESCRIPTION)
    private String description;

    private AcceptanceTest.Builder builder = new AcceptanceTest.Builder();
    
    @NotEmpty
    @Length(max = 20)
    private String title;

    private List<Sprint> sprints;
    
    public AcceptanceTestDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

	public List<Sprint> getSprints() {
		return sprints;
	}

	public void setSprints(List<Sprint> sprints) {
		this.sprints = sprints;
	}

	public AcceptanceTest.Builder getBuilder() {
		return builder;
	}

	public void setBuilder(AcceptanceTest.Builder builder) {
		this.builder = builder;
	}
}
