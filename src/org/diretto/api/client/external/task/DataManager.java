package org.diretto.api.client.external.task;

import java.util.List;

import org.diretto.api.client.base.data.BoundingBox;
import org.diretto.api.client.base.data.ResultSet;
import org.diretto.api.client.base.data.TimeRange;
import org.diretto.api.client.base.types.OrderType;
import org.diretto.api.client.external.task.entities.Task;
import org.diretto.api.client.external.task.entities.TaskID;
import org.diretto.api.client.session.UserSession;
import org.joda.time.DateTime;

/**
 * This interface represents a {@code DataManager}. <br/><br/>
 * 
 * The {@code DataManager} provides the bulk of the platform functionalities in
 * respect of {@code Task Service} API operations.
 * 
 * @author Tobias Schlecht
 */
public interface DataManager
{
	/**
	 * Creates a new {@link Task} and returns the {@link TaskID} if it was
	 * successful.
	 * 
	 * @param userSession The corresponding {@code UserSession}
	 * @param title The title of the {@code Task}
	 * @param description The description of the {@code Task}
	 * @param relevantArea A {@code BoundingBox} of the relevant area
	 * @param relevantTimeRange The relevant {@code TimeRange}
	 * @return The corresponding {@code TaskID}
	 */
	TaskID createTask(UserSession userSession, String title, String description, BoundingBox relevantArea, TimeRange relevantTimeRange);

	/**
	 * Returns a {@link ResultSet} with all {@link Task}s and the mapped
	 * {@link TaskID}s available in the {@code Cache}. <br/><br/>
	 * 
	 * <i>Important:</i> Do not save the returned data or any part of them in an
	 * own variable, but requery the object, because if a method of the stored
	 * object will be invoked later, the received data could no longer be
	 * current.
	 * 
	 * @return A {@code ResultSet} with the {@code Task}s
	 */
	ResultSet<TaskID, Task> getAllCachedTasks();

	/**
	 * Returns a {@link ResultSet} with all available {@link Task}s and the
	 * mapped {@link TaskID}s. <br/><br/>
	 * 
	 * <i>Annotation:</i> This method is <u>critical</u> in respect of
	 * performance aspects and should therefore only be invoked if it is
	 * absolutely necessary. Note also that for the time being only the meta
	 * data of the {@code Task}s without their sub elements will be loaded.
	 * <br/><br/>
	 * 
	 * <i>Important:</i> Do not save the returned data or any part of them in an
	 * own variable, but requery the object, because if a method of the stored
	 * object will be invoked later, the received data could no longer be
	 * current.
	 * 
	 * @return A {@code ResultSet} with the {@code Task}s
	 */
	ResultSet<TaskID, Task> getAllTasks();

	/**
	 * Returns a {@link ResultSet} with all available {@link Task}s and the
	 * mapped {@link TaskID}s. <br/><br/>
	 * 
	 * <i>Annotation:</i> This method is <u>critical</u> in respect of
	 * performance aspects and should therefore only be invoked if it is
	 * absolutely necessary. <br/><br/>
	 * 
	 * <i>Important:</i> Do not save the returned data or any part of them in an
	 * own variable, but requery the object, because if a method of the stored
	 * object will be invoked later, the received data could no longer be
	 * current.
	 * 
	 * @param loadCompletely {@code true} if the complete {@code Task}s and all
	 *        of their sub elements should be loaded; {@code false} if it is
	 *        sufficient when only the meta data of the {@code Task}s will be
	 *        loaded for the time being
	 * @return A {@code ResultSet} with the {@code Task}s
	 */
	ResultSet<TaskID, Task> getAllTasks(boolean loadCompletely);

	/**
	 * Returns the maximum value of {@link Task}s which can be requested with
	 * one method invocation.
	 * 
	 * @return The maximum value of {@link Task}s which can be requested
	 */
	int getMaxTaskRequestSize();

	/**
	 * Returns the {@link Task} with the specified {@link TaskID} or
	 * {@code null} if there is no {@code Task} with the given {@code TaskID}.
	 * <br/><br/>
	 * 
	 * <i>Annotation:</i> For the time being only the meta data of the
	 * {@code Task} without its sub elements will be loaded. If the
	 * {@code Cache} is activated and the desired {@code Task} is located in the
	 * {@code Cache}, the {@code Cache} version will be returned without a
	 * direct API call. <br/><br/>
	 * 
	 * <i>Important:</i> Do not save the returned data or any part of them in an
	 * own variable, but requery the object, because if a method of the stored
	 * object will be invoked later, the received data could no longer be
	 * current.
	 * 
	 * @param taskID A {@code TaskID}
	 * @return The {@code Task}
	 */
	Task getTask(TaskID taskID);

	/**
	 * Returns the {@link Task} with the specified {@link TaskID} or
	 * {@code null} if there is no {@code Task} with the given {@code TaskID}.
	 * <br/><br/>
	 * 
	 * <i>Important:</i> Do not save the returned data or any part of them in an
	 * own variable, but requery the object, because if a method of the stored
	 * object will be invoked later, the received data could no longer be
	 * current.
	 * 
	 * @param taskID A {@code TaskID}
	 * @param loadCompletely {@code true} if the complete {@code Task} and all
	 *        of its sub elements should be loaded; {@code false} if it is
	 *        sufficient when only the meta data of the {@code Task} will be
	 *        loaded for the time being
	 * @param forceAPICall {@code true} if the {@code Task} should be loaded
	 *        directly from the API; {@code false} if the {@code Task} can be
	 *        loaded from the {@code Cache}
	 * @return The {@code Task}
	 */
	Task getTask(TaskID taskID, boolean loadCompletely, boolean forceAPICall);

	/**
	 * Returns a {@link ResultSet} with the requested {@link Task}s and the
	 * mapped {@link TaskID}s. <br/><br/>
	 * 
	 * <i>Annotation:</i> For the time being only the meta data of the
	 * {@code Task}s without their sub elements will be loaded. <br/><br/>
	 * 
	 * <i>Important:</i> Do not save the returned data or any part of them in an
	 * own variable, but requery the object, because if a method of the stored
	 * object will be invoked later, the received data could no longer be
	 * current.
	 * 
	 * @param tags A {@code List} of tags in {@code String} representation
	 * @param boundingBox A {@code BoundingBox}
	 * @param timeRange A {@code TimeRange}
	 * @return A {@code ResultSet} with the {@code Task}s
	 */
	ResultSet<TaskID, Task> getTasks(List<String> tags, BoundingBox boundingBox, TimeRange timeRange);

	/**
	 * Returns a {@link ResultSet} with the requested {@link Task}s and the
	 * mapped {@link TaskID}s. <br/><br/>
	 * 
	 * <i>Important:</i> Do not save the returned data or any part of them in an
	 * own variable, but requery the object, because if a method of the stored
	 * object will be invoked later, the received data could no longer be
	 * current.
	 * 
	 * @param tags A {@code List} of tags in {@code String} representation
	 * @param boundingBox A {@code BoundingBox}
	 * @param timeRange A {@code TimeRange}
	 * @param loadCompletely {@code true} if the complete {@code Task}s and all
	 *        of their sub elements should be loaded; {@code false} if it is
	 *        sufficient when only the meta data of the {@code Task}s will be
	 *        loaded for the time being
	 * @return A {@code ResultSet} with the {@code Task}s
	 */
	ResultSet<TaskID, Task> getTasks(List<String> tags, BoundingBox boundingBox, TimeRange timeRange, boolean loadCompletely);

	/**
	 * Returns a {@link ResultSet} with the requested {@link Task}s and the
	 * mapped {@link TaskID}s. <br/><br/>
	 * 
	 * <i>Annotation:</i> For the time being only the meta data of the
	 * {@code Task}s without their sub elements will be loaded. <br/><br/>
	 * 
	 * <i>Important:</i> Do not save the returned data or any part of them in an
	 * own variable, but requery the object, because if a method of the stored
	 * object will be invoked later, the received data could no longer be
	 * current.
	 * 
	 * @param orderType An {@code OrderType}
	 * @param boundingBox A {@code BoundingBox}
	 * @return A {@code ResultSet} with the {@code Task}s
	 */
	ResultSet<TaskID, Task> getTasks(OrderType orderType, BoundingBox boundingBox);

	/**
	 * Returns a {@link ResultSet} with the requested {@link Task}s and the
	 * mapped {@link TaskID}s. <br/><br/>
	 * 
	 * <i>Important:</i> Do not save the returned data or any part of them in an
	 * own variable, but requery the object, because if a method of the stored
	 * object will be invoked later, the received data could no longer be
	 * current.
	 * 
	 * @param orderType An {@code OrderType}
	 * @param boundingBox A {@code BoundingBox}
	 * @param loadCompletely {@code true} if the complete {@code Task}s and all
	 *        of their sub elements should be loaded; {@code false} if it is
	 *        sufficient when only the meta data of the {@code Task}s will be
	 *        loaded for the time being
	 * @return A {@code ResultSet} with the {@code Task}s
	 */
	ResultSet<TaskID, Task> getTasks(OrderType orderType, BoundingBox boundingBox, boolean loadCompletely);

	/**
	 * Returns a {@link ResultSet} with the requested {@link Task}s and the
	 * mapped {@link TaskID}s. <br/><br/>
	 * 
	 * <i>Annotation:</i> For the time being only the meta data of the
	 * {@code Task}s without their sub elements will be loaded. <br/><br/>
	 * 
	 * <i>Important:</i> Do not save the returned data or any part of them in an
	 * own variable, but requery the object, because if a method of the stored
	 * object will be invoked later, the received data could no longer be
	 * current.
	 * 
	 * @param orderType An {@code OrderType}
	 * @param locationLatitude The latitude of the location in degrees
	 * @param locationLongitude The longitude of the location in degrees
	 * @return A {@code ResultSet} with the {@code Task}s
	 */
	ResultSet<TaskID, Task> getTasks(OrderType orderType, double locationLatitude, double locationLongitude);

	/**
	 * Returns a {@link ResultSet} with the requested {@link Task}s and the
	 * mapped {@link TaskID}s. <br/><br/>
	 * 
	 * <i>Important:</i> Do not save the returned data or any part of them in an
	 * own variable, but requery the object, because if a method of the stored
	 * object will be invoked later, the received data could no longer be
	 * current.
	 * 
	 * @param orderType An {@code OrderType}
	 * @param locationLatitude The latitude of the location in degrees
	 * @param locationLongitude The longitude of the location in degrees
	 * @param loadCompletely {@code true} if the complete {@code Task}s and all
	 *        of their sub elements should be loaded; {@code false} if it is
	 *        sufficient when only the meta data of the {@code Task}s will be
	 *        loaded for the time being
	 * @return A {@code ResultSet} with the {@code Task}s
	 */
	ResultSet<TaskID, Task> getTasks(OrderType orderType, double locationLatitude, double locationLongitude, boolean loadCompletely);

	/**
	 * Returns a {@link ResultSet} with the requested {@link Task}s and the
	 * mapped {@link TaskID}s. <br/><br/>
	 * 
	 * <i>Annotation:</i> For the time being only the meta data of the
	 * {@code Task}s without their sub elements will be loaded. <br/><br/>
	 * 
	 * <i>Important:</i> Do not save the returned data or any part of them in an
	 * own variable, but requery the object, because if a method of the stored
	 * object will be invoked later, the received data could no longer be
	 * current.
	 * 
	 * @param time The {@code DateTime} after which the {@code Task}s should be
	 *        returned
	 * @return A {@code ResultSet} with the {@code Task}s
	 */
	ResultSet<TaskID, Task> getTasksAfter(DateTime time);

	/**
	 * Returns a {@link ResultSet} with the requested {@link Task}s and the
	 * mapped {@link TaskID}s. <br/><br/>
	 * 
	 * <i>Important:</i> Do not save the returned data or any part of them in an
	 * own variable, but requery the object, because if a method of the stored
	 * object will be invoked later, the received data could no longer be
	 * current.
	 * 
	 * @param time The {@code DateTime} after which the {@code Task}s should be
	 *        returned
	 * @param loadCompletely {@code true} if the complete {@code Task}s and all
	 *        of their sub elements should be loaded; {@code false} if it is
	 *        sufficient when only the meta data of the {@code Task}s will be
	 *        loaded for the time being
	 * @return A {@code ResultSet} with the {@code Task}s
	 */
	ResultSet<TaskID, Task> getTasksAfter(DateTime time, boolean loadCompletely);

	/**
	 * Returns a {@link ResultSet} with the requested {@link Task}s and the
	 * mapped {@link TaskID}s. <br/><br/>
	 * 
	 * <i>Annotation:</i> For the time being only the meta data of the
	 * {@code Task}s without their sub elements will be loaded. If the
	 * {@code Cache} is activated and the desired {@code Task}s are located in
	 * the {@code Cache}, the {@code Cache} versions will be returned without a
	 * direct API call. <br/><br/>
	 * 
	 * <i>Important:</i> Do not save the returned data or any part of them in an
	 * own variable, but requery the object, because if a method of the stored
	 * object will be invoked later, the received data could no longer be
	 * current.
	 * 
	 * @param taskIDs A {@code List} of {@code TaskID}s
	 * @return A {@code ResultSet} with the {@code Task}s
	 */
	ResultSet<TaskID, Task> getTasksByIDs(List<TaskID> taskIDs);

	/**
	 * Returns a {@link ResultSet} with the requested {@link Task}s and the
	 * mapped {@link TaskID}s. <br/><br/>
	 * 
	 * <i>Important:</i> Do not save the returned data or any part of them in an
	 * own variable, but requery the object, because if a method of the stored
	 * object will be invoked later, the received data could no longer be
	 * current.
	 * 
	 * @param taskIDs A {@code List} of {@code TaskID}s
	 * @param loadCompletely {@code true} if the complete {@code Task}s and all
	 *        of their sub elements should be loaded; {@code false} if it is
	 *        sufficient when only the meta data of the {@code Task}s will be
	 *        loaded for the time being
	 * @param forceAPICall {@code true} if the {@code Task}s should be loaded
	 *        directly from the API; {@code false} if the {@code Task}s can be
	 *        loaded from the {@code Cache}
	 * @return A {@code ResultSet} with the {@code Task}s
	 */
	ResultSet<TaskID, Task> getTasksByIDs(List<TaskID> taskIDs, boolean loadCompletely, boolean forceAPICall);

	/**
	 * Determines if the {@code Cache} is activated.
	 * 
	 * @return {@code true} if the {@code Cache} is activated; otherwise
	 *         {@code false}
	 */
	boolean isCacheActivated();
}
