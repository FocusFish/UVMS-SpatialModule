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
package fish.focus.uvms.spatial.service.bean;

import fish.focus.schema.config.module.v1.ConfigModuleBaseRequest;
import fish.focus.schema.config.types.v1.PullSettingsStatus;
import fish.focus.schema.config.types.v1.SettingType;
import fish.focus.uvms.config.model.mapper.JAXBMarshaller;
import fish.focus.uvms.config.model.mapper.ModuleResponseMapper;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;

@MessageDriven(mappedName = "jms/queue/UVMSConfigEvent", activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = "javax.jms.MessageListener"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "UVMSConfigEvent")})
public class ConfigServiceMock implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigServiceMock.class);

    @Inject
    private JMSContext context;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            ConfigModuleBaseRequest request = JAXBMarshaller.unmarshallTextMessage(textMessage, ConfigModuleBaseRequest.class);
            switch (request.getMethod()) {
                case PULL:
                    SettingType mockSetting = new SettingType();
                    mockSetting.setKey("key");
                    mockSetting.setValue("value");
                    mockSetting.setDescription("From ConfigServiceMock.java");
                    String pullResponse = ModuleResponseMapper.toPullSettingsResponse(Arrays.asList(mockSetting), PullSettingsStatus.OK);
                    TextMessage response = context.createTextMessage(pullResponse);
                    response.setJMSCorrelationID(textMessage.getJMSMessageID());
                    context.createProducer().send(message.getJMSReplyTo(), response);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            LOG.error("Exception in ConfigServiceMock", e);
        }
    }
}
