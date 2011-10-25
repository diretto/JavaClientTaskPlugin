package org.diretto.api.client.external.task.entities;

import org.diretto.api.client.base.characteristic.VoteManager;
import org.diretto.api.client.base.data.ResultSet;
import org.diretto.api.client.base.data.Votes;
import org.diretto.api.client.base.entities.AbstractSubEntity;
import org.diretto.api.client.base.types.VoteType;
import org.diretto.api.client.external.task.DataManagerImpl;
import org.diretto.api.client.main.core.entities.DocumentID;
import org.diretto.api.client.main.core.entities.Tag;
import org.diretto.api.client.main.core.entities.TagID;
import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.user.UserID;
import org.joda.time.DateTime;

/**
 * This class is the implementation class of the {@link Submission} interface.
 * 
 * @author Tobias Schlecht
 */
final class SubmissionImpl extends AbstractSubEntity<SubmissionID> implements Submission
{
	private final DataManagerImpl dataManager;
	private final VoteManager voteManager;

	private final DocumentID documentID;

	private final DateTime creationTime;
	private final UserID creator;
	private final Votes votes;

	private final ResultSet<TagID, Tag> tags;

	/**
	 * Constructs an object of the {@link Submission} interface.
	 * 
	 * @param builder The {@code SubmissionBuilder} object
	 */
	SubmissionImpl(SubmissionBuilder builder)
	{
		super(builder.getSubmissionID());

		dataManager = builder.getDataManager();
		voteManager = builder.getVoteManager();

		documentID = builder.getDocumentID();

		creationTime = builder.getCreationTime();
		creator = builder.getCreator();
		votes = builder.getVotes();

		tags = builder.getTags();

		if(dataManager == null || voteManager == null || documentID == null)
		{
			throw new NullPointerException();
		}
	}

	@Override
	public DocumentID getDocumentID()
	{
		return documentID;
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
	public VoteType getUserVote(UserSession userSession)
	{
		return voteManager.getUserVote(userSession, getID());
	}

	@Override
	public Votes getVotes()
	{
		return votes;
	}

	@Override
	public TagID addTag(UserSession userSession, String value)
	{
		return dataManager.addTagToEntity(userSession, getID(), value);
	}

	@Override
	public Tag getTag(TagID tagID)
	{
		Tag tag = tags.get(tagID);

		if(tag != null)
		{
			return tag;
		}

		return ((SubmissionImpl) dataManager.getTask((TaskID) getID().getRootID(), true, true).getSubmission(getID())).getTagFromLatestData(tagID);
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
		return tags;
	}
}
