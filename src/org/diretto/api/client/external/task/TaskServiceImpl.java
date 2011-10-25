package org.diretto.api.client.external.task;

import java.net.URL;
import java.util.List;

import org.diretto.api.client.JavaClient;
import org.diretto.api.client.JavaClientImpl;
import org.diretto.api.client.base.annotations.InvocationLimited;
import org.diretto.api.client.base.data.BoundingBox;
import org.diretto.api.client.base.data.ResultSet;
import org.diretto.api.client.base.data.TimeRange;
import org.diretto.api.client.base.types.OrderType;
import org.diretto.api.client.external.task.binding.major.TaskServiceInstanceDataResource;
import org.diretto.api.client.external.task.entities.Task;
import org.diretto.api.client.external.task.entities.TaskID;
import org.diretto.api.client.service.AbstractService;
import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.util.InvocationUtils;
import org.diretto.api.client.util.URLTransformationUtils;
import org.joda.time.DateTime;
import org.restlet.Client;
import org.restlet.resource.ClientResource;

/**
 * This class is the implementation class of the {@link TaskService} interface.
 * 
 * @author Tobias Schlecht
 */
public final class TaskServiceImpl extends AbstractService implements TaskService
{
	private final Client restletClient;
	private final int maxTaskRequestSize;

	private DataManager dataManager = null;

	/**
	 * The constructor is {@code private} to have strict control what instances
	 * exist at any time. Instead of the constructor the {@code public}
	 * <i>static factory method</i> {@link #getInstance(URL, JavaClient)}
	 * returns the instances of the class.
	 * 
	 * @param serviceURL The service {@code URL}
	 * @param javaClient The corresponding {@code JavaClient}
	 */
	private TaskServiceImpl(URL serviceURL, JavaClient javaClient)
	{
		super(TaskServiceID.INSTANCE, serviceURL, javaClient);

		restletClient = ((JavaClientImpl) javaClient).getRestletClient();

		ClientResource clientResource = new ClientResource(serviceURL.toExternalForm());
		clientResource.setNext(restletClient);
		TaskServiceInstanceDataResource taskServiceInstanceDataResource = clientResource.get(TaskServiceInstanceDataResource.class);
		
		System.out.println("[TaskService TaskServiceImpl] " + serviceURL.toExternalForm());
		
		maxTaskRequestSize = taskServiceInstanceDataResource.getParameters().getBatchLimit();
	}

	/**
	 * Returns a {@link TaskService} instance for the specified service
	 * {@link URL} and the corresponding {@link JavaClient}.
	 * 
	 * @param serviceURL The service {@code URL}
	 * @param javaClient The corresponding {@code JavaClient}
	 * @return A {@code TaskService} instance
	 */
	@InvocationLimited(legitimateInvocationClasses = {JavaClientImpl.class})
	public static synchronized TaskService getInstance(URL serviceURL, JavaClient javaClient)
	{
		serviceURL = URLTransformationUtils.adjustServiceURL(serviceURL);

		String warningMessage = "The method invocation \"" + TaskServiceImpl.class.getCanonicalName() + ".getInstance(URL, JavaClient)\" is not intended for this usage. Use the method \"" + JavaClient.class.getCanonicalName() + ".getService(ServicePluginID)\" instead.";
		InvocationUtils.checkMethodInvocation(warningMessage, "getInstance", URL.class, JavaClient.class);

		return new TaskServiceImpl(serviceURL, javaClient);
	}

	/**
	 * Returns the corresponding {@link DataManager}.
	 * 
	 * @return The corresponding {@code DataManager}
	 */
	private DataManager getDataManager()
	{
		if(dataManager == null)
		{
			dataManager = DataManagerImpl.getInstance(serviceURL, restletClient, javaClient.getSystemSession(), maxTaskRequestSize);
		}

		return dataManager;
	}

	@Override
	public TaskID createTask(UserSession userSession, String title, String description, BoundingBox boundingBox, TimeRange timeRange)
	{
		return getDataManager().createTask(userSession, title, description, boundingBox, timeRange);
	}

	@Override
	public ResultSet<TaskID, Task> getAllCachedTasks()
	{
		return getDataManager().getAllCachedTasks();
	}

	@Override
	public ResultSet<TaskID, Task> getAllTasks()
	{
		return getDataManager().getAllTasks();
	}

	@Override
	public ResultSet<TaskID, Task> getAllTasks(boolean loadCompletely)
	{
		return getDataManager().getAllTasks(loadCompletely);
	}

	@Override
	public int getMaxTaskRequestSize()
	{
		return getDataManager().getMaxTaskRequestSize();
	}

	@Override
	public Task getTask(TaskID taskID)
	{
		return getDataManager().getTask(taskID);
	}

	@Override
	public Task getTask(TaskID taskID, boolean loadCompletely, boolean forceAPICall)
	{
		return getDataManager().getTask(taskID, loadCompletely, forceAPICall);
	}

	@Override
	public ResultSet<TaskID, Task> getTasks(List<String> tags, BoundingBox boundingBox, TimeRange timeRange)
	{
		return getDataManager().getTasks(tags, boundingBox, timeRange);
	}

	@Override
	public ResultSet<TaskID, Task> getTasks(List<String> tags, BoundingBox boundingBox, TimeRange timeRange, boolean loadCompletely)
	{
		return getDataManager().getTasks(tags, boundingBox, timeRange, loadCompletely);
	}

	@Override
	public ResultSet<TaskID, Task> getTasks(OrderType orderType, BoundingBox boundingBox)
	{
		return getDataManager().getTasks(orderType, boundingBox);
	}

	@Override
	public ResultSet<TaskID, Task> getTasks(OrderType orderType, BoundingBox boundingBox, boolean loadCompletely)
	{
		return getDataManager().getTasks(orderType, boundingBox, loadCompletely);
	}

	@Override
	public ResultSet<TaskID, Task> getTasks(OrderType orderType, double locationLatitude, double locationLongitude)
	{
		return getDataManager().getTasks(orderType, locationLatitude, locationLongitude);
	}

	@Override
	public ResultSet<TaskID, Task> getTasks(OrderType orderType, double locationLatitude, double locationLongitude, boolean loadCompletely)
	{
		return getDataManager().getTasks(orderType, locationLatitude, locationLongitude, loadCompletely);
	}

	@Override
	public ResultSet<TaskID, Task> getTasksAfter(DateTime time)
	{
		return getDataManager().getTasksAfter(time);
	}

	@Override
	public ResultSet<TaskID, Task> getTasksAfter(DateTime time, boolean loadCompletely)
	{
		return getDataManager().getTasksAfter(time, loadCompletely);
	}

	@Override
	public ResultSet<TaskID, Task> getTasksByIDs(List<TaskID> taskIDs)
	{
		return getDataManager().getTasksByIDs(taskIDs);
	}

	@Override
	public ResultSet<TaskID, Task> getTasksByIDs(List<TaskID> taskIDs, boolean loadCompletely, boolean forceAPICall)
	{
		return getDataManager().getTasksByIDs(taskIDs, loadCompletely, forceAPICall);
	}

	@Override
	public boolean isCacheActivated()
	{
		return getDataManager().isCacheActivated();
	}
}
