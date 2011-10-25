package org.diretto.api.client.external.task.binding.resources;

import java.util.ArrayList;

import org.diretto.api.client.main.core.binding.resources.HyperLinkResource;
import org.diretto.api.client.main.core.binding.resources.HyperLinkResourceWrapper;

/**
 * This class represents a POJO based {@code PageResource}. <br/><br/>
 * 
 * It is used for operating with the data interchange format JSON. So it is
 * possible to marshal Java objects into JSON representation and to unmarshal
 * JSON messages into Java objects. <br/><br/>
 * 
 * <i>Annotation:</i> This is also called <u>(full) data binding<u/>
 * 
 * @author Tobias Schlecht
 */
public final class PageResource
{
	private HyperLinkResource link;
	private ArrayList<TaskHyperLinkResource> list;
	private ArrayList<HyperLinkResourceWrapper> related;

	public HyperLinkResource getLink()
	{
		return link;
	}

	public void setLink(HyperLinkResource link)
	{
		this.link = link;
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
