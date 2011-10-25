package org.diretto.api.client.external.task.binding.post;

import org.diretto.api.client.external.task.binding.resources.QueryResource;

/**
 * This class represents a POJO based {@code DispatchQueryResource}. <br/><br/>
 * 
 * It is used for operating with the data interchange format JSON. So it is
 * possible to marshal Java objects into JSON representation and to unmarshal
 * JSON messages into Java objects. <br/><br/>
 * 
 * <i>Annotation:</i> This is also called <u>(full) data binding<u/>
 * 
 * @author Tobias Schlecht
 */
public final class DispatchQueryResource
{
	private QueryResource query;

	public QueryResource getQuery()
	{
		return query;
	}

	public void setQuery(QueryResource query)
	{
		this.query = query;
	}
}
