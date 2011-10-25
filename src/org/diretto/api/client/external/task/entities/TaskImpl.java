package org.diretto.api.client.external.task.entities;

import org.diretto.api.client.base.characteristic.Cachable;
import org.diretto.api.client.base.characteristic.VoteManager;
import org.diretto.api.client.base.data.BoundingBox;
import org.diretto.api.client.base.data.ResultSet;
import org.diretto.api.client.base.data.TimeRange;
import org.diretto.api.client.base.data.Votes;
import org.diretto.api.client.base.entities.AbstractEntity;
import org.diretto.api.client.base.types.LoadType;
import org.diretto.api.client.base.types.VoteType;
import org.diretto.api.client.external.task.DataManagerImpl;
import org.diretto.api.client.main.core.entities.Comment;
import org.diretto.api.client.main.core.entities.CommentID;
import org.diretto.api.client.main.core.entities.DocumentID;
import org.diretto.api.client.main.core.entities.Tag;
import org.diretto.api.client.main.core.entities.TagID;
import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.user.UserID;
import org.joda.time.DateTime;

/**
 * This class is the implementation class of the {@link Task} interface.
 * 
 * @author Tobias Schlecht
 */
final class TaskImpl extends AbstractEntity<TaskID> implements Task, Cachable
{
	private final DataManagerImpl dataManager;
	private final VoteManager voteManager;

	private final boolean completelyLoaded;
	private final LoadType loadType;

	private final String title;
	private final String description;
	private final DateTime creationTime;
	private final UserID creator;
	private final Votes votes;
	private final TimeRange relevantTimeRange;
	private final BoundingBox relevantArea;

	private final ResultSet<TagID, Tag> tags;
	private final ResultSet<SubmissionID, Submission> submissions;
	private final ResultSet<CommentID, Comment> comments;

	/**
	 * Constructs an object of the {@link Task} interface.
	 * 
	 * @param builder The {@code TaskBuilder} object
	 */
	TaskImpl(TaskBuilder builder)
	{
		super(builder.getTaskID());

		dataManager = builder.getDataManager();
		voteManager = builder.getVoteManager();

		completelyLoaded = builder.getCompletelyLoaded();

		if(completelyLoaded)
		{
			loadType = LoadType.COMPLETE;
		}
		else
		{
			loadType = LoadType.METADATA;
		}

		title = builder.getTitle();
		description = builder.getDescription();
		creationTime = builder.getCreationTime();
		creator = builder.getCreator();
		votes = builder.getVotes();
		relevantTimeRange = builder.getRelevantTimeRange();
		relevantArea = builder.getRelevantArea();

		submissions = builder.getSubmissions();
		comments = builder.getComments();
		tags = builder.getTags();

		if(dataManager == null || voteManager == null)
		{
			throw new NullPointerException();
		}
	}

	@Override
	public boolean isCompletelyLoaded()
	{
		return completelyLoaded;
	}

	@Override
	public LoadType getLoadType()
	{
		return loadType;
	}

	@Override
	public String getTitle()
	{
		return title;
	}

	@Override
	public String getDescription()
	{
		return description;
	}

	@Override
	public DateTime getCreationTime()
	{
		return creationTime;
	}

	@Override
	public UserID getCreator()
	{
		return creator;
	}

	@Override
	public Votes getVotes()
	{
		return votes;
	}

	@Override
	public VoteType getUserVote(UserSession userSession)
	{
		return voteManager.getUserVote(userSession, getID());
	}

	@Override
	public boolean setUserVote(UserSession userSession, VoteType voteType)
	{
		return voteManager.setUserVote(userSession, voteType, getID());
	}

	@Override
	public boolean removeUserVote(UserSession userSession)
	{
		return voteManager.removeUserVote(userSession, getID());
	}

	@Override
	public TimeRange getRelevantTimeRange()
	{
		return relevantTimeRange;
	}

	@Override
	public BoundingBox getRelevantArea()
	{
		return relevantArea;
	}

	@Override
	public TagID addTag(UserSession userSession, String value)
	{
		return dataManager.addTagToEntity(userSession, getID(), value);
	}

	@Override
	public Tag getTag(TagID tagID)
	{
		if(completelyLoaded)
		{
			Tag tag = tags.get(tagID);

			if(tag != null)
			{
				return tag;
			}
		}

		return ((TaskImpl) dataManager.getTask(getID(), true, true)).getTagFromLatestData(tagID);
	}

	/**
	 * Returns the {@link Tag} with the specified {@link TagID}, or {@code null}
	 * if there is no {@code Tag} with the given {@code TagID}. <br/><br/>
	 * 
	 * <i>Annotation:</i> This method should only be used if it is sure that
	 * this object represents the latest data from the API.
	 * 
	 * @param tagID A {@code TagID}
	 * @return The {@code Tag}
	 */
	private Tag getTagFromLatestData(TagID tagID)
	{
		return tags.get(tagID);
	}

	@Override
	public ResultSet<TagID, Tag> getTags()
	{
		if(completelyLoaded)
		{
			return tags;
		}
		else
		{
			return dataManager.getTask(getID(), true, true).getTags();
		}
	}

	@Override
	public SubmissionID addSubmission(UserSession userSession, DocumentID documentID)
	{
		return dataManager.addSubmissionToTask(userSession, getID(), documentID);
	}

	@Override
	public Submission getSubmission(SubmissionID submissionID)
	{
		if(completelyLoaded)
		{
			Submission submission = submissions.get(submissionID);

			if(submission != null)
			{
				return submission;
			}
		}

		return ((TaskImpl) dataManager.getTask(getID(), true, true)).getSubmissionFromLatestData(submissionID);
	}

	/**
	 * Returns the {@link Submission} with the specified {@link SubmissionID},
	 * or {@code null} if there is no {@code Submission} with the given
	 * {@code SubmissionID}. <br/><br/>
	 * 
	 * <i>Annotation:</i> This method should only be used if it is sure that
	 * this object represents the latest data from the API.
	 * 
	 * @param submissionID A {@code SubmissionID}
	 * @return The {@code Submission}
	 */
	private Submission getSubmissionFromLatestData(SubmissionID submissionID)
	{
		return submissions.get(submissionID);
	}

	@Override
	public ResultSet<SubmissionID, Submission> getSubmissions()
	{
		if(completelyLoaded)
		{
			return submissions;
		}
		else
		{
			return dataManager.getTask(getID(), true, true).getSubmissions();
		}
	}

	@Override
	public CommentID addComment(UserSession userSession, String content)
	{
		return dataManager.addCommentToTask(userSession, getID(), content);
	}

	@Override
	public Comment getComment(CommentID commentID)
	{
		if(completelyLoaded)
		{
			Comment comment = comments.get(commentID);

			if(comment != null)
			{
				return comment;
			}
		}

		return ((TaskImpl) dataManager.getTask(getID(), true, true)).getCommentFromLatestData(commentID);
	}

	/**
	 * Returns the {@link Comment} with the specified {@link CommentID}, or
	 * {@code null} if there is no {@code Comment} with the given
	 * {@code CommentID}. <br/><br/>
	 * 
	 * <i>Annotation:</i> This method should only be used if it is sure that
	 * this object represents the latest data from the API.
	 * 
	 * @param commentID A {@code CommentID}
	 * @return The {@code Comment}
	 */
	private Comment getCommentFromLatestData(CommentID commentID)
	{
		return comments.get(commentID);
	}

	@Override
	public ResultSet<CommentID, Comment> getComments()
	{
		if(completelyLoaded)
		{
			return comments;
		}
		else
		{
			return dataManager.getTask(getID(), true, true).getComments();
		}
	}
}
