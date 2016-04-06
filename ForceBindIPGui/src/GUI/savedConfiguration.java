/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

/**
 *
 * @author user
 */
public class savedConfiguration {
    private String name;
    private String NID;
    private String executable;
    private boolean useI;
    private boolean x64;
     
    public savedConfiguration(String name, String nid, String exec, boolean i, boolean x64){
        this.name = name;
        this.NID = nid;
        this.executable = exec;
        this.useI = i;
        this.x64 = x64;
    }
    
    public String getName() {
        return name;
    }
    
    public String toString(){
        String out = this.name + "#" + this.NID + "#" + this.executable + "#"+this.useI+"#"+this.x64;
        return out;
    }
    
}
