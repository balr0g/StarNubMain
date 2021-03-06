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

package org.starnub.starnubserver.resources.connections.handlers;

import org.starnub.starbounddata.packets.Packet;
import org.starnub.starbounddata.packets.connection.ConnectResponsePacket;
import org.starnub.starnubserver.Connections;
import org.starnub.starnubserver.StarNub;
import org.starnub.starnubserver.StarNubTaskManager;
import org.starnub.starnubserver.cache.objects.RejectionCache;
import org.starnub.starnubserver.connections.player.character.PlayerCharacter;
import org.starnub.starnubserver.connections.player.session.PlayerSession;
import org.starnub.starnubserver.events.events.StarNubEvent;
import org.starnub.starnubserver.events.packet.PacketEventHandler;

public class ConnectionResponseHandler implements PacketEventHandler {

    private final Connections CONNECTIONS;

    public ConnectionResponseHandler(Connections CONNECTIONS) {
        this.CONNECTIONS = CONNECTIONS;
    }

    /**
     * Recommended: For connections use with StarNub
     * <p>
     * Uses: Handles Server Connection Response Packets which are replies to the Connection Attempts. Step 2 of 2 (ClientConnectPacket is Part 1).
     *
     * @param eventData Packet representing the packet being routed
     */
    @Override
    public void onEvent(Packet eventData) {
        ConnectResponsePacket connectResponsePacket = (ConnectResponsePacket) eventData;
        RejectionCache rejectionCache = (RejectionCache) CONNECTIONS.getCONNECTED_PLAYERS().getACCEPT_REJECT().removeCache(connectResponsePacket.getDESTINATION_CTX());
        PlayerSession playerSession;
        if (rejectionCache != null) {
            playerSession = rejectionCache.getPLAYERSession();
            if (rejectionCache.isREJECTED()) {
                connectResponsePacket.makeUnsuccessful();
                connectResponsePacket.setRejectionReason(rejectionCache.getPACKET_MESSAGE());
            } else {
                CONNECTIONS.getCONNECTED_PLAYERS().put(playerSession.getCONNECTION().getCLIENT_CTX(), playerSession);
            }
            final RejectionCache finalRejectionCache = rejectionCache;
            StarNubTaskManager.getInstance().execute(() -> postProcessing(playerSession, (int) connectResponsePacket.getClientId(), finalRejectionCache));
        }
    }

    /**
     * Recommended: For connections use with StarNub
     * <p>
     * Uses: This will handle post processing of this connection
     *
     * @param playerSession Player representing the player being post processed
     * @param starboundClientId int representing the starbound starnubclient id
     * @param rejectionCache RejectionCache representing the reason for rejection
     */
    private void postProcessing(PlayerSession playerSession, int starboundClientId, RejectionCache rejectionCache) {
        PlayerCharacter playerCharacter = playerSession.getPlayerCharacter();
        String characterName = playerCharacter.getCleanName();
        if (!rejectionCache.isREJECTED()) {
            playerSession.setStarboundClientId(starboundClientId);
            new StarNubEvent("Player_Connected", playerSession);
        } else {
            RejectionCache.Reason rejectionReason = rejectionCache.getREJECTION_REASON();
            switch (rejectionReason) {
                case RESTARTING: {
                    rejectedProcess(playerSession, "Player_Connection_Failure_Server_Restarting", "A player tried to log into the starnubdata.network while its preparing to restart: ");
                    break;
                }
                case WHITELIST: {
                    rejectedProcess(playerSession, "Player_Connection_Failure_Whitelist", "A Player tried connecting while the starnubdata.network is whitelisted on IP: ");
                    break;
                }
                case TEMPORARY_BANNED: {
                    rejectedProcess(playerSession, "Player_Connection_Failure_Banned_Temporary", "A temporary banned Player tried connecting to the starnubdata.network on IP: ");
                    break;
                }
                case BANNED: {
                    rejectedProcess(playerSession, "Player_Connection_Failure_Banned_Permanent", "A permanently banned Player tried connecting to the starnubdata.network on IP:  ");
                    break;
                }
                case ALREADY_LOGGED_IN: {
                    rejectedProcess(playerSession, "Player_Connection_Failure_Character_Already_Online", "A Player tried to log in to the starnubdata.network with the same character multiple times. Character: " + characterName + ". on IP:");
                    break;
                }
                case SERVER_FULL: {
                    rejectedProcess(playerSession, "Player_Connection_Failure_Character_Server_Full", "A Player tried to log in to the starnubdata.network while it was full and is not a reserved player. Character: " + characterName + ". on IP:");
                    break;
                }
                case SERVER_FULL_NO_VIP: {
                    rejectedProcess(playerSession, "Player_Connection_Failure_Character_Server_Full_No_Vip", "A Player tried to log in to the starnubdata.network while it was full and is a reserved player, but no reserved slots are free. Character: " + characterName + ". on IP:");
                    break;
                }
                case SERVER_FULL_NO_VIP_KICK: {
                    rejectedProcess(playerSession, "Player_Connection_Failure_Character_Server_Full_No_Vip_Kick", "A Player tried to log in to the starnubdata.network while it was full and is a reserved kick player, but no reserved slots or reserved kick slots are free. Character: " + characterName + ". on IP:");
                    break;
                }
            }
        }
    }

    /**
     * Recommended: For connections use with StarNub
     * <p>
     * Uses: This will handle post processing of this connection
     *
     * @param playerSession Player representing the player being post processed
     * @param eventKey String representing the events key for the events
     * @param consoleMessage String representing the console message to print
     */
    private void rejectedProcess(PlayerSession playerSession, String eventKey, String consoleMessage) {
        new StarNubEvent(eventKey, playerSession);
        StarNub.getLogger().cWarnPrint("StarNub", consoleMessage + playerSession.getSessionIpString());
        playerSession.disconnect();
    }
}






