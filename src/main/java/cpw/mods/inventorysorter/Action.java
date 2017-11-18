/*
 *     Copyright © 2016 cpw
 *     This file is part of Inventorysorter.
 *
 *     Inventorysorter is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Inventorysorter is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Inventorysorter.  If not, see <http://www.gnu.org/licenses/>.
 */

package cpw.mods.inventorysorter;

import net.minecraft.inventory.*;
import net.minecraftforge.common.config.*;

import java.util.function.*;

/**
 * Created by cpw on 08/01/16.
 */
public enum Action
{
    SORT(SortingHandler.INSTANCE, "key.inventorysorter.sort", -98, "middleClickSorting", "Middle-click sorting module", true),
    ONEITEMIN(ScrollWheelHandler.ONEITEMIN, "key.inventorysorter.itemin", -200, "mouseWheelMoving", "Mouse wheel movement module", true),
    ONEITEMOUT(ScrollWheelHandler.ONEITEMOUT, "key.inventorysorter.itemout", -201, "mouseWheelMoving", "Mouse wheel movement module", true);

    private final Consumer<ContainerContext> worker;
    private final String keyBindingName;
    private final int defaultKeyCode;
    private final String configName;
    private boolean actionActive;
    private Property property;
    private final String comment;
    private final boolean implemented;

    Action(Consumer<ContainerContext> worker, String keyBindingName, int defaultKeyCode, String configName, String comment, boolean implemented)
    {
        this.worker = worker;
        this.keyBindingName = keyBindingName;
        this.defaultKeyCode = defaultKeyCode;
        this.configName = configName;
        this.comment = comment;
        this.implemented = implemented;
    }

    public String getKeyBindingName() {
        return keyBindingName;
    }
    public static void configure(Configuration config)
    {
        for (Action a : values())
        {
            a.property = config.get(Configuration.CATEGORY_CLIENT, a.configName, true);
            a.property.setRequiresMcRestart(false);
            a.property.setRequiresWorldRestart(false);
            a.property.setLanguageKey("inventorysorter.gui." + a.configName);
            a.property.setShowInGui(a.implemented);
            a.property.setComment(a.comment);
            a.actionActive = a.property.getBoolean(true);
        }
    }

    public Network.ActionMessage message(Slot slot)
    {
        return new Network.ActionMessage(this, slot.slotNumber);
    }

    public void execute(ContainerContext context)
    {
        this.worker.accept(context);
    }

    public Property getProperty()
    {
        return property;
    }

    public boolean isActive()
    {
        return actionActive;
    }

    public String getConfigName()
    {
        return configName;
    }

    public int getDefaultKeyCode() {
        return defaultKeyCode;
    }

}
