/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.fisheries.uvms.spatial.service;

import eu.europa.ec.fisheries.uvms.spatial.message.event.CreateMovementEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.carrier.EventMessage;
import javax.ejb.Local;
import javax.enterprise.event.Observes;

/**
 *
 * @author jojoha
 */
@Local
public interface EventService {

    public void createMovement(@Observes @CreateMovementEvent EventMessage message);

}
