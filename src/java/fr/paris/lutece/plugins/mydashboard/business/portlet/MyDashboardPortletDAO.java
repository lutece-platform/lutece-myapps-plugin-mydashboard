/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.mydashboard.business.portlet;


import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * DAO for MyDashboard portlets
 */
public class MyDashboardPortletDAO implements IPortletInterfaceDAO
{
	 // Constants
    private static final String SQL_QUERY_SELECTALL = "SELECT  id_portlet, id_panel FROM mydashboard_portlet_panel";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECTALL + " WHERE id_portlet = ?";

    private static final String SQL_QUERY_INSERT = "INSERT INTO mydashboard_portlet_panel ( id_portlet, id_panel ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM mydashboard_portlet_panel WHERE id_portlet = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE mydashboard_portlet_panel SET id_portlet = ?, id_panel = ? WHERE id_portlet = ?";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( Portlet portlet )
    {
    	MyDashboardPortlet myPortlet=(MyDashboardPortlet) portlet; 
    	    		
	    int nIndex = 1;
	    try (DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT )){
	         
		   daoUtil.setInt( nIndex++, myPortlet.getId( ) );
		   daoUtil.setInt( nIndex++, myPortlet.getIdPanel(  ) );
		         
           daoUtil.executeUpdate( );
		  
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nPortletId )
    {
    	try(DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE )) {
        
    		daoUtil.setInt( 1, nPortletId );
    		daoUtil.executeUpdate(  );
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Portlet load( int nPortletId )
    {
    	
    	MyDashboardPortlet myDashboardPortlet = new MyDashboardPortlet(  );
        
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT )) {
	        daoUtil.setInt( 1, nPortletId );
	        daoUtil.executeQuery(  );
	        if ( daoUtil.next(  ) )
	        {
	            int nIndex = 1;
	            myDashboardPortlet.setId( daoUtil.getInt( nIndex++ )  );
	            myDashboardPortlet.setIdPanel( daoUtil.getInt( nIndex++ ) );
	        }

        }

        return myDashboardPortlet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Portlet portlet )
    {
    	MyDashboardPortlet myPortlet=(MyDashboardPortlet) portlet; 

    	 try (DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE )){
    		 
    	   int nIndex = 1;
  		   daoUtil.setInt( nIndex++, myPortlet.getId( ) );
  		   daoUtil.setInt( nIndex++, myPortlet.getIdPanel(  ) );

  		   daoUtil.setInt( nIndex++, myPortlet.getId(  ) );

             daoUtil.executeUpdate( );
  		  
      	}
    	
    }
}
