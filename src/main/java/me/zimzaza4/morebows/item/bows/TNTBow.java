package me.zimzaza4.morebows.item.bows;

import cn.nukkit.Server;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.particle.FlameParticle;
import cn.nukkit.scheduler.Task;
import me.zimzaza4.morebows.item.CustomBowBase;
import me.zimzaza4.morebows.item.arrows.TNTArrow;

public class TNTBow extends CustomBowBase {
    public TNTBow() {
        super("morebows:tnt_bow", null, "tnt_bow");
    }

    @Override
    public void onHit(ProjectileHitEvent event) {
        new Explosion(event.getEntity(), 2.0f, event.getEntity()).explodeB();
        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
            @Override
            public void onRun(int i) {
                event.getEntity().kill();
            }
        }, 1);
    }

    @Override
    protected void onTick(EntityProjectile projectile) {
        projectile.level.addParticle(new FlameParticle(projectile));
    }

    @Override
    public Item getArrowType() {
        return new TNTArrow();
    }
}
