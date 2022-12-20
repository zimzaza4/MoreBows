package me.zimzaza4.morebows.item.bows;

import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.level.Explosion;
import me.zimzaza4.morebows.item.CustomBowBase;

public class TNTBow extends CustomBowBase {
    public TNTBow() {
        super("morebows:tnt_bow", "TNT弓", "tnt_bow");
    }

    @Override
    public void onHit(ProjectileHitEvent event) {
        new Explosion(event.getEntity(), 2.0f, event.getEntity()).explodeB();
    }
}
