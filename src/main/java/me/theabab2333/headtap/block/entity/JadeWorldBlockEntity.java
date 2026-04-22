package me.theabab2333.headtap.block.entity;

import com.mojang.logging.LogUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.LevelStem;
import org.slf4j.Logger;

public class JadeWorldBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    @Setter
    @Getter
    private ResourceKey<LevelStem> dimension;
    @Setter
    @Getter
    private boolean revisit;
    @Setter
    @Getter
    private BlockPos blockPos;

    public JadeWorldBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.dimension = LevelStem.OVERWORLD;
        this.revisit = false;
        this.blockPos = pos;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("dimension")) {
            Tag dimTag = tag.get("dimension");
            ResourceKey.codec(Registries.LEVEL_STEM)
                .parse(NbtOps.INSTANCE, dimTag)
                .resultOrPartial(LOGGER::error)
                .ifPresent(pair -> this.dimension = pair);
        }
        this.revisit = tag.getBoolean("revisit");
        int x = tag.getInt("blockPosX");
        int y = tag.getInt("blockPosY");
        int z = tag.getInt("blockPosZ");
        this.blockPos = new BlockPos(x, y, z);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ResourceKey<LevelStem> value = this.dimension;
        ResourceKey.codec(Registries.LEVEL_STEM)
            .encodeStart(NbtOps.INSTANCE, value)
            .resultOrPartial(LOGGER::error)
            .ifPresent(result -> tag.put("dimension", result));

        tag.putBoolean("revisit", this.revisit);
        tag.putInt("blockPosX", this.blockPos.getX());
        tag.putInt("blockPosY", this.blockPos.getY());
        tag.putInt("blockPosZ", this.blockPos.getZ());
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }

    public ResourceKey<Level> getTargetDimension() {
        return Registries.levelStemToLevel(this.dimension);
    }
}
