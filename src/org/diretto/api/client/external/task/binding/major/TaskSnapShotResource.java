package org.diretto.api.client.external.task.binding.major;

import org.diretto.api.client.external.task.binding.entities.TaskResource;
import org.diretto.api.client.external.task.binding.resources.SubmissionsResource;
import org.diretto.api.client.main.core.binding.resources.CommentsResource;
import org.diretto.api.client.main.core.binding.resources.TagsResource;

/**
 * This class represents a POJO based {@code TaskSnapShotResource}. <br/><br/>
 * 
 * It is used for operating with the data interchange format JSON. So it is
 * possible to marshal Java objects into JSON representation and to unmarshal
 * JSON messages into Java objects. <br/><br/>
 * 
 * <i>Annotation:</i> This is also called <u>(full) data binding<u/>
 * 
 * @author Tobias Schlecht
 */
public final class TaskSnapShotResource
{
	private TaskResource task;
	private SubmissionsResource submissions;
	private CommentsResource comments;
	private TagsResource tags;

	public TaskResource getTask()
	{
		return task;
	}

	public void setTask(TaskResource task)
	{
		this.task = task;
	}

	public SubmissionsResource getSubmissions()
	{
		return submissions;
	}

	public void setSubmissions(SubmissionsResource submissions)
	{
		this.submissions = submissions;
	}

	public CommentsResource getComments()
	{
		return comments;
	}

	public void setComments(CommentsResource comments)
	{
		this.comments = comments;
	}

	public TagsResource getTags()
	{
		return tags;
	}

	public void setTags(TagsResource tags)
	{
		this.tags = tags;
	}
}
