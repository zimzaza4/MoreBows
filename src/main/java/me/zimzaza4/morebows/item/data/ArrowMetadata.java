package me.zimzaza4.morebows.item.data;

import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.plugin.Plugin;
import me.zimzaza4.morebows.MoreBows;
import me.zimzaza4.morebows.item.CustomBowBase;

public class ArrowMetadata extends MetadataValue {

    private final CustomBowBase bow;

    public ArrowMetadata(CustomBowBase bow) {
        super(MoreBows.getInstance());
        this.bow = bow;
    }

    @Override
    public Object value() {
        return this;
    }

    public CustomBowBase getBow() {
        return bow;
    }

    @Override
    public void invalidate() {

    }
}
