package org.diretto.api.client.external.task.entities;

import org.diretto.api.client.base.characteristic.Commentable;
import org.diretto.api.client.base.characteristic.Creatable;
import org.diretto.api.client.base.characteristic.Describable;
import org.diretto.api.client.base.characteristic.Tagable;
import org.diretto.api.client.base.characteristic.Votable;
import org.diretto.api.client.base.data.BoundingBox;
import org.diretto.api.client.base.data.ResultSet;
import org.diretto.api.client.base.data.TimeRange;
import org.diretto.api.client.base.entities.Entity;
import org.diretto.api.client.main.core.entities.Document;
import org.diretto.api.client.main.core.entities.DocumentID;
import org.diretto.api.client.session.UserSession;

/**
 * This interface represents a {@code Task}.
 * 
 * @author Tobias Schlecht
 */
public interface Task extends Entity<TaskID>, Votable, Commentable, Tagable, Creatable, Describable
{
	/**
	 * Returns the relevant {@link TimeRange}.
	 * 
	 * @return The relevant {@code TimeRange}
	 */
	TimeRange getRelevantTimeRange();

	/**
	 * Returns a {@link BoundingBox} of the relevant area.
	 * 
	 * @return A {@code BoundingBox} of the relevant area
	 */
	BoundingBox getRelevantArea();

	/**
	 * Adds a new {@link Submission} to the {@link Task} and returns the
	 * {@link SubmissionID} if it was successful. <br/><br/>
	 * 
	 * <i>Annotation:</i> A {@code Submission} is represented by a
	 * {@link Document} and the {@code Document} is identified by the
	 * corresponding {@link DocumentID}.
	 * 
	 * @param userSession The corresponding {@code UserSession}
	 * @param documentID A {@code DocumentID}
	 * @return The corresponding {@code SubmissionID}
	 */
	SubmissionID addSubmission(UserSession userSession, DocumentID documentID);

	/**
	 * Returns the {@link Submission} with the specified {@link SubmissionID},
	 * or {@code null} if there is no {@code Submission} with the given
	 * {@code SubmissionID}.
	 * 
	 * @param submissionID A {@code SubmissionID}
	 * @return The {@code Submission}
	 */
	Submission getSubmission(SubmissionID submissionID);

	/**
	 * Returns a {@link ResultSet} of all {@link Submission}s and the mapped
	 * {@link SubmissionID}s.
	 * 
	 * @return A {@code ResultSet} with the {@code Submission}s
	 */
	ResultSet<SubmissionID, Submission> getSubmissions();
}
