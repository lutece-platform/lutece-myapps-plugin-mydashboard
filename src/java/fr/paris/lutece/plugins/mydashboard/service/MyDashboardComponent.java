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
package fr.paris.lutece.plugins.mydashboard.service;

import fr.paris.lutece.portal.service.security.LuteceUser;

import org.apache.commons.lang.ObjectUtils;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.util.Assert;


/**
 * Dash board Component
 */
public abstract class MyDashboardComponent implements IMyDashboardComponent, InitializingBean
{
    private static final int MY_DASHBOARD_COMPONENT_ID_MAXIMUM_SIZE = 50;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAvailable( LuteceUser user )
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo( IMyDashboardComponent o )
    {
        return ( o != null ) ? getComponentId(  ).compareTo( o.getComponentId(  ) ) : 1;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object obj )
    {
        if ( obj instanceof IMyDashboardComponent )
        {
            IMyDashboardComponent other = (IMyDashboardComponent) obj;

            return ObjectUtils.equals( this.getComponentId(  ), other.getComponentId(  ) );
        }

        return false;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public int hashCode(  )
    {
        return ObjectUtils.hashCode( this.getComponentId(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet(  ) throws Exception
    {
        Assert.hasLength( getComponentId(  ), "Dashboard components must have a non null id" );

        if ( getComponentId(  ).length(  ) > MY_DASHBOARD_COMPONENT_ID_MAXIMUM_SIZE )
        {
            throw new IllegalArgumentException( "Dashboard components id must not be longer than 50 characters (" +
                getComponentId(  ) + " has " + getComponentId(  ).length(  ) + ")" );
        }
    }
}
