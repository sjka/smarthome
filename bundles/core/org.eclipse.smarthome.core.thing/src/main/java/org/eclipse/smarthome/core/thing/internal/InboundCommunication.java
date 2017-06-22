/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.internal;

import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;

/**
 * This interface provides (internal) access to the {@link CommunicationManager}.
 *
 * It is tailored to handle the inbound communication, i.e. state updates, commands and triggers coming from the
 * binding. It allows the {@link ThingManager} to delegate such aspects of the {@link ThingHandlerCallback} internally
 * to the {@link CommunicationManager}.
 *
 * @author Simon Kaufmann - initial contribution and API
 *
 */
public interface InboundCommunication {

    public void stateUpdated(ChannelUID channelUID, State state);

    public void postCommand(ChannelUID channelUID, Command command);

    void channelTriggered(Thing thing, ChannelUID channelUID, String event);

}
