package org.diretto.api.client.external.task.entities;

import org.diretto.api.client.base.characteristic.VoteManager;
import org.diretto.api.client.base.data.BoundingBox;
import org.diretto.api.client.base.data.Builder;
import org.diretto.api.client.base.data.ResultSet;
import org.diretto.api.client.base.data.TimeRange;
import org.diretto.api.client.base.data.Votes;
import org.diretto.api.client.external.task.DataManager;
import org.diretto.api.client.external.task.DataManagerImpl;
import org.diretto.api.client.main.core.entities.Comment;
import org.diretto.api.client.main.core.entities.CommentID;
import org.diretto.api.client.main.core.entities.Tag;
import org.diretto.api.client.main.core.entities.TagID;
import org.diretto.api.client.user.User;
import org.diretto.api.client.user.UserID;
import org.joda.time.DateTime;

/**
 * This class implements the {@link Builder} interface and serves for creating
 * {@code Builder} objects for {@link Task}s.
 * 
 * @author Tobias Schlecht
 */
public final class TaskBuilder implements Builder<Task>
{
	private final TaskID taskID;

	private final DataManagerImpl dataManager;
	private final VoteManager voteManager;

	private final boolean completelyLoaded;

	private String title = null;
	private String description = null;
	private DateTime creationTime = null;
	private UserID creator = null;
	private Votes votes = null;
	private TimeRange relevantTimeRange = null;
	private BoundingBox relevantArea = null;

	private ResultSet<SubmissionID, Submission> submissions = null;
	private ResultSet<TagID, Tag> tags = null;
	private ResultSet<CommentID, Comment> comments = null;

	/**
	 * Constructs a {@link TaskBuilder} object.
	 * 
	 * @param taskID The {@code TaskID}
	 * @param dataManager The {@code DataManager}
	 * @param voteManager The {@code VoteManager}
	 * @param completelyLoaded The {@code completelyLoaded} status
	 */
	public TaskBuilder(TaskID taskID, DataManagerImpl dataManager, VoteManager voteManager, boolean completelyLoaded)
	{
		this.taskID = taskID;
		this.dataManager = dataManager;
		this.voteManager = voteManager;
		this.completelyLoaded = completelyLoaded;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title The title
	 * @return The {@code TaskBuilder} object
	 */
	public TaskBuilder title(String title)
	{
		this.title = title;
		return this;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description The description
	 * @return The {@code TaskBuilder} object
	 */
	public TaskBuilder description(String description)
	{
		this.description = description;
		return this;
	}

	/**
	 * Sets the creation {@link DateTime}.
	 * 
	 * @param creationTime The creation {@code DateTime}
	 * @return The {@code TaskBuilder} object
	 */
	public TaskBuilder creationTime(DateTime creationTime)
	{
		this.creationTime = creationTime;
		return this;
	}

	/**
	 * Sets the {@link UserID} of the creator.
	 * 
	 * @param creator The {@code UserID} of the creator
	 * @return The {@code TaskBuilder} object
	 */
	public TaskBuilder creator(UserID creator)
	{
		this.creator = creator;
		return this;
	}

	/**
	 * Sets the {@link Votes} of all {@link User}s.
	 * 
	 * @param votes The {@code Votes} of all {@code User}s
	 * @return The {@code TaskBuilder} object
	 */
	public TaskBuilder votes(Votes votes)
	{
		this.votes = votes;
		return this;
	}

	/**
	 * Sets the relevant {@link TimeRange}.
	 * 
	 * @param relevantTimeRange The relevant {@code TimeRange}
	 * @return The {@code TaskBuilder} object
	 */
	public TaskBuilder relevantTimeRange(TimeRange relevantTimeRange)
	{
		this.relevantTimeRange = relevantTimeRange;
		return this;
	}

	/**
	 * Sets the {@link BoundingBox} of the relevant area.
	 * 
	 * @param relevantArea The {@code BoundingBox} of the relevant area
	 * @return The {@code TaskBuilder} object
	 */
	public TaskBuilder relevantArea(BoundingBox relevantArea)
	{
		this.relevantArea = relevantArea;
		return this;
	}

	/**
	 * Sets the {@link ResultSet} with the {@link Submission}s.
	 * 
	 * @param submissions The {@code ResultSet} with the {@code Submission}s
	 * @return The {@code TaskBuilder} object
	 */
	public TaskBuilder submissions(ResultSet<SubmissionID, Submission> submissions)
	{
		this.submissions = submissions;
		return this;
	}

	/**
	 * Sets the {@link ResultSet} with the {@link Comment}s.
	 * 
	 * @param comments The {@code ResultSet} with the {@code Comment}s
	 * @return The {@code TaskBuilder} object
	 */
	public TaskBuilder comments(ResultSet<CommentID, Comment> comments)
	{
		this.comments = comments;
		return this;
	}

	/**
	 * Sets the {@link ResultSet} with the {@link Tag}s.
	 * 
	 * @param tags The {@code ResultSet} with the {@code Tag}s
	 * @return The {@code TaskBuilder} object
	 */
	public TaskBuilder tags(ResultSet<TagID, Tag> tags)
	{
		this.tags = tags;
		return this;
	}

	/**
	 * Returns the {@link TaskID}.
	 * 
	 * @return The {@code TaskID}
	 */
	public TaskID getTaskID()
	{
		return taskID;
	}

	/**
	 * Returns the {@link DataManager}.
	 * 
	 * @return The {@code DataManager}
	 */
	public DataManagerImpl getDataManager()
	{
		return dataManager;
	}

	/**
	 * Returns the {@link VoteManager}.
	 * 
	 * @return The {@code VoteManager}
	 */
	public VoteManager getVoteManager()
	{
		return voteManager;
	}

	/**
	 * Returns the {@code completelyLoaded} status.
	 * 
	 * @return The {@code completelyLoaded} status
	 */
	public boolean getCompletelyLoaded()
	{
		return completelyLoaded;
	}

	/**
	 * Returns the title.
	 * 
	 * @return The title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Returns the description.
	 * 
	 * @return The description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Returns the creation {@link DateTime}.
	 * 
	 * @return The creation {@code DateTime}
	 */
	public DateTime getCreationTime()
	{
		return creationTime;
	}

	/**
	 * Returns the {@link UserID} of the creator.
	 * 
	 * @return The {@code UserID} of the creator
	 */
	public UserID getCreator()
	{
		return creator;
	}

	/**
	 * Returns the {@link Votes} of all {@link User}s.
	 * 
	 * @return The {@code Votes} of all {@code User}s
	 */
	public Votes getVotes()
	{
		return votes;
	}

	/**
	 * Returns the relevant {@link TimeRange}.
	 * 
	 * @return The relevant {@code TimeRange}
	 */
	public TimeRange getRelevantTimeRange()
	{
		return relevantTimeRange;
	}

	/**
	 * Returns the {@link BoundingBox} of the relevant area.
	 * 
	 * @return The {@code BoundingBox} of the relevant area
	 */
	public BoundingBox getRelevantArea()
	{
		return relevantArea;
	}

	/**
	 * Returns the {@link ResultSet} with the {@link Submission}s.
	 * 
	 * @return The {@code ResultSet} with the {@code Submission}s
	 */
	public ResultSet<SubmissionID, Submission> getSubmissions()
	{
		return submissions;
	}

	/**
	 * Returns the {@link ResultSet} with the {@link Comment}s.
	 * 
	 * @return The {@code ResultSet} with the {@code Comment}s
	 */
	public ResultSet<CommentID, Comment> getComments()
	{
		return comments;
	}

	/**
	 * Returns the {@link ResultSet} with the {@link Tag}s.
	 * 
	 * @return The {@code ResultSet} with the {@code Tag}s
	 */
	public ResultSet<TagID, Tag> getTags()
	{
		return tags;
	}

	@Override
	public Task build()
	{
		return new TaskImpl(this);
	}
}
