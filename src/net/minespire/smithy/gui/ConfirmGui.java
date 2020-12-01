package net.minespire.smithy.gui;

import net.minespire.smithy.upgrade.Purchasable;

public class ConfirmGui extends Gui {

    private Purchasable upgrade;

    public ConfirmGui(int slots, String name, Purchasable upgrade) {
        super(slots, name);
        this.upgrade = upgrade;
    }

    public Purchasable getUpgrade(){
        return upgrade;
    }
}
