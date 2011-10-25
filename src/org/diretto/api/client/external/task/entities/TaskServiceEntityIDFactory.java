package org.diretto.api.client.external.task.entities;

import java.net.MalformedURLException;
import java.net.URL;

import org.diretto.api.client.base.entities.EntityID;

/**
 * The {@code TaskServiceEntityIDFactory} is a noninstantiable factory class and
 * is responsible for creating the {@link EntityID} instances.
 * 
 * @author Tobias Schlecht
 */
public final class TaskServiceEntityIDFactory
{
	/**
	 * The constructor is {@code private} to suppress the default constructor
	 * for noninstantiability.
	 */
	private TaskServiceEntityIDFactory()
	{
		throw new AssertionError();
	}

	/**
	 * Returns an instance of the requested {@link SubmissionID}.
	 * 
	 * @param uniqueResourceURL The unique resource {@code URL}
	 * @param rootEntityID The {@code EntityID} of the root {@code Entity}
	 * @param parentEntityID The {@code EntityID} of the parent {@code Entity}
	 * @return An instance of the requested {@code SubmissionID}
	 */
	public static SubmissionID getSubmissionIDInstance(URL uniqueResourceURL, EntityID rootEntityID, EntityID parentEntityID)
	{
		return new SubmissionIDImpl(uniqueResourceURL, rootEntityID, parentEntityID);
	}

	/**
	 * Returns an instance of the requested {@link SubmissionID}.
	 * 
	 * @param uniqueResourceURL The unique resource {@code URL}
	 * @param rootEntityID The {@code EntityID} of the root {@code Entity}
	 * @param parentEntityID The {@code EntityID} of the parent {@code Entity}
	 * @return An instance of the requested {@code SubmissionID}
	 */
	public static SubmissionID getSubmissionIDInstance(String uniqueResourceURL, EntityID rootEntityID, EntityID parentEntityID)
	{
		SubmissionID submissionID = null;

		try
		{
			submissionID = new SubmissionIDImpl(new URL(uniqueResourceURL), rootEntityID, parentEntityID);
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}

		return submissionID;
	}

	/**
	 * Returns an instance of the requested {@link TaskID}.
	 * 
	 * @param uniqueResourceURL The unique resource {@code URL}
	 * @return An instance of the requested {@code TaskID}
	 */
	public static TaskID getTaskIDInstance(URL uniqueResourceURL)
	{
		return new TaskIDImpl(uniqueResourceURL);
	}

	/**
	 * Returns an instance of the requested {@link TaskID}.
	 * 
	 * @param uniqueResourceURL The unique resource {@code URL}
	 * @return An instance of the requested {@code TaskID}
	 */
	public static TaskID getTaskIDInstance(String uniqueResourceURL)
	{
		TaskID taskID = null;

		try
		{
			taskID = new TaskIDImpl(new URL(uniqueResourceURL));
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}

		return taskID;
	}
}
