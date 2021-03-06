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

package org.starnub.starnubserver.events.packet;

import org.starnub.starbounddata.packets.Packet;
import org.starnub.starbounddata.packets.Packets;
import org.starnub.starnubserver.StarNub;
import org.starnub.starnubserver.events.events.StarNubEvent;
import org.starnub.utilities.events.EventSubscription;
import org.starnub.utilities.events.Priority;

/**
 * Represents StarNubs PacketEventSubscription that can self register or be manually registered with the
 * {@link PacketEventRouter}
 *
 * @author Daniel (Underbalanced) (www.StarNub.org)
 * @since 1.0 Beta
 */
public class PacketEventSubscription extends EventSubscription<Packet> {

    private final Class<? extends Packet> EVENT_KEY;

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will set up an events subscription ane it will be automatically be registered.
     *
     * @param SUBSCRIBER_NAME String representing the subscribers name
     * @param EVENT_KEY Class extending Packet which represents the Packet Event Key
     * @param EVENT_HANDLER EventHandler representing the events handler that will do some logic you choose to write
     */
    public PacketEventSubscription(String SUBSCRIBER_NAME, Priority PRIORITY, Class<? extends Packet> EVENT_KEY, PacketEventHandler EVENT_HANDLER) {
        super(SUBSCRIBER_NAME,
                ((PRIORITY.ordinal() == 0 || PRIORITY.ordinal() == 1) && !SUBSCRIBER_NAME.contains("StarNub")) ?  Priority.MEDIUM : PRIORITY,
                EVENT_HANDLER);
        this.EVENT_KEY = EVENT_KEY;
        register();
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will set up an events subscription and register it if you supplied a true boolean for the value register else you must getResults .register();
     *
     * @param SUBSCRIBER_NAME String representing the subscribers name
     * @param EVENT_KEY Class extending Packet which represents the Packet Event Key
     * @param register boolean do you want to auto register this events
     * @param EVENT_HANDLER EventHandler representing the events handler that will do some logic you choose to write
     */
    public PacketEventSubscription(String SUBSCRIBER_NAME, Priority PRIORITY, Class<? extends Packet> EVENT_KEY, boolean register, PacketEventHandler EVENT_HANDLER) {
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
     * Uses: This will register this Event Subscription with the {@link PacketEventRouter} - If you used auto register DO NOT USE this
     */
    @Override
    public void register() {
        boolean decoding = (boolean) StarNub.getConfiguration().getNestedValue("advanced_settings", "packet_decoding");
        String packetClassString = EVENT_KEY.toString();
        String subString = packetClassString.substring(packetClassString.lastIndexOf(".") + 1) + ".class";
        Packets packet = Packets.fromString(subString);
        boolean packetEventUsed = packet != null &&  packet.getDirection() != Packet.Direction.NOT_USED;
        if (decoding && packetEventUsed) {
            PacketEventRouter.getInstance().registerEventSubscription(EVENT_KEY, this);
            new StarNubEvent("StarNub_Packet_Subscription_Registered", this);
        }
    }

    /**
     * Recommended: For Plugin Developers & Anyone else.
     * <p>
     * Uses: This will remove this Event Subscription from the {@link PacketEventRouter}
     */
    @Override
    public void unregister() {
        PacketEventRouter.getInstance().removeEventSubscription(this);
        new StarNubEvent("StarNub_Packet_Subscription_Registration_Removed", this);
    }
}
