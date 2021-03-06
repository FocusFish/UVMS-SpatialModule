/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.
This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package fish.focus.uvms.spatial.rest;

import fish.focus.uvms.constants.AuthConstants;
import fish.focus.uvms.rest.security.UserRoleRequestWrapper;
import fish.focus.uvms.spatial.model.schemas.SpatialFeaturesEnum;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthenticationFilterMock implements Filter {
    
    public static final String TEST_USER = "MOCK_USER";
    
    public AuthenticationFilterMock() {
        super();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.addHeader(AuthConstants.HTTP_HEADER_AUTHORIZATION, "MOCK_TOKEN");
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Set<String> roles = new HashSet<>();
        for (SpatialFeaturesEnum feature: SpatialFeaturesEnum.values()) {
            roles.add(feature.value());
        }
        UserRoleRequestWrapper arequest = new UserRoleRequestWrapper( TEST_USER, roles, httpRequest);
        chain.doFilter(arequest, httpResponse);
    }
    
    @Override
    public void init(FilterConfig fc) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
