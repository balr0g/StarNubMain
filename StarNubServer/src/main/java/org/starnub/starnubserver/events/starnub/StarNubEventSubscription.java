/*
 * Copyright (C) 2014 www.StarNub.org - Underbalanced
 *
 * This file is part of org.starnub a Java Wrapper for Starbound.
 *
 * This above mentioned StarNub software is free software:
 * you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free
 * Software Foundation, either version  3 of the License, or
 * any later version. This above mentioned CodeHome software
 * is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
 * the GNU General Public License for more details. You should
 * have received a copy of the GNU General Public License in
 * this StarNub Software.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.starnub.starnubserver.events.starnub;

import org.starnub.starnubserver.events.events.StarNubEvent;
import org.starnub.utilities.events.EventSubscription;
import org.starnub.utilities.events.Priority;
import org.starnub.utilities.events.types.Event;

/**
 * Represents StarNubs StarNubEventSubscription that can self register or be manually registered with the
 * {@link StarNubEventRouter}
 *
 * @author Daniel (Underbalanced) (www.StarNub.org)
 * @since 1.0 Beta
 */
public class StarNubEventSubscription extends EventSubscription<Event<String>> {

    private final String EVENT_KEY;

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will set up an events subscription but it will be automatically registered
     *
     * @param SUBSCRIBER_NAME String representing the subscribers name
     * @param EVENT_KEY Class extending Packet which represents the Packet Event Key
     * @param EVENT_HANDLER EventHandler representing the events handler that will do some logic you choose to write
     */
    @SuppressWarnings("unchecked")
    public StarNubEventSubscription(String SUBSCRIBER_NAME, Priority PRIORITY, String EVENT_KEY, StarNubEventHandler EVENT_HANDLER) {
        super(SUBSCRIBER_NAME,
                ((PRIORITY.ordinal() == 0 || PRIORITY.ordinal() == 1) && !SUBSCRIBER_NAME.contains("StarNub")) ?  Priority.MEDIUM : PRIORITY,
                EVENT_HANDLER);
        this.EVENT_KEY = EVENT_KEY;
        register();
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will set up an events subscription and register it if you supplied a true boolean for the value register
     *
     * @param SUBSCRIBER_NAME String representing the subscribers name
     * @param EVENT_KEY Class extending Packet which represents the Packet Event Key
     * @param register boolean do you want to auto register this events
     * @param EVENT_HANDLER EventHandler representing the events handler that will do some logic you choose to write
     */
    @SuppressWarnings("unchecked")
    public StarNubEventSubscription(String SUBSCRIBER_NAME, Priority PRIORITY, String EVENT_KEY, boolean register, StarNubEventHandler EVENT_HANDLER) {
        super(SUBSCRIBER_NAME,
                ((PRIORITY.ordinal() == 0 || PRIORITY.ordinal() == 1) && !SUBSCRIBER_NAME.contains("StarNub")) ?  Priority.MEDIUM : PRIORITY,
                EVENT_HANDLER);
        this.EVENT_KEY = EVENT_KEY;
        if (register) {
            register();
        }
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will register this Event Subscription with the {@link StarNubEventRouter} - If you used auto register DO NOT USE this
     */
    @Override
    public void register() {
        StarNubEventRouter.getInstance().registerEventSubscription(EVENT_KEY, this);
        new StarNubEvent("StarNub_Event_Subscription_Registered", this);
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will remove this Event Subscription from the {@link StarNubEventRouter}
     */
    @Override
    public void unregister() {
        StarNubEventRouter.getInstance().removeEventSubscription(this);
        new StarNubEvent("StarNub_Event_Subscription_Registration_Removed", this);
    }
}
