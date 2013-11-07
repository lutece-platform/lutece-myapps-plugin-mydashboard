/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reservimport fr.paris.lutece.plugins.mydashboard.web.MyDashboardApp;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppPathService;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
cumentation and/or other materials
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
package fr.paris.lutece.plugins.mydashboard.modules.nickname.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.mydashboard.web.MyDashboardApp;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppPathService;


/**
 * Application to change user nicknames
 */
public class MyDashboardNicknameApp
{
    private static final String PARAMETER_NICKNAME = "nickname";

    private static final String HTML_PARAMETER_REFERER = "referer";

    /**
     * Do change the nickname of the user
     * @param request The request
     * @return The next URL to redirect to
     */
    public static String doChangeUserNickname( HttpServletRequest request )
    {
        LuteceUser user = SecurityService.isAuthenticationEnable( ) ? SecurityService.getInstance( ).getRegisteredUser(
                request ) : null;
        if ( user != null )
        {
            String strNickname = request.getParameter( PARAMETER_NICKNAME );
            UserPreferencesService.instance( ).setNickname( user.getName( ), strNickname );
        }

        String strReferer = request.getHeader( HTML_PARAMETER_REFERER );

        if ( StringUtils.isEmpty( strReferer ) )
        {
            return MyDashboardApp.getUrlDefaultView( AppPathService.getBaseUrl( request ) );
        }

        return strReferer;
    }
}
