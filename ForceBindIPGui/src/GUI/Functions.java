/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
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
    static int ARBRITRARY_ARRAY_SIZE = 100;
    static String ProgramFile;
    static Enumeration<NetworkInterface> NetInterface;
    static String UseNetIntAddress;
    static savedConfiguration[] saveConfigs;
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
    
    static void run(boolean i, boolean x64) throws IOException{
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
    
    static boolean loadConfigs(){
        try {
            Scanner inf = new Scanner(new File(CONFIG_FILE_PATH));
            saveConfigs = new savedConfiguration[inf.nextInt()];
            inf.nextLine();
            while(inf.hasNext()){
                String line = inf.nextLine();
                String[] lineArr = line.split("#");
                boolean usei = Boolean.parseBoolean(lineArr[3]);
                boolean x64 = Boolean.parseBoolean(lineArr[4]);
                saveConfigs[numConfigs] = new savedConfiguration(lineArr[0], lineArr[1], lineArr[2], usei, x64);
                numConfigs++;
            }
            inf.close();
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }
    
    static void saveConfigs(String newConf){
        try {
            PrintWriter outf = new PrintWriter(new File(CONFIG_FILE_PATH));
            outf.println(numConfigs+1);
            for(int x = 0; x < numConfigs; x++){
                outf.println(saveConfigs[x].toString());
            }
            outf.print(newConf);
            outf.close();
        } catch (IOException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    static String getConfig(String name){
            boolean found = false;
            int count = 0;
            String config = null;
            while(!found && count < numConfigs){
                if(saveConfigs[count].getName().equals(name)){
                    config = saveConfigs[count].toString();
                    found = true;
                }
            }
            if(found){
                return config;
            }else{
                return null;
            }
    }
    
    static String configNames(){
        String out = "";
        for(int x = 0; x < numConfigs; x++){
            out += saveConfigs[x].getName()+"#";
        }
        return out;
    }
    
}
