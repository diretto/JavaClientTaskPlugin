package org.diretto.api.client.external.task;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.diretto.api.client.base.data.BoundingBox;
import org.diretto.api.client.base.data.ResultSet;
import org.diretto.api.client.base.data.ResultSetFactory;
import org.diretto.api.client.base.data.ResultSetImpl;
import org.diretto.api.client.base.data.TimeRange;
import org.diretto.api.client.base.data.Votes;
import org.diretto.api.client.base.entities.Entity;
import org.diretto.api.client.base.entities.EntityID;
import org.diretto.api.client.base.exceptions.NoResultsException;
import org.diretto.api.client.base.types.LoadType;
import org.diretto.api.client.base.types.OrderType;
import org.diretto.api.client.base.types.VoteType;
import org.diretto.api.client.external.task.binding.entities.SubmissionResource;
import org.diretto.api.client.external.task.binding.entities.TaskResource;
import org.diretto.api.client.external.task.binding.major.QueryResultPageResource;
import org.diretto.api.client.external.task.binding.major.ResultPageResource;
import org.diretto.api.client.external.task.binding.major.TaskMetaDataResource;
import org.diretto.api.client.external.task.binding.major.TaskSnapShotResource;
import org.diretto.api.client.external.task.binding.post.DispatchQueryResource;
import org.diretto.api.client.external.task.binding.post.MultipleTasksRequestResource;
import org.diretto.api.client.external.task.binding.post.SubmissionCreationResource;
import org.diretto.api.client.external.task.binding.post.TaskCreationResource;
import org.diretto.api.client.external.task.binding.resources.ConstraintsResource;
import org.diretto.api.client.external.task.binding.resources.PageResource;
import org.diretto.api.client.external.task.binding.resources.QueryResource;
import org.diretto.api.client.external.task.binding.resources.SubmissionsResource;
import org.diretto.api.client.external.task.binding.resources.TaskHyperLinkResource;
import org.diretto.api.client.external.task.entities.Submission;
import org.diretto.api.client.external.task.entities.SubmissionID;
import org.diretto.api.client.external.task.entities.Task;
import org.diretto.api.client.external.task.entities.TaskBuilder;
import org.diretto.api.client.external.task.entities.TaskServiceEntityIDFactory;
import org.diretto.api.client.external.task.entities.TaskID;
import org.diretto.api.client.external.task.entities.SubmissionBuilder;
import org.diretto.api.client.main.core.DataManager;
import org.diretto.api.client.main.core.binding.entities.CommentResource;
import org.diretto.api.client.main.core.binding.entities.TagResource;
import org.diretto.api.client.main.core.binding.major.BaseTagResource;
import org.diretto.api.client.main.core.binding.major.MultipleTagsResource;
import org.diretto.api.client.main.core.binding.major.UserVoteResource;
import org.diretto.api.client.main.core.binding.post.CommentCreationResource;
import org.diretto.api.client.main.core.binding.post.MultipleValuesRequestResource;
import org.diretto.api.client.main.core.binding.post.TagCreationResource;
import org.diretto.api.client.main.core.binding.resources.CommentsResource;
import org.diretto.api.client.main.core.binding.resources.HyperLinkResource;
import org.diretto.api.client.main.core.binding.resources.HyperLinkResourceWrapper;
import org.diretto.api.client.main.core.binding.resources.BoundingBoxResource;
import org.diretto.api.client.main.core.binding.resources.TagsResource;
import org.diretto.api.client.main.core.binding.resources.TimeRangeResource;
import org.diretto.api.client.main.core.entities.Comment;
import org.diretto.api.client.main.core.entities.CommentBuilder;
import org.diretto.api.client.main.core.entities.CommentID;
import org.diretto.api.client.main.core.entities.CoreServiceEntityIDFactory;
import org.diretto.api.client.main.core.entities.DocumentID;
import org.diretto.api.client.main.core.entities.Tag;
import org.diretto.api.client.main.core.entities.TagBuilder;
import org.diretto.api.client.main.core.entities.TagID;
import org.diretto.api.client.main.core.entities.Time;
import org.diretto.api.client.session.SystemSession;
import org.diretto.api.client.session.UserSession;
import org.diretto.api.client.user.UserFactory;
import org.diretto.api.client.user.UserID;
import org.joda.time.DateTime;

/**
 * The {@code DataFactory} is a manager class and particularly responsible for
 * the transformation between {@link Entity} objects and the POJO based
 * {@code Resource} classes. Thus it serves as interface between the
 * {@link DataManager} and the {@link ResourceManager}.
 * 
 * @author Tobias Schlecht
 */
final class DataFactory
{
	private final DataManagerImpl dataManager;
	private final ResourceManager resourceManager;

	private final SystemSession systemSession;

	/**
	 * The constructor is {@code private} to have strict control what instances
	 * exist at any time. Instead of the constructor the {@code public}
	 * <i>static factory method</i> {@link #getInstance(DataManagerImpl)}
	 * returns the instances of the class.
	 * 
	 * @param dataManager The corresponding {@code DataManager}
	 */
	private DataFactory(DataManagerImpl dataManager)
	{
		this.dataManager = dataManager;
		resourceManager = dataManager.getResourceManager();
		systemSession = dataManager.getSystemSession();
	}

	/**
	 * Returns a {@link DataFactory} instance for the corresponding
	 * {@link DataManager}.
	 * 
	 * @param dataManager The corresponding {@code DataManager}
	 * @return A {@code DataFactory} instance
	 */
	static synchronized DataFactory getInstance(DataManagerImpl dataManager)
	{
		return new DataFactory(dataManager);
	}

	/**
	 * @see DataManagerImpl#addCommentToTask(UserSession, TaskID, String)
	 */
	CommentID addCommentToTask(UserSession userSession, TaskID taskID, String content)
	{
		CommentCreationResource commentCreationResource = new CommentCreationResource();

		commentCreationResource.setContent(content);

		return resourceManager.addCommentToTask(userSession, taskID, commentCreationResource);
	}

	/**
	 * @see DataManagerImpl#addSubmissionToTask(UserSession, TaskID, DocumentID)
	 */
	SubmissionID addSubmissionToTask(UserSession userSession, TaskID taskID, DocumentID documentID)
	{
		SubmissionCreationResource submissionCreationResource = new SubmissionCreationResource();
		HyperLinkResourceWrapper document = new HyperLinkResourceWrapper();
		HyperLinkResource hyperLink = new HyperLinkResource();

		hyperLink.setRel("self");
		hyperLink.setHref(documentID.getUniqueResourceURL().toExternalForm());

		document.setLink(hyperLink);

		submissionCreationResource.setDocument(document);

		return resourceManager.addSubmissionToTask(userSession, taskID, submissionCreationResource);
	}

	/**
	 * @see DataManagerImpl#addTagToEntity(UserSession, EntityID, String)
	 */
	TagID addTagToEntity(UserSession userSession, EntityID entityID, String value)
	{
		TagCreationResource tagCreationResource = new TagCreationResource();

		tagCreationResource.setValue(value);

		return resourceManager.addTagToEntity(userSession, entityID, tagCreationResource);
	}

	/**
	 * @see DataManagerImpl#createTask(UserSession, String, String, BoundingBox,
	 *      TimeRange)
	 */
	TaskID createTask(UserSession userSession, String title, String description, BoundingBox relevantArea, TimeRange relevantTimeRange)
	{
		TaskCreationResource taskCreationResource = new TaskCreationResource();
		ConstraintsResource constraintsResource = new ConstraintsResource();
		TimeRangeResource timeRangeResource = new TimeRangeResource();
		BoundingBoxResource boundingBoxResource = new BoundingBoxResource();

		timeRangeResource.setStart(relevantTimeRange.getStartDateTime().toString(Time.ISO_UTC_DATE_TIME_FORMATTER));
		timeRangeResource.setEnd(relevantTimeRange.getEndDateTime().toString(Time.ISO_UTC_DATE_TIME_FORMATTER));
		boundingBoxResource.setBbox(relevantArea.getArrayList(1, 0, 3, 2));
		constraintsResource.setTime(timeRangeResource);
		constraintsResource.setLocation(boundingBoxResource);

		taskCreationResource.setTitle(title);
		taskCreationResource.setDescription(description);
		taskCreationResource.setConstraints(constraintsResource);

		return resourceManager.createTask(userSession, taskCreationResource);
	}

	/**
	 * @see DataManagerImpl#getAllTasks(boolean)
	 */
	ResultSetImpl<TaskID, Task> getAllTasks(boolean loadCompletely)
	{
		try
		{
			ResultPageResource resultPageResource = resourceManager.getAllTasks();

			return createTaskResultSet(resultPageResource.getList(), resultPageResource.getRelated(), false, loadCompletely);
		}
		catch(NoResultsException e)
		{
			return createEmptyTaskResultSet(false, loadCompletely);
		}
	}

	/**
	 * @see DataManagerImpl#getNextPageData(ResultSetImpl)
	 */
	LinkedHashMap<TaskID, Task> getNextPageData(ResultSetImpl<TaskID, Task> resultSet)
	{
		if(resultSet.isQueryResultSet())
		{
			QueryResultPageResource queryResultPageResource = resourceManager.getQueryResultPageResource(resultSet.getNextPageURL());

			resultSet.setNextPageURL(createNextPageURL(queryResultPageResource.getResults().getPage().getRelated()));

			return getTasksByIDs(createTaskIDs(queryResultPageResource.getResults().getPage().getList()), resultSet.getLoadCompletelyStatus());
		}
		else
		{
			ResultPageResource resultPageResource = resourceManager.getResultPage(resultSet.getNextPageURL());

			resultSet.setNextPageURL(createNextPageURL(resultPageResource.getRelated()));

			return getTasksByIDs(createTaskIDs(resultPageResource.getList()), resultSet.getLoadCompletelyStatus());
		}
	}

	/**
	 * {@code forceAPICall} = {@code true}
	 * 
	 * @see DataManagerImpl#getTask(TaskID, boolean, boolean)
	 */
	Task getTask(TaskID taskID, boolean loadCompletely)
	{
		if(loadCompletely)
		{
			return createSnapShotTask(taskID, resourceManager.getSnapShotTask(taskID));
		}
		else
		{
			return createMetaDataTask(taskID, resourceManager.getMetaDataTask(taskID));
		}
	}

	/**
	 * @see DataManagerImpl#getTasks(List, BoundingBox, TimeRange, boolean)
	 */
	ResultSetImpl<TaskID, Task> getTasks(List<String> tags, BoundingBox boundingBox, TimeRange timeRange, boolean loadCompletely)
	{
		ArrayList<String> tagIDs = new ArrayList<String>();

		if(tags.size() > 0)
		{
			MultipleValuesRequestResource multipleValuesRequestResource = new MultipleValuesRequestResource();

			multipleValuesRequestResource.setValues(new ArrayList<String>(tags));

			MultipleTagsResource multipleTagsResource = resourceManager.getMultipleTags(multipleValuesRequestResource);
			LinkedHashMap<String, BaseTagResource> results = multipleTagsResource.getResults();

			for(String tag : tags)
			{
				tagIDs.add(results.get(tag).getBaseTag().getLink().getHref());
			}
		}

		DispatchQueryResource dispatchQueryResource = new DispatchQueryResource();
		QueryResource queryResource = new QueryResource();
		TimeRangeResource timeRangeResource = new TimeRangeResource();
		BoundingBoxResource boundingBoxResource = new BoundingBoxResource();

		timeRangeResource.setStart(timeRange.getStartDateTime().toString(Time.ISO_UTC_DATE_TIME_FORMATTER));
		timeRangeResource.setEnd(timeRange.getEndDateTime().toString(Time.ISO_UTC_DATE_TIME_FORMATTER));
		boundingBoxResource.setBbox(boundingBox.getArrayList(1, 0, 3, 2));

		queryResource.setTime(timeRangeResource);
		queryResource.setLocation(boundingBoxResource);
		queryResource.setTags(tagIDs);

		dispatchQueryResource.setQuery(queryResource);

		try
		{
			PageResource pageResource = resourceManager.executeQuery(dispatchQueryResource).getResults().getPage();

			return createTaskResultSet(pageResource.getList(), pageResource.getRelated(), true, loadCompletely);
		}
		catch(NoResultsException e)
		{
			return createEmptyTaskResultSet(true, loadCompletely);
		}
	}

	/**
	 * @see DataManagerImpl#getTasks(OrderType, BoundingBox, boolean)
	 */
	ResultSetImpl<TaskID, Task> getTasks(OrderType orderType, BoundingBox boundingBox, boolean loadCompletely)
	{
		try
		{
			PageResource pageResource = resourceManager.executeQuery(orderType, boundingBox).getResults().getPage();

			return createTaskResultSet(pageResource.getList(), pageResource.getRelated(), true, loadCompletely);
		}
		catch(NoResultsException e)
		{
			return createEmptyTaskResultSet(true, loadCompletely);
		}
	}

	/**
	 * @see DataManagerImpl#getTasks(OrderType, double, double, boolean)
	 */
	ResultSetImpl<TaskID, Task> getTasks(OrderType orderType, double locationLatitude, double locationLongitude, boolean loadCompletely)
	{
		try
		{
			PageResource pageResource = resourceManager.executeQuery(orderType, locationLatitude, locationLongitude).getResults().getPage();

			return createTaskResultSet(pageResource.getList(), pageResource.getRelated(), true, loadCompletely);
		}
		catch(NoResultsException e)
		{
			return createEmptyTaskResultSet(true, loadCompletely);
		}
	}

	/**
	 * @see DataManagerImpl#getTasksAfter(DateTime, boolean)
	 */
	ResultSetImpl<TaskID, Task> getTasksAfter(DateTime time, boolean loadCompletely)
	{
		try
		{
			ResultPageResource resultPageResource = resourceManager.getTasksAfter(time);

			return createTaskResultSet(resultPageResource.getList(), resultPageResource.getRelated(), false, loadCompletely);
		}
		catch(NoResultsException e)
		{
			return createEmptyTaskResultSet(false, loadCompletely);
		}
	}

	/**
	 * {@code forceAPICall} = {@code true}
	 * 
	 * @see DataManagerImpl#getTasksByIDs(List, boolean, boolean)
	 */
	LinkedHashMap<TaskID, Task> getTasksByIDs(List<TaskID> taskIDs, boolean loadCompletely)
	{
		MultipleTasksRequestResource multipleTasksRequestResource = new MultipleTasksRequestResource();
		ArrayList<String> tasksList = new ArrayList<String>();

		for(TaskID taskID : taskIDs)
		{
			tasksList.add(taskID.getUniqueResourceURL().toExternalForm());
		}

		multipleTasksRequestResource.setTasks(tasksList);

		LinkedHashMap<TaskID, Task> tasks = new LinkedHashMap<TaskID, Task>();

		if(loadCompletely)
		{
			LinkedHashMap<String, TaskSnapShotResource> results = resourceManager.getMultipleSnapShotTasks(multipleTasksRequestResource).getResults();

			Task task;

			for(TaskID taskID : taskIDs)
			{
				task = createSnapShotTask(taskID, results.get(taskID.getUniqueResourceURL().toExternalForm()));

				tasks.put(taskID, task);
			}
		}
		else
		{
			LinkedHashMap<String, TaskMetaDataResource> results = resourceManager.getMultipleMetaDataTasks(multipleTasksRequestResource).getResults();

			Task task;

			for(TaskID taskID : taskIDs)
			{
				task = createMetaDataTask(taskID, results.get(taskID.getUniqueResourceURL().toExternalForm()));

				tasks.put(taskID, task);
			}
		}

		return tasks;
	}

	/**
	 * @see DataManagerImpl#getUserVote(UserSession, EntityID)
	 */
	VoteType getUserVote(UserSession userSession, EntityID entityID)
	{
		UserVoteResource userVoteResource = resourceManager.getUserVote(userSession, entityID);

		return VoteType.getValue(userVoteResource.getVote());
	}

	/**
	 * @see DataManagerImpl#removeUserVote(UserSession, EntityID)
	 */
	boolean removeUserVote(UserSession userSession, EntityID entityID)
	{
		return resourceManager.removeUserVote(userSession, entityID);
	}

	/**
	 * @see DataManagerImpl#setUserVote(UserSession, VoteType, EntityID)
	 */
	boolean setUserVote(UserSession userSession, VoteType voteType, EntityID entityID)
	{
		return resourceManager.setUserVote(userSession, voteType, entityID);
	}

	/**
	 * Converts a {@link CommentsResource} to a {@link ResultSet} of
	 * {@link Comment}s and their mapped {@link CommentID}s.
	 * 
	 * @param commentsResource The {@code CommentsResource}
	 * @param taskID The corresponding {@code TaskID}
	 * @return A {@code ResultSet} with the {@code Comment}s
	 */
	private ResultSet<CommentID, Comment> createCommentResultSet(CommentsResource commentsResource, TaskID taskID)
	{
		LinkedHashMap<CommentID, Comment> comments = new LinkedHashMap<CommentID, Comment>();

		CommentID commentID;
		Comment comment;
		String content;
		DateTime creationTime;
		UserID creator;
		Votes votes;
		CommentResource commentResource;

		for(CommentsResource.ListEntry listEntry : commentsResource.getList())
		{
			commentResource = listEntry.getComment();

			commentID = CoreServiceEntityIDFactory.getCommentIDInstance(commentResource.getLink().getHref(), taskID, taskID);

			content = commentResource.getContent();
			creationTime = new DateTime(commentResource.getCreationTime());
			creator = UserFactory.getUserIDInstance(commentResource.getCreator().getLink().getHref());
			votes = new Votes(commentResource.getVotes().getUp(), commentResource.getVotes().getDown());

			comment = new CommentBuilder(commentID, dataManager).content(content).creationTime(creationTime).creator(creator).votes(votes).build();

			comments.put(commentID, comment);
		}

		return ResultSetFactory.getResultSetInstance(null, systemSession, false, LoadType.COMPLETE, comments, null);
	}

	/**
	 * Creates and returns an empty {@link ResultSet}.
	 * 
	 * @param isQueryResultSet {@code true} if the {@code ResultSet} derives
	 *        from a query result page; otherwise {@code false}
	 * @param loadCompletely {@code true} if the complete {@code Task}s and all
	 *        of their sub elements should be loaded; {@code false} if it is
	 *        sufficient when only the meta data of the {@code Task}s will be
	 *        loaded for the time being
	 * @return An empty {@code ResultSet}
	 */
	private ResultSetImpl<TaskID, Task> createEmptyTaskResultSet(boolean isQueryResultSet, boolean loadCompletely)
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

		return (ResultSetImpl<TaskID, Task>) ResultSetFactory.getResultSetInstance(dataManager, systemSession, isQueryResultSet, loadType, new LinkedHashMap<TaskID, Task>(), null);
	}

	/**
	 * Creates a {@link Task} from the given {@link TaskMetaDataResource} and
	 * returns the {@code Task}.
	 * 
	 * @param taskID The corresponding {@code TaskID}
	 * @param taskMetaDataResource The {@code TaskMetaDataResource}
	 * @return The created {@code Task}
	 */
	private Task createMetaDataTask(TaskID taskID, TaskMetaDataResource taskMetaDataResource)
	{
		if(taskMetaDataResource == null)
		{
			return null;
		}

		TaskBuilder taskBuilder = new TaskBuilder(taskID, dataManager, dataManager, false);

		setBaseTaskData(taskMetaDataResource.getTask(), taskBuilder);

		return taskBuilder.build();
	}

	/**
	 * Extracts the {@link URL} of the next page from the given {@code related}
	 * resource.
	 * 
	 * @param related The {@code related} list resource
	 * @return The {@code URL} of the next page
	 */
	private URL createNextPageURL(ArrayList<HyperLinkResourceWrapper> related)
	{
		URL url = null;

		for(HyperLinkResourceWrapper linkResourceWrapper : related)
		{
			if(linkResourceWrapper.getLink().getRel().equals("next"))
			{
				try
				{
					return new URL(linkResourceWrapper.getLink().getHref());
				}
				catch(MalformedURLException e)
				{
					e.printStackTrace();
				}
			}
		}

		return url;
	}

	/**
	 * Creates a {@link Task} from the given {@link TaskSnapShotResource} and
	 * returns the {@code Task}.
	 * 
	 * @param taskID The corresponding {@code TaskID}
	 * @param taskSnapShotResource The {@code TaskSnapShotResource}
	 * @return The created {@code Task}
	 */
	private Task createSnapShotTask(TaskID taskID, TaskSnapShotResource taskSnapShotResource)
	{
		if(taskSnapShotResource == null)
		{
			return null;
		}

		TaskBuilder taskBuilder = new TaskBuilder(taskID, dataManager, dataManager, true);

		setBaseTaskData(taskSnapShotResource.getTask(), taskBuilder);

		ResultSet<SubmissionID, Submission> submissions = createSubmissionResultSet(taskSnapShotResource.getSubmissions(), taskID);
		ResultSet<CommentID, Comment> comments = createCommentResultSet(taskSnapShotResource.getComments(), taskID);
		ResultSet<TagID, Tag> tags = createTagResultSet(taskSnapShotResource.getTags(), taskID, taskID);

		return taskBuilder.submissions(submissions).comments(comments).tags(tags).build();
	}

	/**
	 * Converts a {@link SubmissionsResource} to a {@link ResultSet} of
	 * {@link Submission}s and their mapped {@link SubmissionID}s.
	 * 
	 * @param submissionsResource The {@code SubmissionsResource}
	 * @param taskID The corresponding {@code TaskID}
	 * @return A {@code ResultSet} with the {@code Submission}s
	 */
	private ResultSet<SubmissionID, Submission> createSubmissionResultSet(SubmissionsResource submissionsResource, TaskID taskID)
	{
		LinkedHashMap<SubmissionID, Submission> submissions = new LinkedHashMap<SubmissionID, Submission>();

		SubmissionID submissionID;
		Submission submission;
		DocumentID documentID;
		DateTime creationTime;
		UserID creator;
		Votes votes;
		ResultSet<TagID, Tag> tags;
		SubmissionResource submissionResource;

		for(SubmissionsResource.ListEntry listEntry : submissionsResource.getList())
		{
			submissionResource = listEntry.getSubmission();

			submissionID = TaskServiceEntityIDFactory.getSubmissionIDInstance(submissionResource.getLink().getHref(), taskID, taskID);

			documentID = CoreServiceEntityIDFactory.getDocumentIDInstance(submissionResource.getDocument().getLink().getHref());
			creationTime = new DateTime(submissionResource.getCreationTime());
			creator = UserFactory.getUserIDInstance(submissionResource.getCreator().getLink().getHref());
			votes = new Votes(submissionResource.getVotes().getUp(), submissionResource.getVotes().getDown());

			tags = createTagResultSet(submissionResource.getTags(), taskID, submissionID);

			submission = new SubmissionBuilder(submissionID, dataManager, dataManager, documentID).creationTime(creationTime).creator(creator).votes(votes).tags(tags).build();

			submissions.put(submissionID, submission);
		}

		return ResultSetFactory.getResultSetInstance(null, systemSession, false, LoadType.COMPLETE, submissions, null);
	}

	/**
	 * Converts a {@link TagsResource} to a {@link ResultSet} of {@link Tag}s
	 * and their mapped {@link TagID}s.
	 * 
	 * @param tagsResource The {@code TagsResource}
	 * @param rootEntityID The corresponding root {@code EntityID}
	 * @param parentEntityID The corresponding parent {@code EntityID}
	 * @return A {@code ResultSet} with the {@code Tag}s
	 */
	private ResultSet<TagID, Tag> createTagResultSet(TagsResource tagsResource, EntityID rootEntityID, EntityID parentEntityID)
	{
		LinkedHashMap<TagID, Tag> tags = new LinkedHashMap<TagID, Tag>();

		TagID tagID;
		Tag tag;
		String value;
		DateTime creationTime;
		UserID creator;
		Votes votes;
		TagResource tagResource;

		for(TagsResource.ListEntry listEntry : tagsResource.getList())
		{
			tagResource = listEntry.getTag();

			tagID = CoreServiceEntityIDFactory.getTagIDInstance(tagResource.getLink().getHref(), rootEntityID, parentEntityID);

			value = tagResource.getValue();
			creationTime = new DateTime(tagResource.getCreationTime());
			creator = UserFactory.getUserIDInstance(tagResource.getCreator().getLink().getHref());
			votes = new Votes(tagResource.getVotes().getUp(), tagResource.getVotes().getDown());

			tag = new TagBuilder(tagID, dataManager).value(value).creationTime(creationTime).creator(creator).votes(votes).build();

			tags.put(tagID, tag);
		}

		return ResultSetFactory.getResultSetInstance(null, systemSession, false, LoadType.COMPLETE, tags, null);
	}

	/**
	 * Extracts the {@link List} of {@link TaskID}s from the given
	 * {@link ArrayList} of {@link TaskHyperLinkResource}s.
	 * 
	 * @param taskHyperLinkResources The {@code ArrayList} of
	 *        {@code TaskHyperLinkResource}s
	 * @return The {@code List} of {@code TaskID}s
	 */
	private List<TaskID> createTaskIDs(ArrayList<TaskHyperLinkResource> taskHyperLinkResources)
	{
		List<TaskID> taskIDs = new ArrayList<TaskID>();

		for(TaskHyperLinkResource taskHyperLink : taskHyperLinkResources)
		{
			taskIDs.add(TaskServiceEntityIDFactory.getTaskIDInstance(taskHyperLink.getTask().getLink().getHref()));
		}

		return taskIDs;
	}

	/**
	 * Creates a {@link ResultSet} from the given page data and returns the
	 * {@code ResultSet}.
	 * 
	 * @param listArrayList The {@code list} {@code ArrayList} with the
	 *        {@code TaskHyperLinkResource}s
	 * @param relatedArrayList The {@code related} {@code ArrayList} with the
	 *        {@code HyperLinkResourceWrapper}s
	 * @param isQueryResultSet {@code true} if the {@code ResultSet} derives
	 *        from a query result page; otherwise {@code false}
	 * @param loadCompletely {@code true} if the complete {@code Task}s and all
	 *        of their sub elements should be loaded; {@code false} if it is
	 *        sufficient when only the meta data of the {@code Task}s will be
	 *        loaded for the time being
	 * @return The created {@code ResultSet}
	 */
	private ResultSetImpl<TaskID, Task> createTaskResultSet(ArrayList<TaskHyperLinkResource> listArrayList, ArrayList<HyperLinkResourceWrapper> relatedArrayList, boolean isQueryResultSet, boolean loadCompletely)
	{
		List<TaskID> taskIDs = createTaskIDs(listArrayList);
		URL url = createNextPageURL(relatedArrayList);

		LoadType loadType;

		if(loadCompletely)
		{
			loadType = LoadType.COMPLETE;
		}
		else
		{
			loadType = LoadType.METADATA;
		}

		return (ResultSetImpl<TaskID, Task>) ResultSetFactory.getResultSetInstance(dataManager, systemSession, isQueryResultSet, loadType, getTasksByIDs(taskIDs, loadCompletely), url);
	}

	/**
	 * Sets the base data of the given {@link TaskResource} to the given
	 * {@link TaskBuilder}.
	 * 
	 * @param taskResource The {@code TaskResource}
	 * @param taskBuilder The {@code TaskBuilder}
	 */
	private void setBaseTaskData(TaskResource taskResource, TaskBuilder taskBuilder)
	{
		TimeRangeResource timeRangeResource = taskResource.getConstraints().getTime();
		ArrayList<Double> bbox = taskResource.getConstraints().getLocation().getBbox();

		taskBuilder.title(taskResource.getTitle());
		taskBuilder.description(taskResource.getDescription());
		taskBuilder.creationTime(new DateTime(taskResource.getCreationTime()));
		taskBuilder.creator(UserFactory.getUserIDInstance(taskResource.getCreator().getLink().getHref()));
		taskBuilder.votes(new Votes(taskResource.getVotes().getUp(), taskResource.getVotes().getDown()));
		taskBuilder.relevantTimeRange(new TimeRange(new DateTime(timeRangeResource.getStart()), new DateTime(timeRangeResource.getEnd())));
		taskBuilder.relevantArea(new BoundingBox(bbox.get(0), bbox.get(1), bbox.get(2), bbox.get(3)));
	}
}
