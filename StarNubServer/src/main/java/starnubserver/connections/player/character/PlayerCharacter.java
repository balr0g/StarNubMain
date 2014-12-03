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

package starnubserver.connections.player.character;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.joda.time.DateTime;
import starnubserver.StarNub;
import starnubserver.connections.player.account.Account;
import starnubserver.connections.player.session.Ban;
import utilities.strings.StringUtilities;

import java.util.UUID;


/**
 * StarNub's PlayerCharacter represents a character that belongs
 * to a player. We named it PlayerCharacter due to issues with
 * com.java having a Character class. We did not want to confuse
 * Plugin developers or API users.
 * <p>
 * The data is stored permanently in the database table "characters".
 * <p>
 * All data is established and saved once, but loaded as needed.
 *
 * @author Daniel (Underbalanced) (www.StarNub.org)
 * @since 1.0
 */
@DatabaseTable(tableName = "CHARACTERS")
public class PlayerCharacter {

    /**
     * Represents a unique characterId. It is unique based on Character Name and uuid
     */

    @DatabaseField(generatedId = true, columnName = "CHARACTER_ID")
    private volatile int characterid;

    /**
     * Represents the characters full name with color tags and all
     */

    @DatabaseField(dataType = DataType.STRING, uniqueCombo=true, columnName = "NAME")
    private volatile String name;

    /**
     * Represents the cleaned up version of the characters name without color tags
     */

    @DatabaseField(dataType = DataType.STRING, columnName = "CLEAN_NAME")
    private volatile String cleanName;

    /**
     * Represents the characters uuid generated by the Starbound Client
     */

    @DatabaseField(dataType = DataType.UUID, uniqueCombo=true, columnName = "uuid")
    private volatile UUID uuid;

    /**
     * Represents the characters Account that this character is attached to
     */

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 9, columnName = "RESTRICTIONS_ID")
    private volatile Ban ban;

    /**
     * Represents the last time this character was used
     */

    @DatabaseField(dataType = DataType.DATE_TIME, columnName = "LAST_SEEN")
    private volatile DateTime lastSeen;

    /**
     * Represents the total play time of this character
     */

    @DatabaseField(dataType = DataType.LONG, columnName = "PLAYED_TIME")
    private volatile long playedTime;

    /**
     * Represents the characters Account that this character is attached to
     */

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 9, columnName = "STARNUB_ID")
    private volatile Account account;

    /**
     * Represents the ips that have been used by this character
     */

    @ForeignCollectionField(eager = true)
    private volatile ForeignCollection<CharacterIP> associatedIps;


    /**
     * Constructor for database purposes
     */
    public PlayerCharacter(){}

    public int getCharacterid() {
        return characterid;
    }

    public String getName() {
        return name;
    }

    public String getCleanName() {
        return cleanName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public DateTime getLastSeen() {
        return lastSeen;
    }

    public long getPlayedTime() {
        return playedTime;
    }

    public Account getAccount() {
        return account;
    }

    public ForeignCollection<CharacterIP> getAssociatedIps() {
        return associatedIps;
    }

    public Ban getBan() {
        return ban;
    }

    /**
     * Required to build a character class
     * @param name String name of the character
     * @param uuid uuid of the character
     */
    public PlayerCharacter(String name, UUID uuid) {
        this.name = name;
        this.cleanName = StringUtilities.completeClean(name);
        this.uuid = uuid;
        this.lastSeen = DateTime.now();
        this.ban = null;
    }

    public void setLastSeen(DateTime lastSeen) {
        this.lastSeen = lastSeen;
        StarNub.getDatabaseTables().getCharacters().update(this);
    }

    public void updatePlayedTimeLastSeen() {
        this.playedTime = this.getPlayedTime()+(DateTime.now().getMillis()-lastSeen.getMillis());
        this.lastSeen = DateTime.now();
        StarNub.getDatabaseTables().getCharacters().update(this);
    }

    public void setPlayedTime(long playedTime) {
        this.playedTime = playedTime;
        StarNub.getDatabaseTables().getCharacters().update(this);
    }

    /**
     *
     * @param account Account which this character will belong too
     */
    public void setAccount(Account account) {
        this.account = account;
        StarNub.getDatabaseTables().getCharacters().update(this);
    }

    public void setAssociatedIps(ForeignCollection<CharacterIP> associatedIps) {
        this.associatedIps = associatedIps;
//        StarNub.getDatabase().updateCharacter(this);
    }

    @SuppressWarnings("unchecked")
    public int initialLogInProcessing(String ip, String uuid){
        int accountId = 0;
        if (this.account != null) {
            accountId = account.getStarnubId();
            this.account.setLastLogin(DateTime.now());
            this.account.loadPermissions();
            StarNub.getDatabaseTables().getAccounts().update(this.account);
        }
        StarNub.getDatabaseTables().getCharacters().createOrUpdate(this);
        this.ban = StarNub.getConnections().getBANS().banGet(ip, uuid);
        return accountId;
    }

    public void removeBan(){
        this.ban.removeBan();
        this.ban = null;
        StarNub.getDatabaseTables().getCharacters().update(this);
    }

    public void addBan(String restrictedIdentifier, String imposerName, String reason, Account imposerAccount, DateTime dateRestrictionExpires){
        Ban ban = new Ban();
        ban.setBan(restrictedIdentifier, imposerName, reason, imposerAccount, dateRestrictionExpires);
        this.ban = ban;
        this.ban.addBan();
        StarNub.getDatabaseTables().getCharacters().update(this);
    }


}
