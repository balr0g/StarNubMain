/*
* Copyright (C) 2014 www.StarNub.org - Underbalanced
*
* This utilities.file is part of org.starnub a Java Wrapper for Starbound.
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

package server.server.starbound;

import lombok.Getter;
import lombok.Setter;
import org.codehome.utilities.timers.ThreadSleep;
import server.StarNub;
import server.eventsrouter.events.StarboundServerStatusEvent;
import server.eventsrouter.events.StarboundQueryTaskEvent;






//TODO RETIRE




/**
 * Represents StatusProcessor for checking the Starbound
 * Process and Responsiveness
 * <p>
 * @author Daniel (Underbalanced) (www.StarNub.org)
 * @since 1.0
 */
class OLDStatusQuery implements Runnable {

    /**
     * Constructor which sets the type of query "startup" or "responsive"
     * @param typeString String representing the type of query
     */
    public OLDStatusQuery(String typeString) {
        type = typeString;
    }

    /**
     * String representing type of query,
     * "startup" or "responsive".
     */

    private volatile static String type;

    /**
     * We need to keep track of query attempts
     */

    protected volatile static int queryAttempts = 0;

    /**
     * Starbound Server query success
     */

    private volatile static boolean online;

    /**
     * Depending on the query type the this runnable will
     * attempt to reach the starbounddata.packets.starbounddata.packets.server. If we are starting Starbound
     * we will check every 5 seconds, other wise we check every
     * 10 seconds (12 Tries) for a total of 120 seconds to reach
     * the starbounddata.packets.starbounddata.packets.server. If the starbounddata.packets.starbounddata.packets.server is reached, not reached, this
     * will send an event out of the final connectionstatus, along with completing
     * the task state to allow StarboundManagement use.
     */
    public void run() {
        StarboundQueryTaskEvent.eventSend_Server_Query_Task_Running();
        OLDQueryClient OLDQueryClient = new OLDQueryClient();
        OLDQueryClient.serverQuery();

        boolean firstloop = true;
        while (!isOnline()) {

            /* We want to make sure the process is not crashed
             * while checking the connectionstatus */
            if (!StarboundManagement.isProcessRunning()) {
                StarboundServerStatusEvent.eventSend_Starbound_Server_Status_Crashed();
                StarboundQueryTaskEvent.eventSend_Server_Query_Task_Complete();
                return;
            }

            if (type.equalsIgnoreCase("startup")) {

                if (!firstloop) {
                    StarboundServerStatusEvent.eventSend_Starbound_Server_Status_Starting_Up_Still();
                }
                new ThreadSleep().timerSeconds(5);
                firstloop = false;

            } else if (type.equalsIgnoreCase("responsiveness")) {

                if (queryAttempts < 12) {
                    queryAttempts += 1;
                    StarboundServerStatusEvent.eventSend_Starbound_Server_Status_TCPQuery_Failed(queryAttempts);
                    new ThreadSleep().timerSeconds(10);

                } else if (queryAttempts >= 12) {

                    StarboundServerStatusEvent.eventSend_Starbound_Server_Status_Unresponsive();
                    StarboundQueryTaskEvent.eventSend_Server_Query_Task_Complete();
                    return;

                }

            } else {
                StarNub.getLogger().cErrPrint("StarNub", "During StarNubs attempt to contact the Starbound starbounddata.packets.starbounddata.packets.server it did not" +
                        "recognize the type of Query request. ");
                StarboundQueryTaskEvent.eventSend_Server_Query_Task_Complete();
                return;
            }
            OLDQueryClient.serverQuery();
        }
        if (isOnline() && type.equalsIgnoreCase("responsiveness")) {
            StarboundServerStatusEvent.eventSend_Starbound_Server_Status_Responsive();
            StarboundQueryTaskEvent.eventSend_Server_Query_Task_Complete();
        } else if (isOnline() && type.equalsIgnoreCase("startup")) {
            StarboundServerStatusEvent.eventSend_Starbound_Server_Status_Online();
            StarboundQueryTaskEvent.eventSend_Server_Query_Task_Complete();
        }
    }
}