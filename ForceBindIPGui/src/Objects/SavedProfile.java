/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

/**
 *
 * @author user
 */
public class SavedProfile {
    private String name;
    private String NID;
    private String executable;
    private String cmdOption;
    private boolean useI;
    private boolean x64;
     
    public SavedProfile(String name, String nid, String exec, boolean i, boolean x64){
        this.name = name;
        this.NID = nid;
        this.executable = exec;
        this.useI = i;
        this.x64 = x64;
        this.cmdOption = "";
    }
    
    public SavedProfile(String name, String nid, String exec, boolean i, boolean x64, String cmd){
        this.name = name;
        this.NID = nid;
        this.executable = exec;
        this.useI = i;
        this.x64 = x64;
        this.cmdOption = cmd;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString(){
        String out = "";
        if(cmdOption == "")
            out = this.name + "#" + this.NID + "#" + this.executable + "#"+this.useI+"#"+this.x64;
        else
            out = this.name + "#" + this.NID + "#" + this.executable + "#"+this.useI+"#"+this.x64+"#"+this.cmdOption;
        return out;
    }
    
}
