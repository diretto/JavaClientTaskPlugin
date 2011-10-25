package org.diretto.api.client.external.task.binding.post;

import org.diretto.api.client.main.core.binding.resources.HyperLinkResourceWrapper;

/**
 * This class represents a POJO based {@code SubmissionCreationResource}.
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
public final class SubmissionCreationResource
{
	private HyperLinkResourceWrapper document;

	public HyperLinkResourceWrapper getDocument()
	{
		return document;
	}

	public void setDocument(HyperLinkResourceWrapper document)
	{
		this.document = document;
	}
}
