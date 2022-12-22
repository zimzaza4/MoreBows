package me.zimzaza4.morebows.item.bows;

import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.level.particle.DustParticle;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.BlockColor;
import me.zimzaza4.morebows.item.CustomBowBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IcyBow extends CustomBowBase {
    public IcyBow() {
        super("morebows:icy_bow", null, "icy_bow");
    }

    @Override
    public void onHit(ProjectileHitEvent event) {
        if (event.getMovingObjectPosition().entityHit instanceof EntityLiving living) {
            Effect effect = Effect.getEffect(Effect.SLOWNESS);
            effect.setDuration(100);
            effect.setAmplifier(4);
            living.addEffect(effect);

        }
    }

    @Override
    protected void onTick(EntityProjectile projectile) {
        projectile.level.addParticle(new DestroyBlockParticle(projectile, Block.get(Block.ICE)));
    }
}
