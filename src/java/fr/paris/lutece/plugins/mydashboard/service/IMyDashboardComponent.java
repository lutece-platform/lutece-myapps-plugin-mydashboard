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
package fr.paris.lutece.plugins.mydashboard.service;

import fr.paris.lutece.portal.service.security.LuteceUser;

import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;


/**
 * Interface for dashboard components. Each bean implementing this interface
 * will add a dashboard
 * <p>
 * HTML escaping contract: {@link #getDashboardData} returns HTML the component rendered, usually with
 * {@code template.getHtml( )}, from a template where every value is auto-escaped. plugin-mydashboard marks this HTML
 * as markup where it puts it in its own models ({@code HtmlMarkup.ofAll}, dashboard page and dashboard portlet), so a
 * component returns it as a {@code String}, never escapes it again, and never puts in it a value it did not escape (a
 * value typed by a user, a request parameter, data of another application) outside a template.
 * {@link #getComponentDescription} is text: the templates escape it.
 * </p>
 */
public interface IMyDashboardComponent extends Comparable<IMyDashboardComponent>
{
    /**
     * Gets dash board data for the current user
     * @param request HttpServletRequest
     * @return HTML content to insert into a dash board zone: HTML rendered by the component, written as HTML in the
     *         dashboard page and the dashboard portlet; may be null or empty when there is nothing to show
     */
    String getDashboardData( HttpServletRequest request );

    /**
     * Gets the id of the dashboard component. The id of the dashboard component
     * must be unique for each dashboard component, and be less than 50
     * characters long.
     * @return The id of the dashboard component
     */
    String getComponentId(  );

    /**
     * Get the description of the component
     * @param locale The locale to display the description in
     * @return The description of the component, as text (escaped where it is displayed)
     */
    String getComponentDescription( Locale locale );

    /**
     * Check if the dashboard is available or not for a given Lutece User
     * @param user The user
     * @return True if the dashboard is available for the given user, false
     *         otherwise
     */
    boolean isAvailable( LuteceUser user );
}
