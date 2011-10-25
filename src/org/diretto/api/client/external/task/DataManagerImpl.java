package org.diretto.api.client.external.task;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.diretto.api.client.base.characteristic.Cachable;
import org.diretto.api.client.base.characteristic.ResultSetManager;
import org.diretto.api.client.base.characteristic.VoteManager;
import org.diretto.api.client.base.data.BoundingBox;
import org.diretto.api.client.base.data.ResultSet;
import org.diretto.api.client.base.data.ResultSetFactory;
import org.diretto.api.client.base.data.ResultSetImpl;
import org.diretto.api.client.base.data.TimeRange;
import org.diretto.api.client.base.entities.Entity;
import org.diretto.api.client.base.entities.EntityID;
import org.diretto.api.client.base.entities.SubEntityID;
import org.diretto.api.client.base.exceptions.CacheNotActivatedException;
import org.diretto.api.client.base.exceptions.TooManyEntitiesRequestedException;
import org.diretto.api.client.base.types.LoadType;
import org.diretto.api.client.base.types.OrderType;
import org.diretto.api.client.base.types.VoteType;
import org.diretto.api.client.external.task.entities.Submission;
import org.diretto.api.client.external.task.entities.SubmissionID;
import org.diretto.api.client.external.task.entities.Task;
import org.diretto.api.client.external.task.entities.TaskID;
import org.diretto.api.client.main.core.entities.Comment;
import org.diretto.api.client.main.core.entities.CommentID;
import org.diretto.api.client.main.core.entities.DocumentID;
import org.diretto.api.client.main.core.entities.Tag;
import org.diretto.api.client.main.core.entities.TagID;
import org.diretto.api.client.session.SystemSession;
import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.util.URLTransformationUtils;
import org.joda.time.DateTime;
import org.restlet.Client;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

/**
 * This class is the implementation class of the {@link DataManager} interface.
 * 
 * @author Tobias Schlecht
 */
public final class DataManagerImpl implements DataManager, VoteManager, ResultSetManager<TaskID, Task>
{
	private final URL serviceURL;
	private final Client restletClient;
	private final SystemSession systemSession;
	private final int maxTaskRequestSize;

	private CacheManager cacheManager = null;
	private DataFactory dataFactory = null;
	private ResourceManager resourceManager = null;

	private final Cache cache;
	private final boolean cacheActivated;

	/**
	 * The constructor is {@code private} to have strict control what instances
	 * exist at any time. Instead of the constructor the {@code public}
	 * <i>static factory method</i>
	 * {@link #getInstance(URL, Client, SystemSession, int)} returns the
	 * instances of the class.
	 * 
	 * @param serviceURL The service {@code URL}
	 * @param restletClient The restlet {@link Client}
	 * @param systemSession The corresponding {@code SystemSession}
	 * @param maxTaskRequestSize The maximum value of {@code Task}s which can be
	 *        requested
	 */
	private DataManagerImpl(URL serviceURL, Client restletClient, SystemSession systemSession, int maxTaskRequestSize)
	{
		this.serviceURL = serviceURL;
		this.restletClient = restletClient;
		this.systemSession = systemSession;
		this.maxTaskRequestSize = maxTaskRequestSize;

		CacheManager cacheManager = getCacheManager();

		if(cacheManager.isCacheActivated())
		{
			cache = cacheManager.getCache();
		}
		else
		{
			cache = null;
		}

		cacheActivated = cacheManager.isCacheActivated();
	}

	/**
	 * Returns a {@link DataManager} instance for the specified service
	 * {@link URL}.
	 * 
	 * @param serviceURL The service {@code URL}
	 * @param restletClient The restlet {@link Client}
	 * @param systemSession The corresponding {@code SystemSession}
	 * @param maxTaskRequestSize The maximum value of {@code Task}s which can be
	 *        requested
	 * @return A {@code DataManager} instance
	 */
	static synchronized DataManager getInstance(URL serviceURL, Client restletClient, SystemSession systemSession, int maxTaskRequestSize)
	{
		serviceURL = URLTransformationUtils.adjustServiceURL(serviceURL);

		return new DataManagerImpl(serviceURL, restletClient, systemSession, maxTaskRequestSize);
	}

	/**
	 * Returns the corresponding {@link CacheManager}.
	 * 
	 * @return The corresponding {@code CacheManager}
	 */
	CacheManager getCacheManager()
	{
		if(cacheManager == null)
		{
			cacheManager = CacheManager.getInstance(this);
		}

		return cacheManager;
	}

	/**
	 * Returns the corresponding {@link DataFactory}.
	 * 
	 * @return The corresponding {@code DataFactory}
	 */
	DataFactory getDataFactory()
	{
		if(dataFactory == null)
		{
			dataFactory = DataFactory.getInstance(this);
		}

		return dataFactory;
	}

	/**
	 * Returns the corresponding restlet {@link Client}.
	 * 
	 * @return The corresponding restlet {@code Client}
	 */
	Client getResletClient()
	{
		return restletClient;
	}

	/**
	 * Returns the corresponding {@link ResourceManager}.
	 * 
	 * @return The corresponding {@code ResourceManager}
	 */
	ResourceManager getResourceManager()
	{
		if(resourceManager == null)
		{
			resourceManager = ResourceManager.getInstance(this);
		}

		return resourceManager;
	}

	/**
	 * Returns the corresponding service {@link URL}.
	 * 
	 * @return The corresponding service {@code URL}
	 */
	URL getServiceURL()
	{
		return serviceURL;
	}

	/**
	 * Returns the corresponding {@link SystemSession}.
	 * 
	 * @return The corresponding {@code SystemSession}
	 */
	SystemSession getSystemSession()
	{
		return systemSession;
	}

	@Override
	public TaskID createTask(UserSession userSession, String title, String description, BoundingBox relevantArea, TimeRange relevantTimeRange)
	{
		if(userSession == null || title == null || description == null || relevantArea == null || relevantTimeRange == null)
		{
			throw new NullPointerException();
		}
		else if(title.trim().equals("") || description.trim().equals(""))
		{
			throw new IllegalArgumentException();
		}

		TaskID taskID = getDataFactory().createTask(userSession, title, description, relevantArea, relevantTimeRange);

		if(cacheActivated)
		{
			getTask(taskID, true, true);
		}

		return taskID;
	}

	@Override
	public ResultSet<TaskID, Task> getAllCachedTasks()
	{
		if(cacheActivated)
		{
			LinkedHashMap<TaskID, Task> tasks = new LinkedHashMap<TaskID, Task>();

			Element element;
			Task task;

			for(Object key : cache.getKeysWithExpiryCheck())
			{
				element = cache.get(key);

				if(element != null)
				{
					task = (Task) element.getObjectValue();
					tasks.put(task.getID(), task);
				}
			}

			return ResultSetFactory.getResultSetInstance(null, systemSession, false, LoadType.METADATA, tasks, null);
		}
		else
		{
			throw new CacheNotActivatedException();
		}
	}

	@Override
	public ResultSet<TaskID, Task> getAllTasks()
	{
		return getAllTasks(false);
	}

	@Override
	public ResultSet<TaskID, Task> getAllTasks(boolean loadCompletely)
	{
		ResultSetImpl<TaskID, Task> resultSet = getDataFactory().getAllTasks(loadCompletely);

		putTasksIntoCache(resultSet);

		return resultSet;
	}

	@Override
	public int getMaxTaskRequestSize()
	{
		return maxTaskRequestSize;
	}

	@Override
	public LinkedHashMap<TaskID, Task> getNextPageData(ResultSetImpl<TaskID, Task> resultSet)
	{
		if(resultSet == null)
		{
			throw new NullPointerException();
		}

		LinkedHashMap<TaskID, Task> resultMap = getDataFactory().getNextPageData(resultSet);

		if(cacheActivated)
		{
			for(Task task : resultMap.values())
			{
				cache.put(new Element(task.getID(), task));
			}
		}

		return resultMap;
	}

	@Override
	public Task getTask(TaskID taskID)
	{
		return getTask(taskID, false, false);
	}

	@Override
	public Task getTask(TaskID taskID, boolean loadCompletely, boolean forceAPICall)
	{
		if(taskID == null)
		{
			throw new NullPointerException();
		}

		Task task;

		if(cacheActivated)
		{
			if(!forceAPICall)
			{
				Element element = cache.get(taskID);

				if(element != null)
				{
					task = (Task) element.getObjectValue();

					if(loadCompletely && !((Cachable) task).isCompletelyLoaded())
					{
						task = getDataFactory().getTask(taskID, loadCompletely);
					}
				}
				else
				{
					task = getDataFactory().getTask(taskID, loadCompletely);
				}
			}
			else
			{
				task = getDataFactory().getTask(taskID, loadCompletely);
			}

			if(task != null)
			{
				cache.put(new Element(task.getID(), task));
			}
		}
		else
		{
			task = getDataFactory().getTask(taskID, loadCompletely);
		}

		return task;
	}

	@Override
	public ResultSet<TaskID, Task> getTasks(List<String> tags, BoundingBox boundingBox, TimeRange timeRange)
	{
		return getTasks(tags, boundingBox, timeRange, false);
	}

	@Override
	public ResultSet<TaskID, Task> getTasks(List<String> tags, BoundingBox boundingBox, TimeRange timeRange, boolean loadCompletely)
	{
		if(tags == null || boundingBox == null || timeRange == null)
		{
			throw new NullPointerException();
		}

		ResultSetImpl<TaskID, Task> resultSet = getDataFactory().getTasks(tags, boundingBox, timeRange, loadCompletely);

		putTasksIntoCache(resultSet);

		return resultSet;
	}

	@Override
	public ResultSet<TaskID, Task> getTasks(OrderType orderType, BoundingBox boundingBox)
	{
		return getTasks(orderType, boundingBox, false);
	}

	@Override
	public ResultSet<TaskID, Task> getTasks(OrderType orderType, BoundingBox boundingBox, boolean loadCompletely)
	{
		if(orderType == null || boundingBox == null)
		{
			throw new NullPointerException();
		}

		ResultSetImpl<TaskID, Task> resultSet = getDataFactory().getTasks(orderType, boundingBox, loadCompletely);

		putTasksIntoCache(resultSet);

		return resultSet;
	}

	@Override
	public ResultSet<TaskID, Task> getTasks(OrderType orderType, double locationLatitude, double locationLongitude)
	{
		return getTasks(orderType, locationLatitude, locationLongitude, false);
	}

	@Override
	public ResultSet<TaskID, Task> getTasks(OrderType orderType, double locationLatitude, double locationLongitude, boolean loadCompletely)
	{
		if(orderType == null)
		{
			throw new NullPointerException();
		}

		ResultSetImpl<TaskID, Task> resultSet = getDataFactory().getTasks(orderType, locationLatitude, locationLongitude, loadCompletely);

		putTasksIntoCache(resultSet);

		return resultSet;
	}

	@Override
	public ResultSet<TaskID, Task> getTasksAfter(DateTime time)
	{
		return getTasksAfter(time, false);
	}

	@Override
	public ResultSet<TaskID, Task> getTasksAfter(DateTime time, boolean loadCompletely)
	{
		if(time == null)
		{
			throw new NullPointerException();
		}

		ResultSetImpl<TaskID, Task> resultSet = getDataFactory().getTasksAfter(time, loadCompletely);

		putTasksIntoCache(resultSet);

		return resultSet;
	}

	@Override
	public ResultSet<TaskID, Task> getTasksByIDs(List<TaskID> taskIDs)
	{
		return getTasksByIDs(taskIDs, false, false);
	}

	@Override
	public ResultSet<TaskID, Task> getTasksByIDs(List<TaskID> taskIDs, boolean loadCompletely, boolean forceAPICall)
	{
		LoadType loadType;

		if(loadCompletely)
		{
			loadType = LoadType.COMPLETE;
		}
		else
		{
			loadType = LoadType.METADATA;
		}

		LinkedHashMap<TaskID, Task> resultMap;

		if(taskIDs == null)
		{
			throw new NullPointerException();
		}
		else if(taskIDs.size() > maxTaskRequestSize)
		{
			throw new TooManyEntitiesRequestedException();
		}
		else if(taskIDs.size() == 0)
		{
			return ResultSetFactory.getResultSetInstance(null, systemSession, false, loadType, new LinkedHashMap<TaskID, Task>(), null);
		}
		else if(taskIDs.size() == 1)
		{
			resultMap = new LinkedHashMap<TaskID, Task>();
			resultMap.put(taskIDs.get(0), getTask(taskIDs.get(0), loadCompletely, forceAPICall));

			return ResultSetFactory.getResultSetInstance(null, systemSession, false, loadType, resultMap, null);
		}

		if(cacheActivated)
		{
			if(!forceAPICall)
			{
				resultMap = new LinkedHashMap<TaskID, Task>();
				LinkedHashMap<TaskID, Task> tempMap = new LinkedHashMap<TaskID, Task>();
				List<TaskID> missingTasks = new LinkedList<TaskID>();

				Task task;
				Element element;

				for(TaskID taskID : taskIDs)
				{
					element = cache.get(taskID);

					if(element != null)
					{
						task = (Task) element.getObjectValue();

						if(loadCompletely && !((Cachable) task).isCompletelyLoaded())
						{
							missingTasks.add(taskID);
						}
						else
						{
							tempMap.put(taskID, task);
						}
					}
					else
					{
						missingTasks.add(taskID);
					}
				}

				if(missingTasks.size() > 0)
				{
					tempMap.putAll(getDataFactory().getTasksByIDs(missingTasks, loadCompletely));

					for(TaskID taskID : taskIDs)
					{
						resultMap.put(taskID, tempMap.get(taskID));
					}
				}
				else
				{
					return ResultSetFactory.getResultSetInstance(null, systemSession, false, loadType, tempMap, null);
				}
			}
			else
			{
				resultMap = getDataFactory().getTasksByIDs(taskIDs, loadCompletely);
			}

			if(resultMap.size() > 0)
			{
				for(Task task : resultMap.values())
				{
					cache.put(new Element(task.getID(), task));
				}
			}
		}
		else
		{
			resultMap = getDataFactory().getTasksByIDs(taskIDs, loadCompletely);
		}

		return ResultSetFactory.getResultSetInstance(null, systemSession, false, loadType, resultMap, null);
	}

	@Override
	public VoteType getUserVote(UserSession userSession, EntityID entityID)
	{
		if(userSession == null || entityID == null)
		{
			throw new NullPointerException();
		}

		return getDataFactory().getUserVote(userSession, entityID);
	}

	@Override
	public boolean isCacheActivated()
	{
		return cacheActivated;
	}

	@Override
	public boolean removeUserVote(UserSession userSession, EntityID entityID)
	{
		if(userSession == null || entityID == null)
		{
			throw new NullPointerException();
		}

		boolean wasSuccessful = getDataFactory().removeUserVote(userSession, entityID);

		if(cacheActivated)
		{
			TaskID taskID;

			if(entityID instanceof SubEntityID<?, ?>)
			{
				@SuppressWarnings("unchecked")
				SubEntityID<TaskID, ?> subEntityID = (SubEntityID<TaskID, ?>) entityID;
				taskID = subEntityID.getRootID();

				getTask(taskID, true, true);
			}
			else
			{
				taskID = (TaskID) entityID;

				getTask(taskID, false, true);
			}
		}

		return wasSuccessful;
	}

	@Override
	public boolean setUserVote(UserSession userSession, VoteType voteType, EntityID entityID)
	{
		if(userSession == null || voteType == null || entityID == null)
		{
			throw new NullPointerException();
		}

		boolean wasSuccessful = getDataFactory().setUserVote(userSession, voteType, entityID);

		if(cacheActivated)
		{
			TaskID taskID;

			if(entityID instanceof SubEntityID<?, ?>)
			{
				@SuppressWarnings("unchecked")
				SubEntityID<TaskID, ?> subEntityID = (SubEntityID<TaskID, ?>) entityID;
				taskID = subEntityID.getRootID();

				getTask(taskID, true, true);
			}
			else
			{
				taskID = (TaskID) entityID;

				getTask(taskID, false, true);
			}
		}

		return wasSuccessful;
	}

	/**
	 * Adds a new {@link Comment} to a {@link Task} and returns the
	 * {@link CommentID} if it was successful.
	 * 
	 * @param userSession The corresponding {@code UserSession}
	 * @param taskID The corresponding {@code TaskID}
	 * @param content The content of the {@code Comment}
	 * @return The corresponding {@code CommentID}
	 */
	public CommentID addCommentToTask(UserSession userSession, TaskID taskID, String content)
	{
		if(userSession == null || taskID == null || content == null)
		{
			throw new NullPointerException();
		}
		else if(content.trim().equals(""))
		{
			throw new IllegalArgumentException();
		}

		CommentID commentID = getDataFactory().addCommentToTask(userSession, taskID, content);

		if(cacheActivated)
		{
			getTask(taskID, true, true);
		}

		return commentID;
	}

	/**
	 * Adds a new {@link Submission} to a {@link Task} and returns the
	 * {@link SubmissionID} if it was successful.
	 * 
	 * @param userSession The corresponding {@code UserSession}
	 * @param taskID The corresponding {@code TaskID}
	 * @param documentID A {@code DocumentID}
	 * @return The corresponding {@code SubmissionID}
	 */
	public SubmissionID addSubmissionToTask(UserSession userSession, TaskID taskID, DocumentID documentID)
	{
		if(userSession == null || taskID == null || documentID == null)
		{
			throw new NullPointerException();
		}

		SubmissionID submissionID = getDataFactory().addSubmissionToTask(userSession, taskID, documentID);

		if(cacheActivated)
		{
			getTask(taskID, true, true);
		}

		return submissionID;
	}

	/**
	 * Adds a new {@link Tag} to an {@link Entity} and returns the {@link TagID}
	 * if it was successful.
	 * 
	 * @param userSession The corresponding {@code UserSession}
	 * @param entityID The corresponding {@code EntityID}
	 * @param value The value of the {@code Tag}
	 * @return The corresponding {@code TagID}
	 */
	public TagID addTagToEntity(UserSession userSession, EntityID entityID, String value)
	{
		if(userSession == null || entityID == null || value == null)
		{
			throw new NullPointerException();
		}
		else if(value.trim().equals(""))
		{
			throw new IllegalArgumentException();
		}

		TagID tagID = getDataFactory().addTagToEntity(userSession, entityID, value);

		if(cacheActivated)
		{
			TaskID taskID;

			if(entityID instanceof SubEntityID<?, ?>)
			{
				@SuppressWarnings("unchecked")
				SubEntityID<TaskID, ?> subEntityID = (SubEntityID<TaskID, ?>) entityID;
				taskID = subEntityID.getRootID();
			}
			else
			{
				taskID = (TaskID) entityID;
			}

			getTask(taskID, true, true);
		}

		return tagID;
	}

	/**
	 * Checks whether the {@code Cache} is activated and in case it is
	 * activated, all {@link Task}s of the given {@link ResultSet}, which are
	 * loaded for the present time, will be put in the {@code Cache}.
	 * 
	 * @param resultSet The {@code ResultSet} with the {@code Task}s
	 */
	private void putTasksIntoCache(ResultSetImpl<TaskID, Task> resultSet)
	{
		if(cacheActivated)
		{
			for(Task task : resultSet.getLoadedData())
			{
				cache.put(new Element(task.getID(), task));
			}
		}
	}
}
