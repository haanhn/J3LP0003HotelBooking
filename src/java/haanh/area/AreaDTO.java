/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.area;

import java.io.Serializable;

/**
 *
 * @author HaAnh
 */
public class AreaDTO implements Serializable {
    
    private Integer id;
    private String name;

    public AreaDTO() {
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
