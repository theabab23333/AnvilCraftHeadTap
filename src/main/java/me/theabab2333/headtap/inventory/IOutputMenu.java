package me.theabab2333.headtap.inventory;

import me.theabab2333.headtap.block.entity.IOutputEntity;

public interface IOutputMenu {
    IOutputEntity getBlockEntity();

    default boolean isOutputEnable() {
        return this.getBlockEntity().isOutputEnabled();
    }

    default void setOutputEnable(boolean enable) {
        this.getBlockEntity().setOutputEnabled(enable);
    }

    default void flush() {

    }
}
