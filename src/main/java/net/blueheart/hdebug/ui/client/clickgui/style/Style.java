package net.blueheart.hdebug.ui.client.clickgui.style;

import net.blueheart.hdebug.ui.client.clickgui.Panel;
import net.blueheart.hdebug.ui.client.clickgui.elements.ButtonElement;
import net.blueheart.hdebug.ui.client.clickgui.elements.ModuleElement;
import net.blueheart.hdebug.utils.MinecraftInstance;

public abstract class Style extends MinecraftInstance {

    public abstract void drawPanel(final int mouseX, final int mouseY, final Panel panel);

    public abstract void drawDescription(final int mouseX, final int mouseY, final String text);

    public abstract void drawButtonElement(final int mouseX, final int mouseY, final ButtonElement buttonElement);

    public abstract void drawModuleElement(final int mouseX, final int mouseY, final ModuleElement moduleElement);

}
