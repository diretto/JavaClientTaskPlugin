package org.diretto.api.client.external.task.binding.major;

import java.util.ArrayList;

import org.diretto.api.client.external.task.binding.resources.MainServicesResource;
import org.diretto.api.client.main.core.binding.resources.APIResource;
import org.diretto.api.client.main.core.binding.resources.DeploymentResource;
import org.diretto.api.client.main.core.binding.resources.ParametersResource;
import org.diretto.api.client.main.core.binding.resources.ServiceResource;
import org.diretto.api.client.main.core.binding.resources.TitledLinkResource;

/**
 * This class represents a POJO based {@code TaskServiceInstanceDataResource}.
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
public final class TaskServiceInstanceDataResource
{
	private APIResource api;
	private ServiceResource service;
	private DeploymentResource deployment;
	private MainServicesResource direttoMainServices;
	private ArrayList<TitledLinkResource> links;
	private ParametersResource parameters;

	public APIResource getAPI()
	{
		return api;
	}

	public void setAPI(APIResource api)
	{
		this.api = api;
	}

	public ServiceResource getService()
	{
		return service;
	}

	public void setService(ServiceResource service)
	{
		this.service = service;
	}

	public DeploymentResource getDeployment()
	{
		return deployment;
	}

	public void setDeployment(DeploymentResource deployment)
	{
		this.deployment = deployment;
	}

	public MainServicesResource getDirettoMainServices()
	{
		return direttoMainServices;
	}

	public void setDirettoMainServices(MainServicesResource direttoMainServices)
	{
		this.direttoMainServices = direttoMainServices;
	}

	public ArrayList<TitledLinkResource> getLinks()
	{
		return links;
	}

	public void setLinks(ArrayList<TitledLinkResource> links)
	{
		this.links = links;
	}

	public ParametersResource getParameters()
	{
		return parameters;
	}

	public void setParameters(ParametersResource parameters)
	{
		this.parameters = parameters;
	}
}
