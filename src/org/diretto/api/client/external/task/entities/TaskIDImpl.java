package org.diretto.api.client.external.task.entities;

import java.net.URL;

import org.diretto.api.client.base.entities.AbstractEntityID;

/**
 * This class is the implementation class of the {@link TaskID} interface.
 * 
 * @author Tobias Schlecht
 */
final class TaskIDImpl extends AbstractEntityID implements TaskID
{
	/**
	 * Constructs an object of the {@link TaskID} interface.
	 * 
	 * @param uniqueResourceURL The unique resource {@code URL}
	 */
	TaskIDImpl(URL uniqueResourceURL)
	{
		super(uniqueResourceURL);
	}
}
