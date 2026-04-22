package me.theabab2333.headtap.block.entity;

public interface IOutputEntity {
    AbstractBaseMachineBlockEntity getOutputEnabled();

    default boolean isOutputEnabled() {
        return this.getOutputEnabled().isOutputEnabled();
    }

    default void setOutputEnabled(boolean enabled) {
        this.getOutputEnabled().setOutputEnabled(enabled);
    }
}
