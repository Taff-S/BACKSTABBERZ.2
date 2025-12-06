package me.taff_s.game.world;

import me.taff_s.game.world.RoomType;

public class RoomManager {
    private int currentRoom = 0;
    //private final int totalRooms = 13; // 9 normal, 3 rests + 1 boss

    private final RoomType[] roomSequence = {
        RoomType.COMBAT,    // 1
        RoomType.COMBAT,    // 2
        RoomType.ENCOUNTER, // 3
        RoomType.REST,      // 4
        RoomType.COMBAT,    // 5
        RoomType.ENCOUNTER, // 6
        RoomType.COMBAT,    // 7
        RoomType.REST,      // 8
        RoomType.ENCOUNTER, // 9
        RoomType.COMBAT,    //10
        RoomType.COMBAT,    //11
        RoomType.REST,      //12
        RoomType.BOSS       //13
    };

    public RoomType getNextRoomType() {
        if (currentRoom >= roomSequence.length) {
            throw new IllegalStateException("No more rooms");
        }
        return roomSequence[currentRoom++];
    }

    public boolean hasMoreRooms() {
        return currentRoom < roomSequence.length;
    }
}
