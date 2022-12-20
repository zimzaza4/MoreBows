
package me.zimzaza4.morebows.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.api.PowerNukkitDifference;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustomTool;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.scheduler.Task;
import me.zimzaza4.morebows.item.data.ArrowMetadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.stream.Stream;

public class CustomBowBase extends ItemCustomTool {


    public CustomBowBase(@NotNull String id, @Nullable String name, @NotNull String textureName) {
        super(id, name, textureName);
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition
                .toolBuilder(this, ItemCreativeCategory.EQUIPMENT)
                .allowOffHand(false)
                .handEquipped(true)
                .foil(false)
                .customBuild(nbt -> {
                    nbt.getCompound("components")
                            .putCompound("minecraft:food", new CompoundTag().putBoolean("can_always_eat", true))
                            .getCompound("item_properties")
                            .putInt("use_duration", Integer.MAX_VALUE)
                            .putCompound("minecraft:chargeable", new CompoundTag().putFloat("movement_modifier", 0.35F));

                });
    }

    @Override
    public int getMaxDurability() {
        return ItemTool.DURABILITY_BOW;
    }

    @Override
    public int getEnchantAbility() {
        return 1;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 directionVector) {
        return player.isCreative() ||
                Stream.of(player.getInventory(), player.getOffhandInventory())
                        .anyMatch(inv -> inv.contains(getArrowType()));
    }

    @PowerNukkitDifference(info = "Using new method to play sounds", since = "1.4.0.0-PN")
    @Override
    public boolean onRelease(Player player, int ticksUsed) {
        return findAndShoot(player, ticksUsed);
    }

    protected boolean findAndShoot(Player player, int ticksUsed) {
        Item itemArrow = Item.get(Item.ARROW, 0, 1);

        Inventory inventory = player.getOffhandInventory();

        if (!inventory.contains(itemArrow) && !(inventory = player.getInventory()).contains(itemArrow) && (player.isAdventure() || player.isSurvival())) {
            player.getOffhandInventory().sendContents(player);
            inventory.sendContents(player);
            return false;
        }

        double damage = getBowDamage();

        Enchantment bowDamage = this.getEnchantment(Enchantment.ID_BOW_POWER);
        if (bowDamage != null && bowDamage.getLevel() > 0) {
            damage += (double) bowDamage.getLevel() * 0.5 + 0.5;
        }

        Enchantment flameEnchant = this.getEnchantment(Enchantment.ID_BOW_FLAME);
        boolean flame = flameEnchant != null && flameEnchant.getLevel() > 0;

        CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", player.x))
                        .add(new DoubleTag("", player.y + player.getEyeHeight()))
                        .add(new DoubleTag("", player.z)))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", -Math.sin(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)))
                        .add(new DoubleTag("", -Math.sin(player.pitch / 180 * Math.PI)))
                        .add(new DoubleTag("", Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI))))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", (player.yaw > 180 ? 360 : 0) - (float) player.yaw))
                        .add(new FloatTag("", (float) -player.pitch)))
                .putShort("Fire", flame ? 45 * 60 : 0)
                .putDouble("damage", damage);

        double p = (double) ticksUsed / 20;
        double f = Math.min((p * p + p * 2) / 3, 1) * 3;

        EntityArrow arrow = (EntityArrow) Entity.createEntity("Arrow", player.chunk, nbt, player, f == 2);

        if (arrow == null) {
            return false;
        }

        EntityShootBowEvent entityShootBowEvent = new EntityShootBowEvent(player, this, arrow, f);

        if (f < 0.1 || ticksUsed < 3) {
            entityShootBowEvent.setCancelled();
        }

        Server.getInstance().getPluginManager().callEvent(entityShootBowEvent);
        if (entityShootBowEvent.isCancelled()) {
            entityShootBowEvent.getProjectile().kill();
            player.getInventory().sendContents(player);
            player.getOffhandInventory().sendContents(player);
        } else {
            entityShootBowEvent.getProjectile().setMotion(entityShootBowEvent.getProjectile().getMotion().multiply(entityShootBowEvent.getForce()));
            Enchantment infinityEnchant = this.getEnchantment(Enchantment.ID_BOW_INFINITY);
            boolean infinity = infinityEnchant != null && infinityEnchant.getLevel() > 0;
            EntityProjectile projectile;
            if (infinity && (projectile = entityShootBowEvent.getProjectile()) instanceof EntityArrow) {
                ((EntityArrow) projectile).setPickupMode(EntityProjectile.PICKUP_CREATIVE);
            }
            if (player.isAdventure() || player.isSurvival()) {
                if (!infinity) {
                    inventory.removeItem(itemArrow);
                }
                if (!this.isUnbreakable()) {
                    Enchantment durability = this.getEnchantment(Enchantment.ID_DURABILITY);
                    if (!(durability != null && durability.getLevel() > 0 && (100 / (durability.getLevel() + 1)) <= new Random().nextInt(100))) {
                        this.setDamage(this.getDamage() + 1);
                        if (this.getDamage() >= getMaxDurability()) {
                            player.getLevel().addSound(player, Sound.RANDOM_BREAK);
                            this.count--;
                        }
                        player.getInventory().setItemInHand(this);
                    }
                }
            }
            if (entityShootBowEvent.getProjectile() != null) {
                ProjectileLaunchEvent projectev = new ProjectileLaunchEvent(entityShootBowEvent.getProjectile(), player);
                Server.getInstance().getPluginManager().callEvent(projectev);
                if (projectev.isCancelled()) {
                    entityShootBowEvent.getProjectile().kill();
                } else {
                    entityShootBowEvent.getProjectile().spawnToAll();
                    entityShootBowEvent.getProjectile().setMetadata("morebows:custom_arrow_data", new ArrowMetadata(this));
                    Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {
                        @Override
                        public void onRun(int i) {
                            if (entityShootBowEvent.getProjectile().isAlive()) {
                                onTick(entityShootBowEvent.getProjectile());
                            } else {
                                this.cancel();
                            }
                        }
                    }, 1);
                    onShoot(entityShootBowEvent.getProjectile());
                    player.getLevel().addSound(player, Sound.RANDOM_BOW);
                }
            }
        }
        return true;
    }

    public Item getArrowType() {
        return Item.get(Item.ARROW);
    }

    protected void onTick(EntityProjectile projectile) {
    }

    protected int getBowDamage() {
        return 2;
    }
    protected void onShoot(EntityProjectile projectile) {

    }

    public void onHit(ProjectileHitEvent event) {

    }


}
