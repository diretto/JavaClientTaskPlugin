package org.diretto.api.client.external.task;

import java.net.URL;

import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.XMLConfiguration;
import org.diretto.api.client.base.exceptions.CacheNotActivatedException;

import net.sf.ehcache.Cache;

/**
 * The {@code CacheManager} is responsible for the caching aspects.
 * 
 * @author Tobias Schlecht
 */
final class CacheManager
{
	// See also: http://ehcache.org/documentation

	private final boolean cacheActivated;
	private final URL cacheConfigFile;
	private final String cacheName;

	private final net.sf.ehcache.CacheManager cacheManager;
	private final Cache cache;

	/**
	 * The constructor is {@code private} to have strict control what instances
	 * exist at any time. Instead of the constructor the {@code public}
	 * <i>static factory method</i> {@link #getInstance(DataManagerImpl)}
	 * returns the instances of the class.
	 */
	private CacheManager()
	{
		XMLConfiguration xmlConfiguration = TaskServiceID.INSTANCE.getXMLConfiguration();

		cacheActivated = xmlConfiguration.getBoolean("cache/cache-activated");

		if(cacheActivated)
		{
			cacheConfigFile = ConfigurationUtils.locate(null, xmlConfiguration.getString("cache/cache-config-file"));
			cacheName = xmlConfiguration.getString("cache/cache-name");

			cacheManager = net.sf.ehcache.CacheManager.create(cacheConfigFile);
			cache = cacheManager.getCache(cacheName);
		}
		else
		{
			cacheConfigFile = null;
			cacheName = null;

			cacheManager = null;
			cache = null;
		}
	}

	/**
	 * Returns a {@link CacheManager} instance for the corresponding
	 * {@link DataManager}.
	 * 
	 * @param dataManager The corresponding {@code DataManager}
	 * @return A {@code CacheManager} instance
	 */
	static synchronized CacheManager getInstance(DataManagerImpl dataManager)
	{
		return new CacheManager();
	}

	/**
	 * Returns the {@link Cache}.
	 * 
	 * @return The {@code Cache}
	 */
	Cache getCache()
	{
		if(cacheActivated)
		{
			return cache;
		}
		else
		{
			throw new CacheNotActivatedException();
		}
	}

	/**
	 * Determines if the {@code Cache} is activated.
	 * 
	 * @return {@code true} if the {@code Cache} is activated; otherwise
	 *         {@code false}
	 */
	boolean isCacheActivated()
	{
		return cacheActivated;
	}
}
