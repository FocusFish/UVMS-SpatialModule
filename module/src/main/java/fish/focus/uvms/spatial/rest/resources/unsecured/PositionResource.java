/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package fish.focus.uvms.spatial.rest.resources.unsecured;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/position")
@SuppressWarnings("unchecked")
public class PositionResource {

    private static final Logger log = LoggerFactory.getLogger(PositionResource.class);

    private static final Map<String, BufferedImage> positionEntries = Collections.synchronizedMap(new LinkedHashMap() {

        private static final int MAX_ENTRIES = 1000;

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {

            return size() > MAX_ENTRIES;
        }
    });

    public static Map<String, BufferedImage> getpositionEntries() {
        return positionEntries;
    }

    @GET
    @Path("/{key}")
    public void getPositionEntry(@PathParam("key") String key,
                               @Context HttpServletResponse response) throws IOException {
        BufferedImage bi = positionEntries.get(key);
        if (bi != null) {
            OutputStream out = response.getOutputStream();
            ImageIO.write(bi, "png", out);
            out.close();
        }
    }

}