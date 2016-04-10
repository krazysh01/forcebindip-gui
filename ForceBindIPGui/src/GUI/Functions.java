/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
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
    static String UseNetIntAddress;
    static SavedProfile[] saveProfiles;
    static int numConfigs = 0;
    
    static boolean programFound(){
        
        boolean check = new File(FORCE_BIND_IP_DIR, FORCE_BIND_IP_NAME).exists();
        return check;
        
    }
    
    static void changeDir(String newDir){
        
        FORCE_BIND_IP_DIR = newDir;
        
    }
    
    static void changeProgramFile(String newFile){
        
        ProgramFile = newFile;
        
    }
    
    static String[] getNetInterfaceInfo(){
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
    
    static void runProcess(boolean i, boolean x64) throws IOException{
        ProcessBuilder p;
        if(i && x64){
            p = new ProcessBuilder("cmd.exe", "/c", "cd "+ FORCE_BIND_IP_DIR + " && " + FORCE_BIND_IP_NAME_64 + " -i " + UseNetIntAddress + " " + ProgramFile );
        }else if(i){
            p = new ProcessBuilder("cmd.exe", "/c", "cd "+ FORCE_BIND_IP_DIR + " && " + FORCE_BIND_IP_NAME + " -i " + UseNetIntAddress + " " + ProgramFile );
        }else if(x64){
            p = new ProcessBuilder("cmd.exe", "/c", "cd "+ FORCE_BIND_IP_DIR + " && " + FORCE_BIND_IP_NAME_64 + " " + UseNetIntAddress + " " + ProgramFile );
        }else{
            p = new ProcessBuilder("cmd.exe", "/c", "cd "+ FORCE_BIND_IP_DIR + " && " + FORCE_BIND_IP_NAME + " " + UseNetIntAddress + " " + ProgramFile );
        }
        p.start();
    }
    
    static boolean loadProfiles(){
        try {
            File profileFile = new File(SAVED_FILE_PATH);
            profileFile.createNewFile();
            Scanner inf = new Scanner(profileFile);
            if(inf.hasNext()){
                saveProfiles = new SavedProfile[inf.nextInt()];
                inf.nextLine();
                while(inf.hasNext()){
                    String line = inf.nextLine();
                    String[] lineArr = line.split("#");
                    boolean usei = Boolean.parseBoolean(lineArr[3]);
                    boolean x64 = Boolean.parseBoolean(lineArr[4]);
                    saveProfiles[numConfigs] = new SavedProfile(lineArr[0], lineArr[1], lineArr[2], usei, x64);
                    numConfigs++;
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
    
    static void loadConfig(){
        try {
            File confFile = new File(CONFIG_FILE_PATH);
            Scanner inf = new Scanner(confFile);
            if(inf.hasNextLine() && inf.next() != ""){
                FORCE_BIND_IP_DIR = inf.nextLine();
                SAVED_FILE_PATH = inf.nextLine();
            }
            System.out.println("value: " + FORCE_BIND_IP_DIR);
            System.out.println("Value: "+SAVED_FILE_PATH);
            inf.close();
        } catch (Exception ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    static boolean saveConfig(){
        try {
            File confFile = new File(CONFIG_FILE_PATH);
            confFile.createNewFile();
            PrintWriter outf = new PrintWriter(confFile);
            outf.println(FORCE_BIND_IP_DIR);
            outf.println(SAVED_FILE_PATH);
            System.out.println(FORCE_BIND_IP_DIR);
            System.out.println(SAVED_FILE_PATH);
            outf.close();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    static void saveProfiles(String newProfile){
        try {
            PrintWriter outf = new PrintWriter(new File(SAVED_FILE_PATH));
            outf.println(numConfigs+1);
            for(int x = 0; x < numConfigs; x++){
                outf.println(saveProfiles[x].toString());
            }
            outf.print(newProfile);
            outf.close();
        } catch (IOException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    static String getProfile(String name){
            boolean found = false;
            int count = 0;
            String config = null;
            while(!found && count < numConfigs){
                if(saveProfiles[count].getName().equals(name)){
                    config = saveProfiles[count].toString();
                    found = true;
                }
            }
            if(found){
                return config;
            }else{
                return null;
            }
    }
    
    static String profileNames(){
        String out = "";
        for(int x = 0; x < numConfigs; x++){
            out += saveProfiles[x].getName()+"#";
        }
        return out;
    }
    
}
