package me.zimzaza4.morebows.item;

import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CustomArrowBase extends ItemCustom {


    public CustomArrowBase(@NotNull String id, String texture) {
        super(id, null, texture);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition.simpleBuilder(this, ItemCreativeCategory.EQUIPMENT)
                .allowOffHand(true)
                .build();
    }
}
