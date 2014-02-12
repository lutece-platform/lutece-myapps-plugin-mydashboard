/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.mydashboard.business;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


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
    String getNewConfigId(  );

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
