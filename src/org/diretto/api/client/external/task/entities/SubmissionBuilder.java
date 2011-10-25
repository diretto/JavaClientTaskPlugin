package org.diretto.api.client.external.task.entities;

import org.diretto.api.client.base.characteristic.VoteManager;
import org.diretto.api.client.base.data.Builder;
import org.diretto.api.client.base.data.ResultSet;
import org.diretto.api.client.base.data.Votes;
import org.diretto.api.client.external.task.DataManager;
import org.diretto.api.client.external.task.DataManagerImpl;
import org.diretto.api.client.main.core.entities.DocumentID;
import org.diretto.api.client.main.core.entities.Tag;
import org.diretto.api.client.main.core.entities.TagID;
import org.diretto.api.client.user.User;
import org.diretto.api.client.user.UserID;
import org.joda.time.DateTime;

/**
 * This class implements the {@link Builder} interface and serves for creating
 * {@code Builder} objects for {@link Submission}s.
 * 
 * @author Tobias Schlecht
 */
public final class SubmissionBuilder implements Builder<Submission>
{
	private final SubmissionID submissionID;

	private final DataManagerImpl dataManager;
	private final VoteManager voteManager;

	private final DocumentID documentID;

	private DateTime creationTime = null;
	private UserID creator = null;
	private Votes votes = null;

	private ResultSet<TagID, Tag> tags = null;

	/**
	 * Constructs a {@link SubmissionBuilder} object.
	 * 
	 * @param submissionID The {@code SubmissionID}
	 * @param dataManager The {@code DataManager}
	 * @param voteManager The {@code VoteManager}
	 * @param documentID The {@code DocumentID}
	 */
	public SubmissionBuilder(SubmissionID submissionID, DataManagerImpl dataManager, VoteManager voteManager, DocumentID documentID)
	{
		this.submissionID = submissionID;
		this.dataManager = dataManager;
		this.voteManager = voteManager;
		this.documentID = documentID;
	}

	/**
	 * Sets the creation {@link DateTime}.
	 * 
	 * @param creationTime The creation {@code DateTime}
	 * @return The {@code SubmissionBuilder} object
	 */
	public SubmissionBuilder creationTime(DateTime creationTime)
	{
		this.creationTime = creationTime;
		return this;
	}

	/**
	 * Sets the {@link UserID} of the creator.
	 * 
	 * @param creator The {@code UserID} of the creator
	 * @return The {@code SubmissionBuilder} object
	 */
	public SubmissionBuilder creator(UserID creator)
	{
		this.creator = creator;
		return this;
	}

	/**
	 * Sets the {@link Votes} of all {@link User}s.
	 * 
	 * @param votes The {@code Votes} of all {@code User}s
	 * @return The {@code SubmissionBuilder} object
	 */
	public SubmissionBuilder votes(Votes votes)
	{
		this.votes = votes;
		return this;
	}

	/**
	 * Sets the {@link ResultSet} with the {@link Tag}s.
	 * 
	 * @param tags The {@code ResultSet} with the {@code Tag}s
	 * @return The {@code SubmissionBuilder} object
	 */
	public SubmissionBuilder tags(ResultSet<TagID, Tag> tags)
	{
		this.tags = tags;
		return this;
	}

	/**
	 * Returns the {@link SubmissionID}.
	 * 
	 * @return The {@code SubmissionID}
	 */
	public SubmissionID getSubmissionID()
	{
		return submissionID;
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
	 * Returns the {@link DocumentID}.
	 * 
	 * @return The {@code DocumentID}
	 */
	public DocumentID getDocumentID()
	{
		return documentID;
	}

	/**
	 * Returns the creation {@link DateTime}
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
	 * Returns the {@link ResultSet} with the {@link Tag}s.
	 * 
	 * @return The {@code ResultSet} with the {@code Tag}s
	 */
	public ResultSet<TagID, Tag> getTags()
	{
		return tags;
	}

	@Override
	public Submission build()
	{
		return new SubmissionImpl(this);
	}
}
