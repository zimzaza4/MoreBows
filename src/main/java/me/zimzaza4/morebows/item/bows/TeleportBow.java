package me.zimzaza4.morebows.item.bows;

import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;

import cn.nukkit.level.particle.PortalParticle;
import me.zimzaza4.morebows.item.CustomBowBase;
import me.zimzaza4.morebows.item.arrows.TeleportArrow;

public class TeleportBow extends CustomBowBase {
    public TeleportBow() {
        super("morebows:teleport_bow", null, "teleport_bow");
    }


    @Override
    public void onHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof EntityArrow arrow) {
            if (arrow.shootingEntity != null) {
                arrow.shootingEntity.teleport(event.getMovingObjectPosition().hitVector.add(0, 1, 0));

                arrow.shootingEntity.attack(new EntityDamageByEntityEvent(arrow, arrow.shootingEntity, EntityDamageEvent.DamageCause.PROJECTILE, 5));
                arrow.level.addSound(arrow, Sound.MOB_ENDERMEN_PORTAL);
                arrow.kill();
            }
        }
    }

    @Override
    protected void onTick(EntityProjectile projectile) {
        projectile.level.addParticle(new PortalParticle(projectile));
    }

    @Override
    public Item getArrowType() {
        return new TeleportArrow();
    }
}
