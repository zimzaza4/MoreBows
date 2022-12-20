package me.zimzaza4.morebows.item.bows;

import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.level.Sound;
import me.zimzaza4.morebows.item.CustomBowBase;

public class TeleportBow extends CustomBowBase {
    public TeleportBow() {
        super("morebows:teleport_bow", "传送弓", "teleport_bow");
    }


    @Override
    public void onHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof EntityArrow arrow) {
            if (arrow.shootingEntity != null) {
                arrow.shootingEntity.teleport(event.getMovingObjectPosition().hitVector.add(0, 1, 0));

                arrow.shootingEntity.attack(new EntityDamageByEntityEvent(arrow, arrow.shootingEntity, EntityDamageEvent.DamageCause.PROJECTILE, 5));
                arrow.level.addSound(arrow, Sound.MOB_ENDERMEN_PORTAL);
            }
        }
    }
}
