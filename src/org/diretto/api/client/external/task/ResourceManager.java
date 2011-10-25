package org.diretto.api.client.external.task;

import java.net.URL;

import org.diretto.api.client.base.data.BoundingBox;
import org.diretto.api.client.base.entities.Entity;
import org.diretto.api.client.base.entities.EntityID;
import org.diretto.api.client.base.entities.SubEntityID;
import org.diretto.api.client.base.exceptions.NoResultsException;
import org.diretto.api.client.base.types.OrderType;
import org.diretto.api.client.base.types.VoteType;
import org.diretto.api.client.external.task.binding.major.MultipleMetaDataTasksResource;
import org.diretto.api.client.external.task.binding.major.MultipleSnapShotTasksResource;
import org.diretto.api.client.external.task.binding.major.QueryResultPageResource;
import org.diretto.api.client.external.task.binding.major.ResultPageResource;
import org.diretto.api.client.external.task.binding.major.TaskSnapShotResource;
import org.diretto.api.client.external.task.binding.major.TaskMetaDataResource;
import org.diretto.api.client.external.task.binding.post.DispatchQueryResource;
import org.diretto.api.client.external.task.binding.post.MultipleTasksRequestResource;
import org.diretto.api.client.external.task.binding.post.SubmissionCreationResource;
import org.diretto.api.client.external.task.binding.post.TaskCreationResource;
import org.diretto.api.client.external.task.entities.Submission;
import org.diretto.api.client.external.task.entities.SubmissionID;
import org.diretto.api.client.external.task.entities.Task;
import org.diretto.api.client.external.task.entities.TaskServiceEntityIDFactory;
import org.diretto.api.client.external.task.entities.TaskID;
import org.diretto.api.client.main.core.binding.major.BaseTagResource;
import org.diretto.api.client.main.core.binding.major.MultipleTagsResource;
import org.diretto.api.client.main.core.binding.major.UserVoteResource;
import org.diretto.api.client.main.core.binding.post.CommentCreationResource;
import org.diretto.api.client.main.core.binding.post.MultipleValuesRequestResource;
import org.diretto.api.client.main.core.binding.post.TagCreationResource;
import org.diretto.api.client.main.core.entities.Comment;
import org.diretto.api.client.main.core.entities.CommentID;
import org.diretto.api.client.main.core.entities.CoreServiceEntityIDFactory;
import org.diretto.api.client.main.core.entities.Tag;
import org.diretto.api.client.main.core.entities.TagID;
import org.diretto.api.client.main.core.entities.Time;
import org.diretto.api.client.session.Session;
import org.diretto.api.client.session.SystemSession;
import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.user.User;
import org.diretto.api.client.user.UserFactory;
import org.joda.time.DateTime;
import org.restlet.Client;
import org.restlet.data.Reference;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

/**
 * The {@code ResourceManager} is a manager class and responsible for the
 * communication with the API. For the simplification of the communication
 * <i>Restlet</i> (RESTful web framework for Java) is used.
 * 
 * @author Tobias Schlecht
 */
final class ResourceManager
{
	private final URL serviceURL;
	private final Client restletClient;
	private final SystemSession systemSession;

	/**
	 * The constructor is {@code private} to have strict control what instances
	 * exist at any time. Instead of the constructor the {@code public}
	 * <i>static factory method</i> {@link #getInstance(DataManagerImpl)}
	 * returns the instances of the class.
	 * 
	 * @param dataManager The corresponding {@code DataManager}
	 */
	private ResourceManager(DataManagerImpl dataManager)
	{
		serviceURL = dataManager.getServiceURL();
		restletClient = dataManager.getResletClient();
		systemSession = dataManager.getSystemSession();
	}

	/**
	 * Returns a {@link ResourceManager} instance for the corresponding
	 * {@link DataManager}.
	 * 
	 * @param dataManager The corresponding {@code DataManager}
	 * @return A {@code ResourceManager} instance
	 */
	static synchronized ResourceManager getInstance(DataManagerImpl dataManager)
	{
		return new ResourceManager(dataManager);
	}

	/**
	 * Adds a new {@link Comment} to a {@link Task} and returns the
	 * {@link CommentID} if it was successful.
	 * 
	 * @param userSession The corresponding {@code UserSession}
	 * @param taskID The corresponding {@code TaskID}
	 * @param commentCreationResource The {@code CommentCreationResource}
	 * @return The corresponding {@code CommentID}
	 */
	CommentID addCommentToTask(UserSession userSession, TaskID taskID, CommentCreationResource commentCreationResource)
	{
		ClientResource clientResource = createClientResource(userSession, taskID.getUniqueResourceURL(), "comments");

		try
		{
			clientResource.post(commentCreationResource);
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}

		return CoreServiceEntityIDFactory.getCommentIDInstance(clientResource.getResponse().getLocationRef().toUrl(), taskID, taskID);
	}

	/**
	 * Adds a new {@link Submission} to a {@link Task} and returns the
	 * {@link SubmissionID} if it was successful.
	 * 
	 * @param userSession The corresponding {@code UserSession}
	 * @param taskID The corresponding {@code TaskID}
	 * @param submissionCreationResource The {@code SubmissionCreationResource}
	 * @return The corresponding {@code SubmissionID}
	 */
	SubmissionID addSubmissionToTask(UserSession userSession, TaskID taskID, SubmissionCreationResource submissionCreationResource)
	{
		ClientResource clientResource = createClientResource(userSession, taskID.getUniqueResourceURL(), "submissions");

		try
		{
			clientResource.post(submissionCreationResource);
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}

		return TaskServiceEntityIDFactory.getSubmissionIDInstance(clientResource.getResponse().getLocationRef().toUrl(), taskID, taskID);
	}

	/**
	 * Adds a new {@link Tag} to an {@link Entity} and returns the {@link TagID}
	 * if it was successful.
	 * 
	 * @param userSession The corresponding {@code UserSession}
	 * @param entityID The corresponding {@code EntityID}
	 * @param tagCreationResource The {@code TagCreationResource}
	 * @return The corresponding {@code TagID}
	 */
	TagID addTagToEntity(UserSession userSession, EntityID entityID, TagCreationResource tagCreationResource)
	{
		ClientResource clientResource = createClientResource(userSession, "tags");

		BaseTagResource baseTagResource;

		try
		{
			baseTagResource = clientResource.post(tagCreationResource, BaseTagResource.class);
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}

		clientResource = createClientResource(userSession, entityID.getUniqueResourceURL(), "tags");

		try
		{
			clientResource.post(baseTagResource);
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}

		if(entityID instanceof SubEntityID<?, ?>)
		{
			@SuppressWarnings("unchecked")
			SubEntityID<TaskID, ?> subEntityID = (SubEntityID<TaskID, ?>) entityID;

			return CoreServiceEntityIDFactory.getTagIDInstance(clientResource.getResponse().getLocationRef().toUrl(), subEntityID.getRootID(), subEntityID);
		}
		else
		{
			return CoreServiceEntityIDFactory.getTagIDInstance(clientResource.getResponse().getLocationRef().toUrl(), entityID, entityID);
		}
	}

	/**
	 * Creates a new {@link Task} and returns the {@link TaskID} if it was
	 * successful.
	 * 
	 * @param userSession The corresponding {@code UserSession}
	 * @param taskCreationResource The {@code TaskCreationResource}
	 * @return The corresponding {@code TaskID}
	 */
	TaskID createTask(UserSession userSession, TaskCreationResource taskCreationResource)
	{
		ClientResource clientResource = createClientResource(userSession, "tasks");

		try
		{
			clientResource.post(taskCreationResource);
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}

		return TaskServiceEntityIDFactory.getTaskIDInstance(clientResource.getResponse().getLocationRef().toUrl());
	}

	/**
	 * Executes a query with the given {@link DispatchQueryResource} and returns
	 * the corresponding {@link QueryResultPageResource}.
	 * 
	 * @param dispatchQueryResource The {@code DispatchQueryResource}
	 * @return The {@code QueryResultPageResource}
	 * @throws NoResultsException
	 */
	QueryResultPageResource executeQuery(DispatchQueryResource dispatchQueryResource) throws NoResultsException
	{
		ClientResource clientResource = createClientResource("query");

		try
		{
			clientResource.post(dispatchQueryResource);
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}

		return getQueryResultPageResource(clientResource);
	}

	/**
	 * Executes a query with the given parameters and returns the corresponding
	 * {@link QueryResultPageResource}.
	 * 
	 * @param orderType An {@code OrderType}
	 * @param boundingBox A {@code BoundingBox}
	 * @return The {@code QueryResultPageResource}
	 * @throws NoResultsException
	 */
	QueryResultPageResource executeQuery(OrderType orderType, BoundingBox boundingBox) throws NoResultsException
	{
		ClientResource clientResource = createClientResource("query", "common", orderType.getURLParameter() + "?lat1=" + boundingBox.getLowerLeftLatitude() + "&lon1=" + boundingBox.getLowerLeftLongitude() + "&lat2=" + boundingBox.getUpperRightLatitude() + "&lon2=" + boundingBox.getUpperRightLongitude());

		try
		{
			clientResource.get();
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}

		return getQueryResultPageResource(clientResource);
	}

	/**
	 * Executes a query with the given parameters and returns the corresponding
	 * {@link QueryResultPageResource}.
	 * 
	 * @param orderType An {@code OrderType}
	 * @param locationLatitude The latitude of the location in degrees
	 * @param locationLongitude The longitude of the location in degrees
	 * @return The {@code QueryResultPageResource}
	 * @throws NoResultsException
	 */
	QueryResultPageResource executeQuery(OrderType orderType, double locationLatitude, double locationLongitude) throws NoResultsException
	{
		ClientResource clientResource = createClientResource("query", "common", orderType.getURLParameter() + "?lat=" + locationLatitude + "&lon=" + locationLongitude);

		try
		{
			clientResource.get();
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}

		return getQueryResultPageResource(clientResource);
	}

	/**
	 * Returns the corresponding {@link ResultPageResource}.
	 * 
	 * @return The {@code ResultPageResource}
	 * @throws NoResultsException
	 */
	ResultPageResource getAllTasks() throws NoResultsException
	{
		ClientResource clientResource = createClientResource("tasks");

		return getResultPageResource(clientResource);
	}

	/**
	 * Returns the corresponding {@link TaskMetaDataResource}.
	 * 
	 * @param taskID A {@code TaskID}
	 * @return The {@code TaskMetaDataResource}
	 */
	TaskMetaDataResource getMetaDataTask(TaskID taskID)
	{
		ClientResource clientResource = createClientResource(taskID.getUniqueResourceURL());

		try
		{
			return clientResource.get(TaskMetaDataResource.class);
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}
	}

	/**
	 * Returns the corresponding {@link MultipleMetaDataTasksResource}.
	 * 
	 * @param multipleTasksRequestResource The
	 *        {@code MultipleTasksRequestResource}
	 * @return The {@code MultipleMetaDataTasksResource}
	 */
	MultipleMetaDataTasksResource getMultipleMetaDataTasks(MultipleTasksRequestResource multipleTasksRequestResource)
	{
		ClientResource clientResource = createClientResource("tasks", "metadata");

		try
		{
			return clientResource.post(multipleTasksRequestResource, MultipleMetaDataTasksResource.class);
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}
	}

	/**
	 * Returns the corresponding {@link MultipleSnapShotTasksResource}.
	 * 
	 * @param multipleTasksRequestResource The
	 *        {@code MultipleTasksRequestResource}
	 * @return The {@code MultipleSnapShotTasksResource}
	 */
	MultipleSnapShotTasksResource getMultipleSnapShotTasks(MultipleTasksRequestResource multipleTasksRequestResource)
	{
		ClientResource clientResource = createClientResource("tasks", "snapshots");

		try
		{
			return clientResource.post(multipleTasksRequestResource, MultipleSnapShotTasksResource.class);
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}
	}

	/**
	 * Returns the corresponding {@link MultipleTagsResource}.
	 * 
	 * @param multipleValuesRequestResource The
	 *        {@code MultipleValuesRequestResource}
	 * @return The {@code MultipleTagsResource}
	 */
	MultipleTagsResource getMultipleTags(MultipleValuesRequestResource multipleValuesRequestResource)
	{
		ClientResource clientResource = createClientResource("tags", "multiple");

		try
		{
			return clientResource.post(multipleValuesRequestResource, MultipleTagsResource.class);
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}
	}

	/**
	 * Returns the {@link QueryResultPageResource} for the given query result
	 * page {@link URL}.
	 * 
	 * @param pageURL The {@code URL} of the query result page
	 * @return The {@code QueryResultPageResource}
	 */
	QueryResultPageResource getQueryResultPageResource(URL pageURL)
	{
		ClientResource clientResource = createClientResource(new Reference(pageURL));

		try
		{
			return clientResource.get(QueryResultPageResource.class);
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}
	}

	/**
	 * Returns the {@link ResultPageResource} for the given result page
	 * {@link URL}.
	 * 
	 * @param pageURL The {@code URL} of the result page
	 * @return The {@code ResultPageResource}
	 */
	ResultPageResource getResultPage(URL pageURL)
	{
		ClientResource clientResource = createClientResource(new Reference(pageURL));

		try
		{
			return clientResource.get(ResultPageResource.class);
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}
	}

	/**
	 * Returns the corresponding {@link TaskSnapShotResource}.
	 * 
	 * @param taskID A {@code TaskID}
	 * @return The {@code TaskSnapShotResource}
	 */
	TaskSnapShotResource getSnapShotTask(TaskID taskID)
	{
		ClientResource clientResource = createClientResource(taskID.getUniqueResourceURL(), "snapshot");

		try
		{
			return clientResource.get(TaskSnapShotResource.class);
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}
	}

	/**
	 * Returns the corresponding {@link ResultPageResource}.
	 * 
	 * @param time The {@code DateTime} after which the {@code Task}s should be
	 *        returned
	 * @return The {@code ResultPageResource}
	 * @throws NoResultsException
	 */
	ResultPageResource getTasksAfter(DateTime time) throws NoResultsException
	{
		ClientResource clientResource = createClientResource("tasks", "since", time.toString(Time.ISO_UTC_DATE_TIME_FORMATTER));

		return getResultPageResource(clientResource);
	}

	/**
	 * Returns the current {@link VoteType} of the {@link User}.
	 * 
	 * @param userSession The corresponding {@code UserSession}
	 * @param entityID An {@code EntityID}
	 * @return The {@code UserVoteResource}
	 */
	UserVoteResource getUserVote(UserSession userSession, EntityID entityID)
	{
		ClientResource clientResource = createClientResource(userSession, entityID.getUniqueResourceURL(), "vote", "user", userSession.getUser().getAuthID());

		try
		{
			return clientResource.get(UserVoteResource.class);
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}
	}

	/**
	 * Removes the vote of the {@link User} if the {@code User} has been voted
	 * before.
	 * 
	 * @param userSession The corresponding {@code UserSession}
	 * @param entityID An {@code EntityID}
	 * @return {@code true} if the removal was successful; otherwise
	 *         {@code false}
	 */
	boolean removeUserVote(UserSession userSession, EntityID entityID)
	{
		ClientResource clientResource = createClientResource(userSession, entityID.getUniqueResourceURL(), "vote", "user", userSession.getUser().getAuthID());

		try
		{
			clientResource.delete();
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return false;
		}

		return true;
	}

	/**
	 * Sets the given {@link VoteType} of the {@link User}. If the {@code User}
	 * has already been voted with another {@code VoteType}, the
	 * {@code VoteType} will be changed.
	 * 
	 * @param userSession The corresponding {@code UserSession}
	 * @param voteType The {@code VoteType} of the {@code User}
	 * @param entityID An {@code EntityID}
	 * @return {@code true} if the voting was successful; otherwise
	 *         {@code false}
	 */
	boolean setUserVote(UserSession userSession, VoteType voteType, EntityID entityID)
	{
		ClientResource clientResource = createClientResource(userSession, entityID.getUniqueResourceURL(), "vote", "user", userSession.getUser().getAuthID(), voteType.getURLParameter());

		try
		{
			clientResource.put(null);
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return false;
		}

		return true;
	}

	/**
	 * Builds a {@code String} representation of the target {@code URL} with the
	 * specified target {@code URL} parts. <br/><br/>
	 * 
	 * <i>Annotation:</i> The parts are preceded by the given
	 * {@code resourceURL}. If the {@code resourceURL} is {@code null}, the
	 * parts are preceded by the service {@code URL}.
	 * 
	 * @param resourceURL The corresponding resource {@code URL}
	 * @param parts The target {@code URL} parts
	 * @return A {@code String} representation of the target {@code URL}
	 */
	private String buildURL(URL resourceURL, String... parts)
	{
		StringBuilder url;

		if(resourceURL == null)
		{
			url = new StringBuilder(serviceURL.toExternalForm());
		}
		else
		{
			url = new StringBuilder(resourceURL.toExternalForm());
		}

		for(String part : parts)
		{
			url.append("/");
			url.append(part);
		}

		System.out.println("[TaskService ResourceManager] " + url.toString());

		return url.toString();
	}

	/**
	 * Creates a {@link ClientResource} with the specified target
	 * {@link Reference}.
	 * 
	 * @param reference The target {@code Reference}
	 * @return The {@code ClientResource}
	 */
	private ClientResource createClientResource(Reference reference)
	{
		ClientResource clientResource = new ClientResource(reference);
		handleClientResource(systemSession, clientResource);

		System.out.println("[TaskService ResourceManager] " + reference.toString());

		return clientResource;
	}

	/**
	 * Creates a {@link ClientResource} with the specified target {@code URL}
	 * parts. <br/><br/>
	 * 
	 * <i>Annotation:</i> The parts are preceded by the service {@code URL}. For
	 * example if the parts {@code "xyz"} and {@code "15"} are delivered and the
	 * service {@code URL} is {@code http://example.diretto.org/v2} the result
	 * for the target {@code URL} is
	 * {@code http://example.diretto.org/v2/xyz/15}.
	 * 
	 * @param parts The target {@code URL} parts
	 * @return The {@code ClientResource}
	 */
	private ClientResource createClientResource(String... parts)
	{
		ClientResource clientResource = new ClientResource(buildURL(null, parts));
		handleClientResource(systemSession, clientResource);

		return clientResource;
	}

	/**
	 * Creates a {@link ClientResource} with the specified target {@code URL}
	 * parts. <br/><br/>
	 * 
	 * <i>Annotation:</i> The parts are preceded by the given
	 * {@code resourceURL}. For example if the parts {@code "xyz"} and
	 * {@code "15"} are delivered and the {@code resourceURL} is
	 * {@code http://example.diretto.org/v2} the result for the target
	 * {@code URL} is {@code http://example.diretto.org/v2/xyz/15}.
	 * 
	 * @param resourceURL The corresponding resource {@code URL}
	 * @param parts The target {@code URL} parts
	 * @return The {@code ClientResource}
	 */
	private ClientResource createClientResource(URL resourceURL, String... parts)
	{
		ClientResource clientResource = new ClientResource(buildURL(resourceURL, parts));
		handleClientResource(systemSession, clientResource);

		return clientResource;
	}

	/**
	 * Creates a {@link ClientResource} with the specified target {@code URL}
	 * parts. <br/><br/>
	 * 
	 * <i>Annotation:</i> The parts are preceded by the service {@code URL}. For
	 * example if the parts {@code "xyz"} and {@code "15"} are delivered and the
	 * service {@code URL} is {@code http://example.diretto.org/v2} the result
	 * for the target {@code URL} is
	 * {@code http://example.diretto.org/v2/xyz/15}.
	 * 
	 * @param userSession The corresponding {@code UserSession}
	 * @param parts The target {@code URL} parts
	 * @return The {@code ClientResource}
	 */
	private ClientResource createClientResource(UserSession userSession, String... parts)
	{
		ClientResource clientResource = new ClientResource(buildURL(null, parts));
		handleClientResource(userSession, clientResource);

		return clientResource;
	}

	/**
	 * Creates a {@link ClientResource} with the specified target {@code URL}
	 * parts. <br/><br/>
	 * 
	 * <i>Annotation:</i> The parts are preceded by the given
	 * {@code resourceURL}. For example if the parts {@code "xyz"} and
	 * {@code "15"} are delivered and the {@code resourceURL} is
	 * {@code http://example.diretto.org/v2} the result for the target
	 * {@code URL} is {@code http://example.diretto.org/v2/xyz/15}.
	 * 
	 * @param userSession The corresponding {@code UserSession}
	 * @param resourceURL The corresponding resource {@code URL}
	 * @param parts The target {@code URL} parts
	 * @return The {@code ClientResource}
	 */
	private ClientResource createClientResource(UserSession userSession, URL resourceURL, String... parts)
	{
		ClientResource clientResource = new ClientResource(buildURL(resourceURL, parts));
		handleClientResource(userSession, clientResource);

		return clientResource;
	}

	/**
	 * Returns the {@link QueryResultPageResource} for the given
	 * {@link ClientResource}.
	 * 
	 * @param clientResource The {@code ClientResource}
	 * @return The {@code QueryResultPageResource}
	 * @throws NoResultsException
	 */
	private QueryResultPageResource getQueryResultPageResource(ClientResource clientResource) throws NoResultsException
	{
		if(clientResource.getResponse().getStatus().getCode() == 202)
		{
			clientResource = createClientResource((clientResource.getResponse().getLocationRef()));

			try
			{
				QueryResultPageResource queryResultPageResource = clientResource.get(QueryResultPageResource.class);

				if(clientResource.getResponse().getStatus().getCode() == 204)
				{
					throw new NoResultsException();
				}

				return queryResultPageResource;
			}
			catch(ResourceException e)
			{
				System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the {@link ResultPageResource} for the given
	 * {@link ClientResource}.
	 * 
	 * @param clientResource The {@code ClientResource}
	 * @return The {@code ResultPageResource}
	 * @throws NoResultsException
	 */
	private ResultPageResource getResultPageResource(ClientResource clientResource) throws NoResultsException
	{
		try
		{
			ResultPageResource resultPageResource = clientResource.get(ResultPageResource.class);

			if(clientResource.getResponse().getStatus().getCode() == 204)
			{
				throw new NoResultsException();
			}

			return resultPageResource;
		}
		catch(ResourceException e)
		{
			System.err.println("[TaskService ResourceManager] " + e.getStatus().getCode());
			return null;
		}
	}

	/**
	 * Handles the given {@link ClientResource}.
	 * 
	 * @param session The corresponding {@code Session}
	 * @param clientResource The {@code ClientResource} to be handled
	 */
	private void handleClientResource(Session session, ClientResource clientResource)
	{
		clientResource.setNext(restletClient);

		if(session != null)
		{
			UserFactory.authenticateClientResource(session.getUser(), clientResource);
		}
	}
}
