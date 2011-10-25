package org.diretto.api.client.external.task.binding.entities;

import org.diretto.api.client.main.core.binding.resources.HyperLinkResource;
import org.diretto.api.client.main.core.binding.resources.HyperLinkResourceWrapper;
import org.diretto.api.client.main.core.binding.resources.TagsResource;
import org.diretto.api.client.main.core.binding.resources.VotesResource;

/**
 * This class represents a POJO based {@code SubmissionResource}. <br/><br/>
 * 
 * It is used for operating with the data interchange format JSON. So it is
 * possible to marshal Java objects into JSON representation and to unmarshal
 * JSON messages into Java objects. <br/><br/>
 * 
 * <i>Annotation:</i> This is also called <u>(full) data binding<u/>
 * 
 * @author Tobias Schlecht
 */
public final class SubmissionResource
{
	private HyperLinkResource link;
	private VotesResource votes;
	private HyperLinkResourceWrapper document;
	private String creationTime;
	private HyperLinkResourceWrapper creator;
	private TagsResource tags;

	public HyperLinkResource getLink()
	{
		return link;
	}

	public void setLink(HyperLinkResource link)
	{
		this.link = link;
	}

	public VotesResource getVotes()
	{
		return votes;
	}

	public void setVotes(VotesResource votes)
	{
		this.votes = votes;
	}

	public HyperLinkResourceWrapper getDocument()
	{
		return document;
	}

	public void setDocument(HyperLinkResourceWrapper document)
	{
		this.document = document;
	}

	public String getCreationTime()
	{
		return creationTime;
	}

	public void setCreationTime(String creationTime)
	{
		this.creationTime = creationTime;
	}

	public HyperLinkResourceWrapper getCreator()
	{
		return creator;
	}

	public void setCreator(HyperLinkResourceWrapper creator)
	{
		this.creator = creator;
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
