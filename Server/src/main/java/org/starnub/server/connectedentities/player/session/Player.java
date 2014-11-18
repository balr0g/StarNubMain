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

package org.starnub.server.connectedentities.player.session;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.starnub.server.StarNub;
import org.starnub.server.connectedentities.Sender;
import org.starnub.server.connectedentities.player.character.Character;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * StarNub's Player class that represents a sender. This class
 * purpose of this class is to represent a Players Session.
 * <p>
 * The data in this class will always change between log-ins. The
 * Account and Character classes will save the permanent data to
 * a database.
 * <p>
 * All data is based on a "Session" with StarNub.
 *
 * @author Daniel (Underbalanced) (www.StarNub.org)
 * @since 1.0
 */
@DatabaseTable(tableName = "PLAYER_SESSION_LOG")
public class Player extends Sender {

    /**
     * Represents this sessions unique sessionId that was generated by the database tools
     */
    @Getter
    @DatabaseField(generatedId = true, columnName = "SESSION_ID")
    private volatile int sessionID;

    /**
     * Represents this Players IP ina string mainly used for the database
     */
    @Getter
    @DatabaseField(dataType = DataType.STRING, columnName = "IP")
    private volatile String sessionIpString;

    /**
     * Represents this Players IP as a InetAddress
     */
    @Getter
    private volatile InetAddress sessionIp;

    /**
     * Represents the start time in UTC from when the Player connection was completely excepted
     */
    @Getter
    @DatabaseField(dataType = DataType.DATE_TIME, columnName = "START_TIME")
    private volatile DateTime startTimeUtc;

    /**
     * Represents the start time in UTC from when the Player connection was completely excepted
     */
    @Getter
    @DatabaseField(dataType = DataType.DATE_TIME, columnName = "END_TIME")
    private volatile DateTime endTimeUtc;

    /**
     * Represents the Players StarNub ID if they have registered the Character
     */
    @Getter
    @DatabaseField(dataType = DataType.INTEGER, canBeNull = true, columnName = "STARNUB_ID")
    private volatile int account;

    /**
     * Represents the Character for this Player
     */
    @Getter
    @DatabaseField(foreign = true, canBeNull = false, columnName = "CHARACTER_ID")
    private volatile Character character;

    /**
     * Represents the Character for this Player
     */
    @Getter
    private volatile Restrictions restrictions;

    /**
     * Represents the Channel StarNub from the Players Client side of the connection
     */
    @Getter
    private volatile Channel clientChannel;

    /**
     * Represents the ChannelHandlerContext from the Players Client side of the connection
     */
    @Getter
    private volatile ChannelHandlerContext clientCtx;

    /**
     * Represents the ChannelHandlerContext from the Players Server side of the connection
     */
    @Getter
    private volatile ChannelHandlerContext serverCtx;

    /**
     * Represents the Starbound Client ID assigned by the Starbound server
     */
    @Getter
    private volatile long starboundClientId;

    /**
     * Represents the characters full name with color tags and all
     */
    @Getter
    private volatile String gameName;

    /**
     * Represents the Nick name of the Player, with all of the color strings and other stuff in it
     */
    @Getter
    private volatile String nickName;

    /**
     * Represents the Clean version of the Nick name where we stripped everything out that inter fears with server logs and administration
     */
    @Getter
    private volatile String cleanNickName;

    /**
     * Represents the last time a message was sent by the player in UTC
     */
    @Getter
    private volatile long lastMessageTime;

    /**
     * Represents the last message that was sent by the player
     */
    @Getter
    private volatile String lastMessage;

    /**
     * Represents the last time a command was sent
     */
    @Getter
    private volatile long lastCommandTime;

    /**
     * This indicates weather this account is an operator, which gives them complete control
     * over the server.
     * <p>
     * NOTE: Use with caution. This is really not the right way to give a person admin or mod access. You
     * should use a group with the correct permissions.
     */
    @Getter
    private volatile boolean isOp;

    /**
     * Represents if the Player is Away From Keyboard (AFK), this is manually set by a Player or Plugin
     */
    @Getter
    private volatile boolean afk;

    /**
     * Represents the ChannelHandlerContext of who not to send a message to
     */
    @Getter
    private volatile ArrayList<Channel> doNotSendMessageList;

    /**
     * Represents the latency of a players connection in milliseconds
     */
    @Getter
    private volatile int latency;

    /**
     * Constructor for database purposes
     */
    public Player(){}

    /**
     * This constructor is used to set the player session data up, otherwise the data cannot be set
     *
     * @param senderCTX ChannelHandlerContext representing the client side of the connection (Socket)
     * @param destinationCTX ChannelHandlerContext representing the server side of the connection (Socket)
     * @param connectingIp InetAddress IP address of the client
     * @param character PlayerCharacter the character that is being used in this player session
     * @param restrictions PlayerRestrictions if any that mark this player with (Banned, Muted, Command Blocked...See the class for details)
     * @param account Integer representing a StarNub account id
     * @param isOp boolean representing if the player is OP or not
     */
    public Player(ChannelHandlerContext senderCTX, ChannelHandlerContext destinationCTX, InetAddress connectingIp, Character character, Restrictions restrictions, int account, boolean isOp) {
        super(senderCTX);
        this.startTimeUtc = DateTime.now();
        this.clientChannel = senderCTX.channel();
        this.sessionIp = connectingIp;
        this.sessionIpString = StringUtils.remove(sessionIp.toString(), "/");
        this.clientCtx = senderCTX;
        this.serverCtx = destinationCTX;
        this.character = character;
        this.restrictions = restrictions;
        this.account = account;
        this.gameName = character.getName();
        this.nickName = character.getName();
        this.cleanNickName = character.getCleanName();
        this.lastMessageTime = 0L;
        this.lastMessage= "";
        this.lastCommandTime = 0L;
        this.isOp = isOp;
    }

    /**
     *
     * @param endTimeUtc long allows the setting of this session when it ends
     */
    public void setEndTimeUtc(DateTime endTimeUtc) {
        this.endTimeUtc = endTimeUtc;
        StarNub.getDatabaseTables().getPlayerSessionLog().update(this);
    }

    /**
     *
     * @param account Account which this session belongs too
     */
    public void setAccount(int account) {
        this.account = account;
        StarNub.getDatabaseTables().getPlayerSessionLog().update(this);
    }

    /**
     *
     * @param restrictions sets the Restrictions for this session with (Banned, Muted, Command Blocked...See the class for details)
     */
    public void setRestrictions(Restrictions restrictions) {
        this.restrictions = restrictions;
    }

    /**
     *
     * WARNING: Do not use this method, not for public consumption.
     *
     * @param starboundClientId int that set the Starbound Client ID
     */
    public void setStarboundClientId(long starboundClientId) {
        this.starboundClientId = starboundClientId;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     *
     * @param nickName String set the nick name that includes colors and other characters
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     *
     * @param cleanNickName String to set the clean version of the nick name, colors and special characters removed
     */
    public void setCleanNickName(String cleanNickName) {
        this.cleanNickName = cleanNickName;
    }

    /**
     *
     * @param lastMessage String that you want to set as the last message
     */
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    /**
     *
     * @param lastMessageTime long sets the time that we received a chat message from the player session
     */
    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public void setLastCommandTime(long lastCommandTime) {
        this.lastCommandTime = lastCommandTime;
    }

    public void setOp(boolean isOp) {
        this.isOp = isOp;
    }

    /**
     *
     * @param afk boolean to set if this session/character is ask or not
     */
    public void setAfk(boolean afk) {
        this.afk = afk;
    }

    public void setDoNotSendMessageList() {
        this.doNotSendMessageList = new ArrayList<Channel>();
    }

    public void reloadIgnoreList(){

    }

    public void addToIgnoreList(){

    }

    public void removeFromIgnoreList(){

    }
}




