package me.theabab2333.headtap.inventory;

import me.theabab2333.headtap.block.entity.AbstractBaseMachineBlockEntity;

public interface IOutputMenu {
    AbstractBaseMachineBlockEntity getBlockEntity();

    default boolean isOutputEnable() {
        return this.getBlockEntity().isOutputEnabled();
    }

    default void setOutputEnable(boolean enable) {
        this.getBlockEntity().setOutputEnabled(enable);
    }

    default void flush() {}
}
