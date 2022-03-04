/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package fish.focus.uvms.spatial.service.message;

import fish.focus.uvms.commons.message.api.Fault;
import fish.focus.uvms.commons.message.impl.JAXBUtils;
import fish.focus.uvms.spatial.model.enums.FaultCode;
import fish.focus.uvms.spatial.model.exception.SpatialModelMarshallException;
import fish.focus.uvms.spatial.model.schemas.*;
import fish.focus.uvms.spatial.service.bean.MapConfigServiceBean;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static fish.focus.uvms.commons.message.api.MessageConstants.CONNECTION_TYPE;
import static fish.focus.uvms.commons.message.api.MessageConstants.DESTINATION_TYPE_QUEUE;
import static fish.focus.uvms.commons.message.api.MessageConstants.QUEUE_MODULE_SPATIAL;
import static fish.focus.uvms.commons.message.api.MessageConstants.QUEUE_MODULE_SPATIAL_NAME;

@MessageDriven(mappedName = QUEUE_MODULE_SPATIAL, activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = QUEUE_MODULE_SPATIAL_NAME)
})
public class SpatialEventMDB implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(SpatialEventMDB.class);

    @Inject
    private SpatialProducer producer;

    @Inject
    private MapConfigServiceBean mapConfigServiceBean;

    @Override
    public void onMessage(Message message) {

        TextMessage textMessage = (TextMessage) message;

        try {

            SpatialModuleRequest request = JAXBUtils.unMarshallMessage(textMessage.getText(), SpatialModuleRequest.class);
            SpatialModuleMethod method = request.getMethod();

            switch (method) {
                case GET_MAP_CONFIGURATION:
                    SpatialGetMapConfigurationRQ spatialGetMapConfigurationRQ = JAXBUtils.unMarshallMessage(textMessage.getText(), SpatialGetMapConfigurationRQ.class);
                    MapConfigurationType configuration = mapConfigServiceBean.getMapConfigurationType(spatialGetMapConfigurationRQ.getReportId(), spatialGetMapConfigurationRQ.getPermittedServiceLayers());
                    String response = SpatialModuleResponseMapper.mapSpatialGetMapConfigurationResponse(new SpatialGetMapConfigurationRS(configuration));
                    producer.sendResponseMessageToSender(textMessage, response);
                    break;
                case SAVE_OR_UPDATE_MAP_CONFIGURATION:
                    SpatialSaveOrUpdateMapConfigurationRQ spatialSaveMapConfigurationRQ = JAXBUtils.unMarshallMessage(textMessage.getText(), SpatialSaveOrUpdateMapConfigurationRQ.class);
                    SpatialSaveOrUpdateMapConfigurationRS saveOrUpdateMapConfigurationRS = mapConfigServiceBean.handleSaveOrUpdateSpatialMapConfiguration(spatialSaveMapConfigurationRQ);
                    String mapConfigurationResponse = SpatialModuleResponseMapper.mapSpatialSaveOrUpdateMapConfigurationRSToString(saveOrUpdateMapConfigurationRS);
                    producer.sendResponseMessageToSender(textMessage, mapConfigurationResponse);
                    break;
                case DELETE_MAP_CONFIGURATION:
                    SpatialDeleteMapConfigurationRQ mapConfigurationRQ = JAXBUtils.unMarshallMessage(textMessage.getText(), SpatialDeleteMapConfigurationRQ.class);
                    mapConfigServiceBean.handleDeleteMapConfiguration(mapConfigurationRQ);
                    String value = JAXBUtils.marshallJaxBObjectToString(JAXBUtils.marshallJaxBObjectToString(new SpatialDeleteMapConfigurationRS()));
                    producer.sendResponseMessageToSender(textMessage, value);
                    break;
                case PING:
                    PingRS pingRS = new PingRS();
                    pingRS.setResponse("pong");
                    producer.sendResponseMessageToSender(textMessage, SpatialModuleResponseMapper.mapPingResponse(pingRS));
                    break;
                default:
                    log.error("[ Not implemented method consumed: {} ]", method);
                    Fault fault = new Fault(FaultCode.SPATIAL_MESSAGE.getCode(), "Method not implemented");
                    producer.sendFault(textMessage,fault);
            }

        } catch (JMSException | JAXBException | SpatialModelMarshallException e) {
            Fault fault = new Fault(FaultCode.SPATIAL_MESSAGE.getCode(), "ERROR OCCURRED IN SPATIAL MDB");
            producer.sendFault(textMessage, fault);
        }
    }
}