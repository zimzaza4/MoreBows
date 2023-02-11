package me.zimzaza4.morebows.item;

import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;

public class MenuButton extends ItemCustom {
    public MenuButton() {
        super("morebows:button", "-");
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }


    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition.simpleBuilder(this, ItemCreativeCategory.CONSTRUCTOR).build();
    }
}
