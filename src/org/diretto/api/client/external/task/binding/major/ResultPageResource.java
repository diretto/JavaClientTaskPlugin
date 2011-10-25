package org.diretto.api.client.external.task.binding.major;

import java.util.ArrayList;

import org.diretto.api.client.external.task.binding.resources.TaskHyperLinkResource;
import org.diretto.api.client.main.core.binding.resources.HyperLinkResourceWrapper;

/**
 * This class represents a POJO based {@code ResultPageResource}. <br/><br/>
 * 
 * It is used for operating with the data interchange format JSON. So it is
 * possible to marshal Java objects into JSON representation and to unmarshal
 * JSON messages into Java objects. <br/><br/>
 * 
 * <i>Annotation:</i> This is also called <u>(full) data binding<u/>
 * 
 * @author Tobias Schlecht
 */
public final class ResultPageResource
{
	private HyperLinkResourceWrapper page;
	private ArrayList<TaskHyperLinkResource> list;
	private ArrayList<HyperLinkResourceWrapper> related;

	public HyperLinkResourceWrapper getPage()
	{
		return page;
	}

	public void setPage(HyperLinkResourceWrapper page)
	{
		this.page = page;
	}

	public ArrayList<TaskHyperLinkResource> getList()
	{
		return list;
	}

	public void setList(ArrayList<TaskHyperLinkResource> list)
	{
		this.list = list;
	}

	public ArrayList<HyperLinkResourceWrapper> getRelated()
	{
		return related;
	}

	public void setRelated(ArrayList<HyperLinkResourceWrapper> related)
	{
		this.related = related;
	}
}
