package me.zimzaza4.morebows.item.bows;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFire;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Location;
import cn.nukkit.level.particle.LavaDripParticle;
import cn.nukkit.scheduler.Task;
import me.zimzaza4.morebows.item.CustomBowBase;
import me.zimzaza4.morebows.item.arrows.BlazeArrow;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class BlazeBow extends CustomBowBase {
    public BlazeBow() {
        super("morebows:blaze_bow", null, "blaze_bow");
    }

    @Override
    public void onHit(ProjectileHitEvent event) {

        Entity entity = event.getEntity();
        Entity entityHit = event.getMovingObjectPosition().entityHit;
        if (entityHit != null) {
            entityHit.setOnFire(8);
        }
        if (entity.level.gameRules.getBoolean(GameRule.DO_FIRE_TICK)) {
            for (Block block : selectionRadiusBlock(event.getEntity(), 1, 2)) {

                if (block.getId() != Block.AIR) {
                    continue;
                }

                BlockFire fire = (BlockFire) Block.get(Block.FIRE);

                fire.x = block.x;
                fire.y = block.y;
                fire.z = block.z;
                fire.level = block.level;
                block.level.setBlock(fire, fire, true);

                if (fire.isBlockTopFacingSurfaceSolid(fire.down()) || fire.canNeighborBurn()) {
                    BlockIgniteEvent e = new BlockIgniteEvent(block, null, entity, BlockIgniteEvent.BlockIgniteCause.FLINT_AND_STEEL);
                    entity.getServer().getPluginManager().callEvent(e);
                    if (!e.isCancelled()) {
                        entity.level.setBlock(fire, fire, true);
                        entity.level.scheduleUpdate(fire, fire.tickRate() + ThreadLocalRandom.current().nextInt(10));
                    }
                }
            }
        }

        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
            @Override
            public void onRun(int i) {
                event.getEntity().kill();
            }
        }, 1);
    }

    @Override
    protected void onTick(EntityProjectile projectile) {
        projectile.level.addParticle(new LavaDripParticle(projectile));
    }

    @Override
    public Item getArrowType() {
        return new BlazeArrow();
    }

    public static Set<Block> selectionRadiusBlock(Location location, int horizontalRadius, int verticalHeight) {
        Set<Block> blocks = new HashSet<>();
        for(int x = location.getFloorX() - horizontalRadius; x <= location.getFloorX() + horizontalRadius; x++) {
            for(int y = location.getFloorY() - verticalHeight; y <= location.getFloorY() + verticalHeight; y++) {
                for(int z = location.getFloorZ() - horizontalRadius; z <= location.getFloorZ() + horizontalRadius; z++) {
                    Block block = location.level.getBlock(x, y, z);
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

}
