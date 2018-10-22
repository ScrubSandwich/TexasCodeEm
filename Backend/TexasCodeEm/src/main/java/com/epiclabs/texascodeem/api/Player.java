package com.epiclabs.texascodeem.api;

public class Player {

    private final String name;
    private final String id;
    private int stackSize;

    public Player(String name, String id) {
        this.name = name;
        this.id = id;
        this.stackSize = Values.DEFAULT_STACK_SIZE;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getStackSize() {
        return stackSize;
    }

    public void setStackSize(int stackSize) {
        stackSize = stackSize;
    }

    public void incrementStack(int size) {
        stackSize += size;
    }

    public void decrementStack(int size) {
        stackSize -= size;
    }
}
