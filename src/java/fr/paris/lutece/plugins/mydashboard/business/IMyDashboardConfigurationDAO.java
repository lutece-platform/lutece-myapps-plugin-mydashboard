package fr.paris.lutece.plugins.mydashboard.business;

import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;


/**
 * Interface of DAO for MyDashboardConfiguration objects
 */
public interface IMyDashboardConfigurationDAO
{
    /**
     * Get a new id of dashboard configurations. Each id returned by this method
     * are ensured to be unique.
     * @return The new id of the dashboard configuration
     */
    String getNewConfigId( );

    /**
     * Get the list of MyDashboardConfiguration associated to a given
     * configuration id
     * @param strConfigId The id of the configuration
     * @param plugin The plugin
     * @return The list MyDashboardConfiguration associated to the given
     *         configuration id, or an empty list if no configuration was found
     */
    List<MyDashboardConfiguration> findByConfigId( String strConfigId, Plugin plugin );

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
     * Remove every configuration associated with a given configuration id
     * @param strConfigId The id of the configuration
     * @param plugin The plugin
     */
    void removeByConfigId( String strConfigId, Plugin plugin );

    /**
     * Remove a dashboard from a configuration id and a dashboard component id
     * @param strConfigId The id of the configuration
     * @param strDashboardId The dashboard component id
     * @param plugin The plugin
     */
    void removeByConfigIdAndDashboardId( String strConfigId, String strDashboardId, Plugin plugin );
}
