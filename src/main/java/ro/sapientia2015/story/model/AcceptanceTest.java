package ro.sapientia2015.story.model;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * @author Csonka Erik
 */
@Entity
@Table(name="acceptancetest")
public class AcceptanceTest {

    public static final int MAX_LENGTH_DESCRIPTION = 500;
    public static final int MAX_LENGTH_TITLE = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationTime;

    @Column(name = "description", nullable = true, length = MAX_LENGTH_DESCRIPTION)
    private String description;

    @Column(name = "modification_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modificationTime;

    @Column(name = "title", nullable = false, length = MAX_LENGTH_TITLE)
    private String title;

    @Version
    private long version;

    @Column(name = "sprint")
    @OneToMany
    private List<Sprint> sprints;

    public AcceptanceTest() {

    }

    public static Builder getBuilder(String title) {
        return new Builder(title);
    }

    public Long getId() {
        return id;
    }

    public List<Sprint> getSprints() {
		return sprints;
	}

	public void setStories(List<Sprint> sprints) {
		this.sprints = sprints;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setModificationTime(DateTime modificationTime) {
		this.modificationTime = modificationTime;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public DateTime getCreationTime() {
        return creationTime;
    }

    public String getDescription() {
        return description;
    }

    public DateTime getModificationTime() {
        return modificationTime;
    }

    public String getTitle() {
        return title;
    }

    public long getVersion() {
        return version;
    }

    @PrePersist
    public void prePersist() {
        DateTime now = DateTime.now();
        creationTime = now;
        modificationTime = now;
    }

    @PreUpdate
    public void preUpdate() {
        modificationTime = DateTime.now();
    }

    public void update(String description, String title) {
        this.description = description;
        this.title = title;
    }

    public static class Builder {

        private AcceptanceTest built;

        public Builder() {
            built = new AcceptanceTest();
        }
        
        public Builder setTitle(String title)
        {
        	this.built.title=title;
        	return this;
        }
        
        public Builder(String title) {
            built = new AcceptanceTest();
            built.title = title;
        }

        public AcceptanceTest build() {
            return built;
        }

        public Builder description(String description) {
            built.description = description;
            return this;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
