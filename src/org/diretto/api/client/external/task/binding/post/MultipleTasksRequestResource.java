package org.diretto.api.client.external.task.binding.post;

import java.util.ArrayList;

/**
 * This class represents a POJO based {@code MultipleTasksRequestResource}.
 * <br/><br/>
 * 
 * It is used for operating with the data interchange format JSON. So it is
 * possible to marshal Java objects into JSON representation and to unmarshal
 * JSON messages into Java objects. <br/><br/>
 * 
 * <i>Annotation:</i> This is also called <u>(full) data binding<u/>
 * 
 * @author Tobias Schlecht
 */
public final class MultipleTasksRequestResource
{
	private ArrayList<String> tasks;

	public ArrayList<String> getTasks()
	{
		return tasks;
	}

	public void setTasks(ArrayList<String> tasks)
	{
		this.tasks = tasks;
	}
}
