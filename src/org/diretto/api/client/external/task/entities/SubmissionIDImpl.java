package org.diretto.api.client.external.task.entities;

import java.net.URL;

import org.diretto.api.client.base.entities.AbstractSubEntityID;
import org.diretto.api.client.base.entities.EntityID;

/**
 * This class is the implementation class of the {@link SubmissionID} interface.
 * 
 * @author Tobias Schlecht
 */
final class SubmissionIDImpl extends AbstractSubEntityID<EntityID, EntityID> implements SubmissionID
{
	/**
	 * Constructs an object of the {@link SubmissionID} interface.
	 * 
	 * @param uniqueResourceURL The unique resource {@code URL}
	 * @param rootEntityID The {@code EntityID} of the root {@code Entity}
	 * @param parentEntityID The {@code EntityID} of the parent {@code Entity}
	 */
	SubmissionIDImpl(URL uniqueResourceURL, EntityID rootEntityID, EntityID parentEntityID)
	{
		super(uniqueResourceURL, rootEntityID, parentEntityID);
	}
}
