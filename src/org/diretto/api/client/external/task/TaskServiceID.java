package org.diretto.api.client.external.task;

import org.apache.commons.configuration.XMLConfiguration;
import org.diretto.api.client.service.AbstractServicePluginID;
import org.diretto.api.client.service.Service;
import org.diretto.api.client.util.ConfigUtils;

/**
 * This class serves for the identification of the {@link TaskService}.
 * <br/><br/>
 * 
 * <i>Annotation:</i> <u>Singleton Pattern</u>
 * 
 * @author Tobias Schlecht
 */
public final class TaskServiceID extends AbstractServicePluginID
{
	private static final String CONFIG_FILE = "org/diretto/api/client/external/task/config.xml";

	private static final XMLConfiguration xmlConfiguration = ConfigUtils.getXMLConfiguration(CONFIG_FILE);

	public static final TaskServiceID INSTANCE = new TaskServiceID(xmlConfiguration.getString("name"), xmlConfiguration.getString("api-version"), getInitServiceClass());

	/**
	 * Constructs the sole instance of the {@link TaskServiceID}. <br/><br/>
	 * 
	 * <i>Annotation:</i> <u>Singleton Pattern</u>
	 */
	private TaskServiceID(String name, String apiVersion, Class<Service> serviceClass)
	{
		super(name, apiVersion, serviceClass);
	}

	/**
	 * Returns the implementation class of the {@link TaskService}, which is
	 * loaded from the XML configuration file.
	 * 
	 * @return The implementation class of the {@code TaskService}
	 */
	@SuppressWarnings("unchecked")
	private static Class<Service> getInitServiceClass()
	{
		try
		{
			return (Class<Service>) Class.forName(xmlConfiguration.getString("service-class"));
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns the {@link XMLConfiguration} object, which is loaded from the XML
	 * configuration file corresponding to the whole {@link TaskService}
	 * implementation.
	 * 
	 * @return The {@code XMLConfiguration} object
	 */
	XMLConfiguration getXMLConfiguration()
	{
		return xmlConfiguration;
	}
}
