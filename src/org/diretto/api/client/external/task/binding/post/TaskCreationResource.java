package org.diretto.api.client.external.task.binding.post;

import org.diretto.api.client.external.task.binding.resources.ConstraintsResource;

/**
 * This class represents a POJO based {@code TaskCreationResource}. <br/><br/>
 * 
 * It is used for operating with the data interchange format JSON. So it is
 * possible to marshal Java objects into JSON representation and to unmarshal
 * JSON messages into Java objects. <br/><br/>
 * 
 * <i>Annotation:</i> This is also called <u>(full) data binding<u/>
 * 
 * @author Tobias Schlecht
 */
public final class TaskCreationResource
{
	private ConstraintsResource constraints;
	private String title;
	private String description;

	public ConstraintsResource getConstraints()
	{
		return constraints;
	}

	public void setConstraints(ConstraintsResource constraints)
	{
		this.constraints = constraints;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
