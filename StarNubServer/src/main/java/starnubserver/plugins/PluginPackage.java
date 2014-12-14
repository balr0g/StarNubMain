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

package starnubserver.plugins;

import starnubserver.plugins.generic.CommandInfo;
import starnubserver.plugins.generic.PluginDetails;
import starnubserver.plugins.resources.PluginRunnables;
import starnubserver.plugins.resources.YAMLFiles;
import starnubserver.resources.files.PluginConfiguration;

/**
 * Represents the StarNubs PluginPackage. This information
 * is used internally when the plugin is loaded, we build this
 * from the plugin.yml. This helps Server owners as well as plugin
 * makers to get information about plugins, get configuration variables,
 * as well as the commands that are associated with the plugin.
 * <p>
 * NOTE: These fields are public for the sake of information dumping via YAML.
 * <p>
 * This class is abstract and how a generic PluginPackage should look.
 * <p>
 *
 * @author Daniel (Underbalanced) (www.StarNub.org)
 * @since 1.0
 */
public abstract class PluginPackage {

    private final String NAME;
    private final String PATH;
    private final PluginDetails PLUGIN_DETAILS;
    private final PluginConfiguration CONFIGURATION;
    private final YAMLFiles FILES;
    private final CommandInfo COMMAND_INFO;
    private final PluginRunnables PLUGIN_RUNNABLES;

    private boolean enabled;

    /**
     * This is for plugins to not have to construct anything but allows for StarNub to construct this
     */
    public PluginPackage() {
        this.NAME = null;
        this.PATH = null;
        this.PLUGIN_DETAILS = null;
        this.CONFIGURATION = null;
        this.FILES = null;
        this.COMMAND_INFO = null;
        this.PLUGIN_RUNNABLES = null;
    }

    /**
     * Used in building a plugin
     *
     * @param NAME String name of the plugin
     * @param PATH String file path of the plugin
     * @param PLUGIN_DETAILS PluginDetails containing plugin information
     * @param CONFIGURATION PluginConfiguration contains the plugin configuration from disk
     * @param FILES YAMLFiles containing the files used in this plugin
     * @param COMMAND_INFO CommandInfo information on the command and the command packages
     * @param PLUGIN_RUNNABLES PluginRunnables containing the plugin runnables
     */
    public PluginPackage(String NAME, String PATH, PluginDetails PLUGIN_DETAILS, PluginConfiguration CONFIGURATION, YAMLFiles FILES, CommandInfo COMMAND_INFO, PluginRunnables PLUGIN_RUNNABLES) {
        this.NAME = NAME;
        this.PATH = PATH;
        this.PLUGIN_DETAILS = PLUGIN_DETAILS;
        this.CONFIGURATION = CONFIGURATION;
        this.FILES = FILES;
        this.COMMAND_INFO = COMMAND_INFO;
        this.PLUGIN_RUNNABLES = PLUGIN_RUNNABLES;
    }

    public String getNAME() {
        return NAME;
    }

    public String getPATH() {
        return PATH;
    }

    public PluginDetails getPLUGIN_DETAILS() {
        return PLUGIN_DETAILS;
    }

    public PluginConfiguration getCONFIGURATION() {
        return CONFIGURATION;
    }

    public YAMLFiles getFILES() {
        return FILES;
    }

    public CommandInfo getCOMMAND_INFO() {
        return COMMAND_INFO;
    }

    public PluginRunnables getPLUGIN_RUNNABLES() {
        return PLUGIN_RUNNABLES;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
