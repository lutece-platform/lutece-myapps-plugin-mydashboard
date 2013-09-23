package fr.paris.lutece.plugins.mydashboard.service;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;


/**
 * MyDashboardPlugin
 */
public class MyDashboardPlugin extends Plugin
{
    /**
     * The name of this plugin
     */
    public static final String PLUGIN_NAME = "mydashboard";

    /**
     * {@inheritDoc}
     */
    @Override
    public void init( )
    {
        // Nothing to do
    }

    /**
     * Get the MyDashboard plugin
     * @return The MyDashboard plugin
     */
    public static Plugin getPlugin( )
    {
        return PluginService.getPlugin( PLUGIN_NAME );
    }

}
