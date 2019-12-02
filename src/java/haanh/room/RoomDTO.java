/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.room;

import java.io.Serializable;

/**
 *
 * @author HaAnh
 */
public class RoomDTO implements Serializable {
    
    private Integer id;
    private Integer roomInfoId;

    public RoomDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomInfoId() {
        return roomInfoId;
    }

    public void setRoomInfoId(Integer roomInfoId) {
        this.roomInfoId = roomInfoId;
    }
}
