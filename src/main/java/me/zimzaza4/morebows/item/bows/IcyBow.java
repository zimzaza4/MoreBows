package me.zimzaza4.morebows.item.bows;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.potion.Effect;
import me.zimzaza4.morebows.item.CustomBowBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IcyBow extends CustomBowBase {
    public IcyBow() {
        super("morebows:icy_bow", "§b冰弓", "icy_bow");
    }

    @Override
    public void onHit(ProjectileHitEvent event) {
        if (event.getMovingObjectPosition().entityHit instanceof EntityLiving living) {
            Effect effect = Effect.getEffect(Effect.SLOWNESS);
            effect.setDuration(100);
            effect.setAmplifier(3);
            living.addEffect(effect);

        }
    }
}