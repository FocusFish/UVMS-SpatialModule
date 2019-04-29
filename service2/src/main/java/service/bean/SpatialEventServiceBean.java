/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package service.bean;

import eu.europa.ec.fisheries.uvms.commons.message.api.Fault;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.spatial.message.bean.SpatialProducer;
import eu.europa.ec.fisheries.uvms.spatial.model.enums.FaultCode;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.MapConfigService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialService;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import java.util.List;

@Stateless
@LocalBean
@Slf4j
public class SpatialEventServiceBean {

    private static final String MODULE_NAME = "spatial";

    @EJB
    private SpatialService spatialService;

    @EJB
    private SpatialEnrichmentServiceBean enrichmentService;

    @EJB
    private AreaService areaService;

    @EJB
    private MapConfigService mapConfigService;

    @EJB
    private AreaTypeNamesService areaTypeNamesService;

    @EJB
    private SpatialProducer messageProducer;


    public void getAreaByLocation(@Observes @GetAreaByLocationEvent SpatialMessageEvent message) {
        log.info("Getting area by location.");
        try {
            List<AreaExtendedIdentifierType> areaTypesByLocation = areaService.getAreasByPoint(message.getAreaByLocationSpatialRQ());
            log.debug("Send back areaByLocation response.");
            messageProducer.sendResponseMessageToSender(message.getMessage(), SpatialModuleResponseMapper.mapAreaByLocationResponse(areaTypesByLocation), MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }


    public void getAreaTypeNames(@Observes @GetAreaTypeNamesEvent SpatialMessageEvent message) {
        log.info("Getting area names.");
        try {
            List<String> areaTypeNames = areaTypeNamesService.listAllAreaTypeNames();
            log.debug("Send back area types response.");
            messageProducer.sendResponseMessageToSender(message.getMessage(), SpatialModuleResponseMapper.mapAreaTypeNamesResponse(areaTypeNames), MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }

    private void sendError(SpatialMessageEvent message, Exception e) {
        log.error("[ Error in spatial module. ] ", e);
        messageProducer.sendFault(message.getMessage(), new Fault(FaultCode.SPATIAL_MESSAGE.getCode(),"Exception in spatial [ " + e.getMessage()));
    }


    public void getClosestArea(@Observes @GetClosestAreaEvent SpatialMessageEvent message) {
        log.info("Getting closest area.");
        try {
            ClosestAreaSpatialRQ request = message.getClosestAreaSpatialRQ();
            Double lat = request.getPoint().getLatitude();
            Double lon = request.getPoint().getLongitude();
            Integer crs = request.getPoint().getCrs();
            UnitType unit = request.getUnit();
            List<Area> closestAreas = areaService.getClosestArea(lon, lat, crs, unit);
            log.debug("Send back closestAreas response.");
            messageProducer.sendResponseMessageToSender(message.getMessage(), SpatialModuleResponseMapper.mapClosestAreaResponse(closestAreas), MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }


    public void getClosestLocation(@Observes @GetClosestLocationEvent SpatialMessageEvent message) {
        log.info("Getting closest locations.");
        try {
            List<Location> closestLocations = areaService.getClosestPointByPoint(message.getClosestLocationSpatialRQ());
            log.debug("Send back closest locations response.");
            messageProducer.sendResponseMessageToSender(message.getMessage(), SpatialModuleResponseMapper.mapClosestLocationResponse(closestLocations), MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }


    public void areaByCode(@Observes @AreaByCodeEvent SpatialMessageEvent message) {
        log.info("Getting area by code");
        try {
            AreaByCodeRequest areaByCodeRequest = message.getAreaByCodeRequest();
            List<AreaSimpleType> areaSimples = areaByCodeRequest.getAreaSimples();
            List<AreaSimpleType> areaSimpleTypeList = areaService.getAreasByCode(areaSimples);

            AreaByCodeResponse areaByCodeRes = new AreaByCodeResponse();
            areaByCodeRes.setAreaSimples(areaSimpleTypeList);

            messageProducer.sendResponseMessageToSender(message.getMessage(), SpatialModuleResponseMapper.mapAreaByCodeResponseToString(areaByCodeRes), MODULE_NAME);

        } catch (Exception e) {
            log.error("[ Error when responding to area by code. ] ", e);
            sendError(message, e);
        }
    }


    public void getSpatialEnrichment(@Observes @GetSpatialEnrichmentEvent SpatialMessageEvent message) {
        log.info("Getting spatial enrichment.");
        try {
            SpatialEnrichmentRS spatialEnrichmentRS = enrichmentService.getSpatialEnrichment(message.getSpatialEnrichmentRQ());
            log.debug("Send back enrichment response.");
            messageProducer.sendResponseMessageToSender(message.getMessage(), SpatialModuleResponseMapper.mapEnrichmentResponse(spatialEnrichmentRS), MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }


    public void getBatchSpatialEnrichment(@Observes @GetSpatialBatchEnrichmentEvent SpatialMessageEvent message) {
        log.info("Getting Spatial BATCH Enrichment.");
        try {
            BatchSpatialEnrichmentRS spatialEnrichmentRS = enrichmentService.getBatchSpatialEnrichment(message.getSpatialBatchEnrichmentRQ());
            log.info("Enrich BATCH was Successful.. Response sent back to [ {} ] queue.", message.getMessage().getJMSReplyTo());
            messageProducer.sendResponseMessageToSender(message.getMessage(), SpatialModuleResponseMapper.mapToBatchEnrichmentResponse(spatialEnrichmentRS), MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }


    public void deleteMapConfiguration(@Observes @DeleteMapConfigurationEvent SpatialMessageEvent message) {
        log.info("Delete mapDefaultSRIDToEPSG configurations.");
        try {
            mapConfigService.handleDeleteMapConfiguration(message.getSpatialDeleteMapConfigurationRQ());
            log.debug("Send back mapDefaultSRIDToEPSG configurations response.");
            String value = JAXBUtils.marshallJaxBObjectToString(JAXBUtils.marshallJaxBObjectToString(new SpatialDeleteMapConfigurationRS()));
            messageProducer.sendResponseMessageToSender(message.getMessage(), value, MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }


    public void getFilterAreas(@Observes @GetFilterAreaEvent SpatialMessageEvent message) {
        log.info("Getting Filter Areas");
        try {
            FilterAreasSpatialRQ filterAreaSpatialRQ = message.getFilterAreasSpatialRQ();
            FilterAreasSpatialRS filterAreasSpatialRS = areaService.computeAreaFilter(filterAreaSpatialRQ);
            log.debug("Send back filtered Areas");
            messageProducer.sendResponseMessageToSender(message.getMessage(), SpatialModuleResponseMapper.mapFilterAreasResponse(filterAreasSpatialRS), MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }


    public void saveOrUpdateSpatialMapConfiguration(@Observes @SaveOrUpdateMapConfigurationEvent SpatialMessageEvent message) {
        log.info("Saving/Updating mapDefaultSRIDToEPSG configurations.");
        try {
            SpatialSaveOrUpdateMapConfigurationRS saveOrUpdateMapConfigurationRS = mapConfigService.handleSaveOrUpdateSpatialMapConfiguration(message.getSpatialSaveOrUpdateMapConfigurationRQ());
            log.debug("Send back mapDefaultSRIDToEPSG configurations response.");
            String response = SpatialModuleResponseMapper.mapSpatialSaveOrUpdateMapConfigurationRSToString(saveOrUpdateMapConfigurationRS);
            messageProducer.sendResponseMessageToSender(message.getMessage(), response, MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }


    public void getMapConfiguration(@Observes @GetMapConfigurationEvent SpatialMessageEvent message) {
        log.info("Getting mapDefaultSRIDToEPSG configurations.");
        try {
            SpatialGetMapConfigurationRS mapConfigurationRS = mapConfigService.getMapConfiguration(message.getSpatialGetMapConfigurationRQ());
            log.debug("Send back mapDefaultSRIDToEPSG configurations response.");
            String response = SpatialModuleResponseMapper.mapSpatialGetMapConfigurationResponse(mapConfigurationRS);
            messageProducer.sendResponseMessageToSender(message.getMessage(), response, MODULE_NAME);
        } catch (Exception e) {
            sendError(message, e);
        }
    }


    public void ping(@Observes @PingEvent SpatialMessageEvent message) {
        log.info("Getting Filter Areas");
        try {
            PingRS pingRS = new PingRS();
            pingRS.setResponse("pong");
            messageProducer.sendResponseMessageToSender(message.getMessage(), SpatialModuleResponseMapper.mapPingResponse(pingRS), MODULE_NAME);
        } catch (Exception e) {
            log.error("[ Error when responding to ping. ] ", e);
            sendError(message, e);
        }
    }


    public void getGeometryForPortCode(@Observes @GetGeometryByPortCodeEvent SpatialMessageEvent message) {
        log.info("Getting area by code");
        try {
            GeometryByPortCodeRequest geometryByPortCodeRequest = message.getGeometryByPortCodeRequest();
            String portCode=geometryByPortCodeRequest.getPortCode();
            String geometry= areaService.getGeometryForPort(portCode);

            GeometryByPortCodeResponse geometryByPortCodeResponse = new GeometryByPortCodeResponse();
            geometryByPortCodeResponse.setPortGeometry(geometry);

            messageProducer.sendResponseMessageToSender(message.getMessage(), SpatialModuleResponseMapper.mapGeometryByPortCodeResponse(geometryByPortCodeResponse), MODULE_NAME);

        } catch (Exception e) {
            log.error("[ Error when responding to area by code. ] ", e);
            sendError(message, e);
        }
    }

}