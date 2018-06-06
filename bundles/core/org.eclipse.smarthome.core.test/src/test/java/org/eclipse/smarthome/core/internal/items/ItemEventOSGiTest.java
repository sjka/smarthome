/**
 * Copyright (c) 2014,2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.smarthome.core.internal.items;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventFilter;
import org.eclipse.smarthome.core.events.EventSubscriber;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.items.events.AbstractItemRegistryEvent;
import org.eclipse.smarthome.core.items.events.ItemAddedEvent;
import org.eclipse.smarthome.core.items.events.ItemRemovedEvent;
import org.eclipse.smarthome.core.items.events.ItemUpdatedEvent;
import org.eclipse.smarthome.core.library.items.SwitchItem;
import org.eclipse.smarthome.test.java.JavaOSGiTest;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

/**
 * The {@link ItemEventOSGiTest} runs inside an OSGi container and tests that the events
 * are generated properly.
 *
 * @author Andre Fuechsel - Initial contribution
 */
public class ItemEventOSGiTest extends JavaOSGiTest {

    private ItemRegistry itemRegistry;
    private final ConcurrentLinkedQueue<Event> receivedEvents = new ConcurrentLinkedQueue<Event>();

    private final static String ITEM_NAME = "switch";
    private final static String ITEM_TAG = "myTag";

    @Before
    public void setUp() {
        registerVolatileStorageService();

        itemRegistry = getService(ItemRegistry.class);
        assertNotNull(itemRegistry);

        EventSubscriber eventSubscriber = new EventSubscriber() {
            @Override
            public void receive(@NonNull Event event) {
                receivedEvents.add(event);
            }

            @Override
            public @NonNull Set<@NonNull String> getSubscribedEventTypes() {
                return ImmutableSet.of(ItemAddedEvent.TYPE, ItemRemovedEvent.TYPE, ItemUpdatedEvent.TYPE);
            }

            @Override
            public @Nullable EventFilter getEventFilter() {
                return null;
            }
        };
        registerService(eventSubscriber);
    }

    @Test
    public void testItemEvents() {
        // add
        Item item = new SwitchItem(ITEM_NAME);
        itemRegistry.add(item);
        waitForAssert(() -> assertTrue("ItemAddedEvent not correct",
                receivedEvents.stream().filter(e -> e.getType().equals(ItemAddedEvent.TYPE)).findFirst().isPresent()));

        // update
        itemRegistry.addTag(ITEM_NAME, ITEM_TAG);
        item = itemRegistry.get(ITEM_NAME);
        assertThat(item, is(notNullValue()));
        if (item != null) {
            itemRegistry.update(item);
            waitForAssert(() -> assertTrue("ItemUpdatedEvent not correct",
                    receivedEvents.stream()
                            .filter(e -> e.getType().equals(ItemUpdatedEvent.TYPE)
                                    && ((AbstractItemRegistryEvent) e).getItem().tags.stream()
                                            .anyMatch(t -> ITEM_TAG.equals(t)))
                            .findFirst().isPresent()));

            // remove
            itemRegistry.remove(ITEM_NAME);
            waitForAssert(() -> assertTrue("ItemRemovedEvent not correct",
                    receivedEvents.stream()
                            .filter(e -> e.getType().equals(ItemRemovedEvent.TYPE)
                                    && ((AbstractItemRegistryEvent) e).getItem().tags.stream()
                                            .anyMatch(t -> ITEM_TAG.equals(t)))
                            .findFirst().isPresent()));
        }
    }
}