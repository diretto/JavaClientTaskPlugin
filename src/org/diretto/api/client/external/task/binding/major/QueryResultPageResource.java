package org.diretto.api.client.external.task.binding.major;

import org.diretto.api.client.external.task.binding.resources.ResultsResource;
import org.diretto.api.client.main.core.binding.resources.HyperLinkResourceWrapper;

/**
 * This class represents a POJO based {@code QueryResultPageResource}.
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
public final class QueryResultPageResource
{
	private HyperLinkResourceWrapper query;
	private ResultsResource results;

	public HyperLinkResourceWrapper getQuery()
	{
		return query;
	}

	public void setQuery(HyperLinkResourceWrapper query)
	{
		this.query = query;
	}

	public ResultsResource getResults()
	{
		return results;
	}

	public void setResults(ResultsResource results)
	{
		this.results = results;
	}
}
