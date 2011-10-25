package org.diretto.api.client.external.task.binding.resources;

/**
 * This class represents a POJO based {@code ResultsResource}. <br/><br/>
 * 
 * It is used for operating with the data interchange format JSON. So it is
 * possible to marshal Java objects into JSON representation and to unmarshal
 * JSON messages into Java objects. <br/><br/>
 * 
 * <i>Annotation:</i> This is also called <u>(full) data binding<u/>
 * 
 * @author Tobias Schlecht
 */
public final class ResultsResource
{
	private int count;
	private PageResource page;

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public PageResource getPage()
	{
		return page;
	}

	public void setPage(PageResource page)
	{
		this.page = page;
	}
}
