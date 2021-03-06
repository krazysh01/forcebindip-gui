/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Objects.SavedProfile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Functions {
    
    static String FORCE_BIND_IP_DIR = "C:/Program Files (x86)/ForceBindIP";
    static String FORCE_BIND_IP_NAME = "ForceBindIP.exe";
    static String FORCE_BIND_IP_NAME_64 = "ForceBindIP64.exe";
    static String CONFIG_FILE_PATH = "Config.ini";
    static String SAVED_FILE_PATH = "Profiles.ini";
    static int ARBRITRARY_ARRAY_SIZE = 100;
    static String ProgramFile;
    static Enumeration<NetworkInterface> NetInterface;
    static String UseNetIntAddress ="127.0.0.1";
    static LinkedList<SavedProfile> saveProfiles = new LinkedList<>();
    static String cmdOptions = "";
    
    public static boolean programFound(){
        
        boolean check = new File(FORCE_BIND_IP_DIR, FORCE_BIND_IP_NAME).exists();
        return check;
        
    }
    
    public static void changeDir(String newDir){
        
        FORCE_BIND_IP_DIR = newDir;
        
    }
    
    public static void changeProgramFile(String newFile){
        
        ProgramFile = newFile;
        
    }
    
    public static String[] getNetInterfaceInfo(){
        String[] outArr = new String[ARBRITRARY_ARRAY_SIZE];
        int count = 0;
        for (NetworkInterface netIf : Collections.list(NetInterface)) {
            if(netIf.getInetAddresses().hasMoreElements() && netIf.getInetAddresses().nextElement().getHostAddress().charAt(3) == '.'){
                outArr[count] = netIf.getDisplayName() + " " + netIf.getInetAddresses().nextElement().getHostAddress();
                count++;
            }
        }
        String[] newArr = new String[count];
        for(int x = 0; x < count; x++)
            newArr[x] = outArr[x];
        return newArr;
    }
    
    public static Process runProcess(boolean i, boolean x64) throws IOException{
        ProcessBuilder pb;
        String cmd;
        if(i && x64){
            cmd = "cd "+ FORCE_BIND_IP_DIR + " && " + FORCE_BIND_IP_NAME_64 + " -i " + UseNetIntAddress + " " + ProgramFile;
        }else if(i){
            cmd =  "cd "+ FORCE_BIND_IP_DIR + " && " + FORCE_BIND_IP_NAME + " -i " + UseNetIntAddress + " " + ProgramFile;
        }else if(x64){
            cmd =  "cd "+ FORCE_BIND_IP_DIR + " && " + FORCE_BIND_IP_NAME_64 + " " + UseNetIntAddress + " " + ProgramFile;
        }else{
            cmd =  "cd "+ FORCE_BIND_IP_DIR + " && " + FORCE_BIND_IP_NAME + " " + UseNetIntAddress + " " + ProgramFile;
        }
        System.out.println(cmd);
        pb = new ProcessBuilder("cmd.exe", "/c", cmd, cmdOptions);
        pb.redirectErrorStream(true).inheritIO();
        Process p =  pb.start();
        return p;
    }
    
    public static boolean loadProfiles(){
        try {
            File profileFile = new File(SAVED_FILE_PATH);
            profileFile.createNewFile();
            Scanner inf = new Scanner(profileFile);
            if(inf.hasNext()){
                if(inf.hasNextInt())
                    inf.nextLine();
                while(inf.hasNext()){
                    String line = inf.nextLine();
                    String[] lineArr = line.split("#");
                    boolean usei = Boolean.parseBoolean(lineArr[3]);
                    boolean x64 = Boolean.parseBoolean(lineArr[4]);
                    switch (lineArr.length) {
                        case 5:
                            saveProfiles.add(new SavedProfile(lineArr[0], lineArr[1], lineArr[2], usei, x64));
                            break;
                        case 6:
                            saveProfiles.add(new SavedProfile(lineArr[0], lineArr[1], lineArr[2], usei, x64, lineArr[5]));
                            break;
                        default:
                            System.out.println("Profile: "+lineArr[0]+" Corrupted");
                            break;
                    }
                }
                inf.close();
                return true;
            }else{
                return false;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File " + SAVED_FILE_PATH + " not found. Creating it.");
            return false;
        } catch (IOException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }
    
    public static void loadConfig(){
        try {
            File confFile = new File(CONFIG_FILE_PATH);
            Scanner inf = new Scanner(confFile);
            if(inf.hasNextLine() /*&& inf.next() != ""*/){
                FORCE_BIND_IP_DIR = inf.nextLine();
                //SAVED_FILE_PATH = inf.nextLine();
            }
            System.out.println("value: " + FORCE_BIND_IP_DIR);
            System.out.println("Value: "+SAVED_FILE_PATH);
            inf.close();
        } catch (Exception ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static boolean saveConfig(){
        try {
            File confFile = new File(CONFIG_FILE_PATH);
            confFile.createNewFile();
            PrintWriter outf = new PrintWriter(confFile);
            outf.println(FORCE_BIND_IP_DIR);
            //outf.println(SAVED_FILE_PATH);
            System.out.println(FORCE_BIND_IP_DIR);
            //System.out.println(SAVED_FILE_PATH);
            outf.close();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public static void saveProfile(SavedProfile newProfile){
        if(confExists(newProfile.getName()) == -1){
            saveProfiles.add(newProfile);
        }else{
            int index = confExists(newProfile.getName());
            saveProfiles.remove(index);
            saveProfiles.add(index, newProfile);
        }  
        try {
            PrintWriter outf = new PrintWriter(new File(SAVED_FILE_PATH));
            for(int x = 0; x < saveProfiles.size(); x++){
                outf.println(saveProfiles.get(x).toString());
            }
            //outf.print(newProfile);
            outf.close();
        } catch (IOException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    
    public static String getProfile(String name){
        boolean found = false;
        int count = 0;
        System.out.println("Looking for "+name);
        String config = null;
        while(!found && count < saveProfiles.size()){
            if(saveProfiles.get(count).getName().equals(name)){
                config = saveProfiles.get(count).toString();
                System.out.println("Found as "+config);
                found = true;
            }
            count++;
        }
        if(found){
            return config;
        }else{
            return null;
        }
    }
    
    public static String profileNames(){
        String out = "";
        for(int x = 0; x < saveProfiles.size(); x++){
            out += saveProfiles.get(x).getName()+"#";
        }
        return out;
    }
    
    public static void setCommandOptions(String op){
        cmdOptions = op;
    }
    
    public static String getCommandOptions(){
        return cmdOptions;
    }
    
    public static int confExists(String Name){
        if(saveProfiles.size() == 0)
            return -1;
        boolean found = false;
        int count = saveProfiles.size();
        while(!found && count > 0 ){
            count--;
            if(saveProfiles.get(count).getName() == null ? Name == null : saveProfiles.get(count).getName().equals(Name))
                found = true;
        }
        return count;
    }

    public static String getProgramFile() {
        return ProgramFile;
    }

    public static Enumeration<NetworkInterface> getNetInterface() {
        return NetInterface;
    }

    public static String getUseNetIntAddress() {
        return UseNetIntAddress;
    }

    public static void setProgramFile(String ProgramFile) {
        Functions.ProgramFile = ProgramFile;
    }

    public static void setNetInterface(Enumeration<NetworkInterface> NetInterface) {
        Functions.NetInterface = NetInterface;
    }

    public static void setUseNetIntAddress(String UseNetIntAddress) {
        Functions.UseNetIntAddress = UseNetIntAddress;
    }
    
    
    
    
}
