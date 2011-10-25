package org.diretto.api.client.external.task.binding.major;

import org.diretto.api.client.external.task.binding.entities.TaskResource;
import org.diretto.api.client.main.core.binding.resources.HyperLinkResourceWrapper;

/**
 * This class represents a POJO based {@code TaskMetaDataResource}. <br/><br/>
 * 
 * It is used for operating with the data interchange format JSON. So it is
 * possible to marshal Java objects into JSON representation and to unmarshal
 * JSON messages into Java objects. <br/><br/>
 * 
 * <i>Annotation:</i> This is also called <u>(full) data binding<u/>
 * 
 * @author Tobias Schlecht
 */
public final class TaskMetaDataResource
{
	private TaskResource task;
	private HyperLinkResourceWrapper submissions;
	private HyperLinkResourceWrapper comments;
	private HyperLinkResourceWrapper tags;

	public TaskResource getTask()
	{
		return task;
	}

	public void setTask(TaskResource task)
	{
		this.task = task;
	}

	public HyperLinkResourceWrapper getSubmissions()
	{
		return submissions;
	}

	public void setSubmissions(HyperLinkResourceWrapper submissions)
	{
		this.submissions = submissions;
	}

	public HyperLinkResourceWrapper getComments()
	{
		return comments;
	}

	public void setComments(HyperLinkResourceWrapper comments)
	{
		this.comments = comments;
	}

	public HyperLinkResourceWrapper getTags()
	{
		return tags;
	}

	public void setTags(HyperLinkResourceWrapper tags)
	{
		this.tags = tags;
	}
}
