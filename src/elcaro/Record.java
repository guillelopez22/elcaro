/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcaro;

import java.util.ArrayList;

/**
 *
 * @author Don Papi
 */
public class Record {
    ArrayList data = new ArrayList();
    boolean active = true;

    public Record() {
    }

    public ArrayList getData() {
        return data;
    }

    public void setData(String data) {
        this.data.add(data);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive() {
        this.active = true;
    }
    public void setInactive(){
        this.active = false;
    }
    
    
    
}
