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

import java.io.Serializable;


/**
 * This is the business class for the object DashboardAssociation
 */
public class DashboardAssociation implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations 
    private int _nId;
    private String _strIdDashboard;
    private int _nIdPanel;
    private int _nPosition;

    /**
     * Returns the Id
     * @return The Id
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * @param nId The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the IdDashboard
     * @return The IdDashboard
     */
    public String getIdDashboard(  )
    {
        return _strIdDashboard;
    }

    /**
     * Sets the IdDashboard
     * @param nIdDashboard The IdDashboard
     */
    public void setIdDashboard( String strIdDashboard )
    {
        _strIdDashboard = strIdDashboard;
    }

    /**
     * Returns the IdPannel
     * @return The IdPannel
     */
    public int getIdPanel(  )
    {
        return _nIdPanel;
    }

    /**
     * Sets the IdPannel
     * @param nIdPannel The IdPannel
     */
    public void setIdPanel( int nIdPannel )
    {
        _nIdPanel = nIdPannel;
    }

    /**
     * Returns the Position
     * @return The Position
     */
    public int getPosition(  )
    {
        return _nPosition;
    }

    /**
     * Sets the Position
     * @param nPosition The Position
     */
    public void setPosition( int nPosition )
    {
        _nPosition = nPosition;
    }
}
