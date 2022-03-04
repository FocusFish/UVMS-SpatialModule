/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package fish.focus.uvms.spatial.service.bean;

import javax.ejb.Singleton;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import fish.focus.uvms.init.AbstractModuleInitializerBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class SpatialInitializerBean extends AbstractModuleInitializerBean {

    private static final Logger LOG = LoggerFactory.getLogger(SpatialInitializerBean.class);

    public static final String PROP_FILE_NAME = "config.properties";
    public static final String PROP_USM_DESCRIPTOR_FORCE_UPDATE = "usm_deployment_descriptor_force_update";

    protected InputStream getDeploymentDescriptorRequest() {
        return SpatialInitializerBean.class.getClassLoader().getResourceAsStream("usmDeploymentDescriptor.xml");
    }

    protected boolean mustRedeploy() {
        boolean isMustRedploy = false;
        String envVariable = System.getenv().get(PROP_USM_DESCRIPTOR_FORCE_UPDATE);

        if (StringUtils.isNotBlank(envVariable)) {
            LOG.info("You have environment variable {}, which overrides the same configuration in {} of Spatial.ear. The value is {}.", PROP_USM_DESCRIPTOR_FORCE_UPDATE, PROP_FILE_NAME, envVariable);
            isMustRedploy = Boolean.valueOf(envVariable);
        } else {
            try {
                Properties moduleConfigs = retrieveModuleConfigs();
                isMustRedploy = Boolean.valueOf(moduleConfigs.getProperty(PROP_USM_DESCRIPTOR_FORCE_UPDATE));
                LOG.info("{} file contains a configuration {}, with the following value {}", PROP_FILE_NAME, PROP_USM_DESCRIPTOR_FORCE_UPDATE, isMustRedploy);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
                LOG.info("No {} file with property {} was configured. The default behavior is to skip USM deployment if application has already been deployed.", PROP_FILE_NAME, PROP_USM_DESCRIPTOR_FORCE_UPDATE);
                //in case we can't retrieve a configuration, the default behavior is skipping redeployment
            }
        }
        return isMustRedploy;
    }

    private Properties retrieveModuleConfigs() throws IOException {
        Properties prop = new Properties();
        InputStream properties = SpatialInitializerBean.class.getClassLoader().getResourceAsStream(PROP_FILE_NAME);
        if (properties != null) {
            prop.load(properties);
            return prop;
        } else {
            throw new FileNotFoundException("Property file '" + PROP_FILE_NAME + "' not found in the classpath");
        }
    }
}