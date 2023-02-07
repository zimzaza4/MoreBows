package me.zimzaza4.morebows.item.bows;

import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.ParticleEffect;
import cn.nukkit.potion.Effect;
import me.zimzaza4.morebows.item.CustomBowBase;
import me.zimzaza4.morebows.item.arrows.ShulkerArrow;

public class ShulkerBow extends CustomBowBase {
    public ShulkerBow() {
        super("morebows:shulker_bow", null, "shulker_bow");
    }

    @Override
    public void onHit(ProjectileHitEvent event) {
        if (event.getMovingObjectPosition().entityHit instanceof EntityLiving living) {
            Effect effect = Effect.getEffect(Effect.LEVITATION);
            effect.setDuration(200);
            effect.setAmplifier(0);
            living.addEffect(effect);
        }
    }
    @Override
    protected void onTick(EntityProjectile projectile) {
        projectile.level.addParticleEffect(projectile, ParticleEffect.ENDROD);
    }

    @Override
    public Item getArrowType() {
        return new ShulkerArrow();
    }
}
