package org.diretto.api.client.external.task.entities;

import org.diretto.api.client.base.characteristic.Creatable;
import org.diretto.api.client.base.characteristic.Tagable;
import org.diretto.api.client.base.characteristic.Votable;
import org.diretto.api.client.base.entities.SubEntity;
import org.diretto.api.client.main.core.entities.Document;
import org.diretto.api.client.main.core.entities.DocumentID;

/**
 * This interface represents a {@code Submission}.
 * 
 * @author Tobias Schlecht
 */
public interface Submission extends SubEntity<SubmissionID>, Votable, Tagable, Creatable
{
	/**
	 * Returns the {@link DocumentID}. <br/><br/>
	 * 
	 * <i>Annotation:</i> A {@code Submission} is represented by a
	 * {@link Document} and the {@code Document} is identified by the
	 * corresponding {@link DocumentID}.
	 * 
	 * @return The {@code DocumentID}
	 */
	DocumentID getDocumentID();
}
