package fr.paris.lutece.plugins.mydashboard.business;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


/**
 * Interface of DAO for MyDashboardConfiguration objects
 */
public interface IMyDashboardConfigurationDAO
{
    /**
     * Get the list of MyDashboardConfiguration associated to a given user
     * @param strUserName The name of the user to get the
     *            MyDashboardConfiguration of
     * @param plugin The plugin
     * @return The list MyDashboardConfiguration associated to the given user,
     *         or an empty list if no configuration was found
     */
    List<MyDashboardConfiguration> findByUserName( String strUserName, Plugin plugin );

    /**
     * Modify a configuration
     * @param config The new value of the configuration
     * @param plugin The plugin
     */
    void updateConfiguration( MyDashboardConfiguration config, Plugin plugin );

    /**
     * Create a new configuration
     * @param config The configuration to create
     * @param plugin The plugin
     */
    void insertConfiguration( MyDashboardConfiguration config, Plugin plugin );

    /**
     * Remove every configuration associated with a given user name
     * @param strUserName The name of the user to remove the dashboard
     *            configuration of
     * @param plugin The plugin
     */
    void removeByUserName( String strUserName, Plugin plugin );

    /**
     * Remove a dashboard from a user name and a dashboard component id
     * @param strUserName The user name
     * @param strDashboardId The dashboard component id
     * @param plugin The plugin
     */
    void removeByUserNameAndDashboardId( String strUserName, String strDashboardId, Plugin plugin );
}
