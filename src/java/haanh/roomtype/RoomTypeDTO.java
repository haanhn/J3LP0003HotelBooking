/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.roomtype;

import java.io.Serializable;

/**
 *
 * @author HaAnh
 */
public class RoomTypeDTO implements Serializable {
    
    private Integer id;
    private String name;

    public RoomTypeDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
