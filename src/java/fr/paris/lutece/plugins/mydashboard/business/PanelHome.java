/*
 * Copyright (c) 2002-2016, Mairie de Paris
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
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for Panel objects
 */
public final class PanelHome
{
    // Static variable pointed at the DAO instance
    private static IPanelDAO _dao = SpringContextService.getBean( "mydashboard.panelDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "mydashboard" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private PanelHome(  )
    {
    }

    /**
     * Create an instance of the panel class
     * @param panel The instance of the Panel which contains the informations to store
     * @return The  instance of panel which has been created with its primary key.
     */
    public static Panel create( Panel panel )
    {
        _dao.insert( panel, _plugin );

        return panel;
    }

    /**
     * Update of the panel which is specified in parameter
     * @param panel The instance of the Panel which contains the data to store
     * @return The instance of the  panel which has been updated
     */
    public static Panel update( Panel panel )
    {
        _dao.store( panel, _plugin );

        return panel;
    }

    /**
     * Remove the panel whose identifier is specified in parameter
     * @param nKey The panel Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a panel whose identifier is specified in parameter
     * @param nKey The panel primary key
     * @return an instance of Panel
     */
    public static Panel findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }
    /**
     * Returns an instance of a panel whose identifier is specified in parameter
     * @param nKey The panel primary key
     * @return an instance of Panel
     */
    public static Panel findByCode(String strCode )
    {
        return _dao.loadByCode( strCode, _plugin);
    }

    /**
     * Load the data of all the panel objects and returns them as a list
     * @return the list which contains the data of all the panel objects
     */
    public static List<Panel> getPanelsList( )
    {
        return _dao.selectPanelsList( _plugin );
    }
    
    /**
     * Load the id of all the panel objects and returns them as a list
     * @return the list which contains the id of all the panel objects
     */
    public static List<Integer> getIdPanelsList( )
    {
        return _dao.selectIdPanelsList( _plugin );
    }
    
    /**
     * Load the data of all the panel objects and returns them as a referenceList
     * @return the referenceList which contains the data of all the panel objects
     */
    public static ReferenceList getPanelsReferenceList( )
    {
        return _dao.selectPanelsReferenceList(_plugin );
    }
    
    /**
     * Returns  the default panel
     * @param nKey The panel primary key
     * @return an instance of Panel
     */
    public static Panel getDefaultPanel( )
    {
       Panel panel=null;
       List<Panel> listPanel=getPanelsList();
       if(listPanel!=null && listPanel.size()>0)
       {
    	   panel=listPanel.get(0);
    	   
       }
       return panel;
    }
    
    
}

