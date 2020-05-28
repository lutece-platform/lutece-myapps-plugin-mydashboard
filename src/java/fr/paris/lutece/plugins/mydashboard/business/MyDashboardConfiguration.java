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
package fr.paris.lutece.plugins.mydashboard.business;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;


/**
 * User configuration of dashboards
 */
public class MyDashboardConfiguration implements Comparable<MyDashboardConfiguration>, Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 9094886288827434050L;
    private int _nOrder;
    private String _strIdConfig;
    private String _strMyDashboardComponentId;
    private boolean _bHideDashboard;

    /**
     * Returns the Order
     * @return The Order
     */
    public int getOrder(  )
    {
        return _nOrder;
    }

    /**
     * Sets the Order
     * @param nOrder The Order
     */
    public void setOrder( int nOrder )
    {
        _nOrder = nOrder;
    }

    /**
     * Get the id of the config of this dashboard configuration
     * @return the user name
     */
    public String getIdConfig(  )
    {
        return _strIdConfig;
    }

    /**
     * Set the id of the config of this dashboard configuration
     * @param strIdConfig the id of the config
     */
    public void setIdConfig( String strIdConfig )
    {
        _strIdConfig = strIdConfig;
    }

    /**
     * Get the id of the dashboard component
     * @return The id of the dashboard component
     */
    public String getMyDashboardComponentId(  )
    {
        return _strMyDashboardComponentId;
    }

    /**
     * Set the id of the dashboard component
     * @param strMyDashboardComponentId The id of the dashboard component
     */
    public void setMyDashboardComponentId( String strMyDashboardComponentId )
    {
        _strMyDashboardComponentId = strMyDashboardComponentId;
    }

    /**
     * Check if the dashboard should be hidden for the associated user or not
     * @return True if the dashboard should be hidden for this user, false
     *         otherwise
     */
    public boolean getHideDashboard(  )
    {
        return _bHideDashboard;
    }

    /**
     * Set the visibility of the dashboard for the associated user
     * @param bHideDashboard True if the dashboard should be hidden for this
     *            user, false otherwise
     */
    public void setHideDashboard( boolean bHideDashboard )
    {
        this._bHideDashboard = bHideDashboard;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo( MyDashboardConfiguration o )
    {
        return ( o != null ) ? ( this.getOrder(  ) - o.getOrder(  ) ) : 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object o )
    {
        return ( o instanceof MyDashboardConfiguration )
        ? StringUtils.equals( this.getMyDashboardComponentId(  ),
            ( (MyDashboardConfiguration) o ).getMyDashboardComponentId(  ) ) : false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode(  )
    {
        return ( getMyDashboardComponentId(  ) != null ) ? getMyDashboardComponentId(  ).hashCode(  ) : 0;
    }
}
