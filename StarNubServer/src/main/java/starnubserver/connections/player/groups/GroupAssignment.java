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

package starnubserver.connections.player.groups;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import starnubserver.connections.player.account.Account;

@DatabaseTable(tableName = "GROUP_ASSIGNMENTS")
public class GroupAssignment {

    /**
     * Represents a group assignment id
     */

    @DatabaseField(generatedId =true, dataType = DataType.INTEGER, columnName = "GROUP_ASSIGNMENT_ID")
    private volatile int groupAssignmentId;

    /**
     * Represents the starnubId which has an associated group
     */

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "STARNUB_ID")
    private volatile Account starnubId;

    /**
     * Represents this Players IP ina string mainly used for the database
     */

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "GROUP")
    private volatile Group group;

    /**
     * Constructor for database purposes
     */
    public GroupAssignment(){}

    public int getGroupAssignmentId() {
        return groupAssignmentId;
    }

    public Account getStarnubId() {
        return starnubId;
    }

    public Group getGroup() {
        return group;
    }

    /**
     * Constructor used in adding, removing or updating a group assignment for a account
     * @param starnubId int representing the account id
     * @param group int representing the group id
     */
    public GroupAssignment(Account starnubId, Group group) {
        this.starnubId = starnubId;
        this.group = group;
    }
}