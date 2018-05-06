package ru.pavel.queuehandler.entity;

public class Item {
    private int itemId;
    private int groupId;

    public Item(int itemId, int groupId) {
        this.itemId = itemId;
        this.groupId = groupId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", groupId=" + groupId +
                '}';
    }
}
