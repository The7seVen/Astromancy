package coffee.amo.astromancy.common.blockentity.jar;

import coffee.amo.astromancy.Astromancy;
import coffee.amo.astromancy.aequivaleo.AspectiEntry;
import coffee.amo.astromancy.client.helper.ClientRenderHelper;
import coffee.amo.astromancy.common.block.jar.JarBlock;
import coffee.amo.astromancy.common.item.AspectiPhial;
import coffee.amo.astromancy.core.handlers.AstromancyPacketHandler;
import coffee.amo.astromancy.core.helpers.BlockHelper;
import coffee.amo.astromancy.core.helpers.StringHelper;
import coffee.amo.astromancy.core.packets.ItemSyncPacket;
import coffee.amo.astromancy.core.packets.JarUpdatePacket;
import coffee.amo.astromancy.core.registration.BlockEntityRegistration;
import coffee.amo.astromancy.core.registration.ItemRegistry;
import coffee.amo.astromancy.core.systems.aspecti.Aspecti;
import coffee.amo.astromancy.core.systems.blockentity.AstromancyBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class JarBlockEntity extends AstromancyBlockEntity {
    private int count;
    private Aspecti aspecti;
    public int clientLookAtTicks;
    public JarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public JarBlockEntity(BlockPos pos, BlockState state){
        super(BlockEntityRegistration.JAR.get(), pos, state);
        aspecti = Aspecti.EMPTY;
    }

    @Override
    public void tick() {
        super.tick();
        if(level.isClientSide){
            clientLookAtTicks = ClientRenderHelper.tickStuff((ClientLevel) level, this, clientLookAtTicks);
        }
    }

    // TODO: cap this at 256, or maybe make it a config option.
    // TODO: make it so the first stack added starts the count at 16
    // TODO: fix the renderer
    @Override
    public InteractionResult onUse(Player player, InteractionHand hand) {
        if(!level.isClientSide){
            ItemStack heldItem = player.getItemInHand(hand);
            if (heldItem.getItem() instanceof AspectiPhial && heldItem.hasTag()) {
                if (count <= 240) {
                    if (aspecti == Aspecti.fromNbt(heldItem.getTag()).getFirst() || aspecti == Aspecti.EMPTY) {
                        heldItem.shrink(1);
                        player.addItem(new ItemStack(ItemRegistry.ASPECTI_PHIAL.get(),1));
                        BlockHelper.updateAndNotifyState(level, worldPosition);
                        count = count == 0 ? Aspecti.fromNbt(heldItem.getTag()).getSecond() : count + Aspecti.fromNbt(heldItem.getTag()).getSecond();
                        aspecti = Aspecti.fromNbt(heldItem.getTag()).getFirst();
                        AstromancyPacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
                                this.getBlockPos().getX(),
                                this.getBlockPos().getY(),
                                this.getBlockPos().getZ(),
                                128, this.level.dimension())), new JarUpdatePacket(this.getBlockPos(), count, aspecti.ordinal()));
                        return InteractionResult.SUCCESS;
                    }
                    return InteractionResult.PASS;
                }
                return InteractionResult.PASS;
            }  else if (heldItem.getItem() instanceof AspectiPhial && !heldItem.hasTag() && this.aspecti != Aspecti.EMPTY) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("count", 16);
                tag.putInt("aspecti", aspecti.ordinal());
                ItemStack stack = new ItemStack(ItemRegistry.ASPECTI_PHIAL.get(), 1);
                stack.getOrCreateTag().put("aspecti", tag);
                player.addItem(stack);
                heldItem.shrink(1);
                if (count - 16 <= 0) {
                    this.count = 0;
                    this.aspecti = Aspecti.EMPTY;
                } else {
                    this.count = count - 16;
                }
                AstromancyPacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
                        this.getBlockPos().getX(),
                        this.getBlockPos().getY(),
                        this.getBlockPos().getZ(),
                        128, this.level.dimension())), new JarUpdatePacket(this.getBlockPos(), count, aspecti.ordinal()));
                return InteractionResult.SUCCESS;
            } else if (heldItem.getItem() == ItemRegistry.JAR.get() && heldItem.getTag() != null && !player.isCrouching()) {
                if(count <= 256 && Aspecti.values()[heldItem.getTag().getCompound("BlockEntityTag").getInt("aspecti")] == aspecti){
                    CompoundTag tag = heldItem.getTag().getCompound("BlockEntityTag");
                    int jarCount = tag.getInt("count");
                    int cachedJarCount = jarCount;
                    jarCount = jarCount + count > 256 ? (jarCount + count) - 256 : 0;
                    count = Math.min(cachedJarCount + count, 256);
                    if(jarCount == 0){
                        heldItem.getTag().remove("BlockEntityTag");
                    } else {
                        tag.putInt("count", jarCount);
                        heldItem.getTag().put("BlockEntityTag", tag);
                    }
                    AstromancyPacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
                            this.getBlockPos().getX(),
                            this.getBlockPos().getY(),
                            this.getBlockPos().getZ(),
                            128, this.level.dimension())), new JarUpdatePacket(this.getBlockPos(), count, aspecti.ordinal()));
                    AstromancyPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new ItemSyncPacket(heldItem));
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public int getCount() {
        return count;
    }
    public void setAspecti(Aspecti aspecti) {
        this.aspecti = aspecti;
    }
    public Aspecti getAspecti() {
        return aspecti;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("count", count);
        if(this.aspecti != null){
            pTag.putInt("aspecti", aspecti.ordinal());
        }
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        count = pTag.getInt("count");
        if(pTag.contains("aspecti")){
            aspecti = Aspecti.values()[pTag.getInt("aspecti")];
        }
        super.load(pTag);
    }

    @Override
    public void onBreak(@Nullable Player player) {
        super.onBreak(player);
    }

    public TextComponent getAspectiComponent(){
        TextComponent tc = new TextComponent("");
        if(this.aspecti != null){
            tc.append(new TextComponent("[").withStyle(s -> s.withFont(Astromancy.astromancy("aspecti"))));
            tc.append(new TranslatableComponent("space.0").withStyle(s -> s.withFont(Astromancy.astromancy("negative_space"))));
            tc.append(new TranslatableComponent("space.-1").withStyle(s -> s.withFont(Astromancy.astromancy("negative_space"))));
            tc.append(new TextComponent(aspecti.symbol()).withStyle(style -> style.withFont(Astromancy.astromancy("aspecti"))));
            tc.append(new TranslatableComponent("space.0").withStyle(s -> s.withFont(Astromancy.astromancy("negative_space"))));
            tc.append(new TranslatableComponent("space.-1").withStyle(s -> s.withFont(Astromancy.astromancy("negative_space"))));
            tc.append(AspectiEntry.intToTextComponent(count).append(new TextComponent("]").withStyle(s -> s.withFont(Astromancy.astromancy("aspecti")))));
        }
        return tc;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
