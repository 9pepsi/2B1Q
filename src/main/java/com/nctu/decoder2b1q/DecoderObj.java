package com.nctu.decoder2b1q;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.math.BigInteger;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author Mohammed Youssef Ahmed Alabd
 */

public class DecoderObj extends JPanel{

    public static String[] duo_binary_array;
    public static String[] voltage_db_array;
    public static int previous_level=0; 

    public DecoderObj(String[] duo_binary,String[] voltage){
        DecoderObj.duo_binary_array = duo_binary;
        DecoderObj.voltage_db_array = voltage;
    }
    
    public DecoderObj decrypt(String hexa){
        
        String[] store_hex_to_binary_result = convert_hex_to_binary(hexa).split("(?!^)");
        String[] duo_binary_array = new String[(store_hex_to_binary_result.length+1)/2];
        String[] voltage_db_array = new String[duo_binary_array.length];
         
        //if binary numbers are not even digits
        if(isEven(store_hex_to_binary_result.length)==false){
            
            store_hex_to_binary_result = ArrayUtils.insert(0, store_hex_to_binary_result,"0");
            }
            int newCounter=0;
            for(int i=0; i<duo_binary_array.length; i++){
               
                duo_binary_array[i]=store_hex_to_binary_result[newCounter]+store_hex_to_binary_result[newCounter+1];
                newCounter+=2;}

            for(int k=0; k<duo_binary_array.length; ++k){
                if(previous_level==1){
                    if(duo_binary_array[k].equals("00")){voltage_db_array[k]="+1";}
                    if(duo_binary_array[k].equals("01")){voltage_db_array[k]="+3";}
                    if(duo_binary_array[k].equals("10")){voltage_db_array[k]="-1"; previous_level=-1;}
                    if(duo_binary_array[k].equals("11")){voltage_db_array[k]="-3"; previous_level=-1;}
                }
                else if(previous_level==-1){
                    if(duo_binary_array[k].equals("00")){voltage_db_array[k]="-1";}
                    if(duo_binary_array[k].equals("01")){voltage_db_array[k]="-3";}
                    if(duo_binary_array[k].equals("10")){voltage_db_array[k]="+1"; previous_level=1;}
                    if(duo_binary_array[k].equals("11")){voltage_db_array[k]="+3"; previous_level=1;}
                } else{;}
            }
            
        return new DecoderObj(duo_binary_array,voltage_db_array);
    }
    
    public DecoderObj uart_decrypt(String hexa,String parity){
        //variable declrations
        String[] hexa_to_binary_lsb = convert_hex_to_binary(hexa).split("(?!^)");
        int count_of_one = 0;
        
        //shifting left if length is odd
        if(isEven(hexa_to_binary_lsb.length)==false){
            hexa_to_binary_lsb = ArrayUtils.insert(0, hexa_to_binary_lsb,"0");
        }
        //reversing binary so LSB is first
        for (int i=0; i<hexa_to_binary_lsb.length/2; i++){
            String swap = hexa_to_binary_lsb[i];
            hexa_to_binary_lsb[i] = hexa_to_binary_lsb[hexa_to_binary_lsb.length-i-1];
			hexa_to_binary_lsb[hexa_to_binary_lsb.length-i-1] = swap;
        }
        //tallying number of 1s to add parity bit
        for(int i=0; i<hexa_to_binary_lsb.length;i++){
            if(hexa_to_binary_lsb[i].equals("1")){
                count_of_one++;
            }
        }
        //adding parity bit
        if(parity == "Odd"){
            if(!isEven(count_of_one)){
                hexa_to_binary_lsb = ArrayUtils.insert(hexa_to_binary_lsb.length, hexa_to_binary_lsb, "0");
            }else if(isEven(count_of_one)){
                hexa_to_binary_lsb = ArrayUtils.insert(hexa_to_binary_lsb.length, hexa_to_binary_lsb, "1");
            }

        }else if(parity == "Even"){
            if(!isEven(count_of_one)){
                hexa_to_binary_lsb = ArrayUtils.insert(hexa_to_binary_lsb.length, hexa_to_binary_lsb, "1");
            }else if(isEven(count_of_one)){
                hexa_to_binary_lsb = ArrayUtils.insert(hexa_to_binary_lsb.length, hexa_to_binary_lsb, "0");
            }
        }
        //adding start bit
        hexa_to_binary_lsb = ArrayUtils.insert(0, hexa_to_binary_lsb,"0");
        //adding stop bits
        hexa_to_binary_lsb = ArrayUtils.insert(hexa_to_binary_lsb.length, hexa_to_binary_lsb,"1");
        hexa_to_binary_lsb = ArrayUtils.insert(hexa_to_binary_lsb.length, hexa_to_binary_lsb,"1");
        //2B1Q
        String[] duo_binary_array = new String[(hexa_to_binary_lsb.length+1)/2];
        String[] voltage_db_array = new String[duo_binary_array.length];
        int newCounter=0;
            for(int i=0; i<duo_binary_array.length; i++){
               
                duo_binary_array[i]=hexa_to_binary_lsb[newCounter]+hexa_to_binary_lsb[newCounter+1];
                newCounter+=2;}

            for(int k=0; k<duo_binary_array.length; ++k){
                if(previous_level==1){
                    if(duo_binary_array[k].equals("00")){voltage_db_array[k]="+1";}
                    if(duo_binary_array[k].equals("01")){voltage_db_array[k]="+3";}
                    if(duo_binary_array[k].equals("10")){voltage_db_array[k]="-1"; previous_level=-1;}
                    if(duo_binary_array[k].equals("11")){voltage_db_array[k]="-3"; previous_level=-1;}
                }
                else if(previous_level==-1){
                    if(duo_binary_array[k].equals("00")){voltage_db_array[k]="-1";}
                    if(duo_binary_array[k].equals("01")){voltage_db_array[k]="-3";}
                    if(duo_binary_array[k].equals("10")){voltage_db_array[k]="+1"; previous_level=1;}
                    if(duo_binary_array[k].equals("11")){voltage_db_array[k]="+3"; previous_level=1;}
                } else{;}
            }
            return new DecoderObj(duo_binary_array,voltage_db_array);
    }

    public static String[] getDB(){return duo_binary_array;}
    public static String[] getVoltage(){return voltage_db_array;}
    public void setDB(String[] d_b_a){DecoderObj.duo_binary_array= d_b_a;}
    public void setVoltage(String[] v_a){DecoderObj.voltage_db_array=v_a;}
    public void setPL(int plvl){DecoderObj.previous_level=plvl;}
    public static String convert_hex_to_binary(String hex) {
        hex = new BigInteger(hex, 16).toString(2);
        return hex;}
    public static String convert_hex_to_decimal(String hex){
        hex = new BigInteger(hex, 16).toString(10);
        return hex;}
    public static String convert_hex_to_ascii(String hex){
        StringBuilder ascii = new StringBuilder();
        if(hex.length()%2!=0){;}else{
        for (int i = 0; i < hex.length(); i = i + 2) {
            String s = hex.substring(i, i + 2);
            int n = Integer.valueOf(s, 16);
            ascii.append((char)n);
         }
          }
        return ascii.toString();
    }   
    public static boolean isEven(int num){return (num % 2 == 0);}

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.setBackground(Color.BLACK);
        //some variables declration
        Font font = new Font("SansSerif", Font.PLAIN, 15);
        int len_start = 25, len_end = 75, alt3p = 35, alt1p = alt3p+45, alt1n=alt3p+95, alt3n=alt3p+135;
        //
        g.setColor(Color.WHITE);
        g.setFont(font);
        //drawing graph x and y axis
        g.drawLine(25, 20, 25, 185); g.drawLine(25, 105, 525, 105);
        g.drawString(" +3", 0, 30); g.drawString(" +1", 0, 75);
        g.drawString(" -1", 5, 125); g.drawString(" -3", 5, 165);
        /*drawing results*/
        g.setColor(Color.RED);
        if (voltage_db_array != null) {
            for (int counter = 0; counter<voltage_db_array.length; counter++) {
                //draw value
                     if(voltage_db_array[counter].equals("+3")){g.drawLine(len_start, alt3p, len_end, alt3p);}
                else if(voltage_db_array[counter].equals("+1")){g.drawLine(len_start, alt1p, len_end, alt1p);}
                else if(voltage_db_array[counter].equals("-1")){g.drawLine(len_start, alt1n, len_end, alt1n);}
                else if(voltage_db_array[counter].equals("-3")){g.drawLine(len_start, alt3n, len_end, alt3n);}
                //draw first speical case connector
                if(counter==0 && voltage_db_array.length!=1){
                    switch(voltage_db_array[0]){
                        case "+3":
                                 if(voltage_db_array[1].equals("+1")){g.drawLine(len_end, alt3p, len_end, alt1p);}
                            else if(voltage_db_array[1].equals("-1")){g.drawLine(len_end, alt3p, len_end, alt1n);}
                            else if(voltage_db_array[1].equals("-3")){g.drawLine(len_end, alt3p, len_end, alt3n);}
                            break;
                        case "+1":
                                 if(voltage_db_array[1].equals("+3")){g.drawLine(len_end, alt1p, len_end, alt3p);}
                            else if(voltage_db_array[1].equals("-1")){g.drawLine(len_end, alt1p, len_end, alt1n);}
                            else if(voltage_db_array[1].equals("-3")){g.drawLine(len_end, alt1p, len_end, alt3n);}
                            break;
                        case "-1":
                                 if(voltage_db_array[1].equals("+3")){g.drawLine(len_end, alt1n, len_end, alt3p);}
                            else if(voltage_db_array[1].equals("+1")){g.drawLine(len_end, alt1n, len_end, alt1p);}
                            else if(voltage_db_array[1].equals("-3")){g.drawLine(len_end, alt1n, len_end, alt3n);}
                            break;
                        case "-3":
                                 if(voltage_db_array[1].equals("+3")){g.drawLine(len_end, alt3n, len_end, alt3p);}
                            else if(voltage_db_array[1].equals("+1")){g.drawLine(len_end, alt3n, len_end, alt1p);}
                            else if(voltage_db_array[1].equals("-1")){g.drawLine(len_end, alt3n, len_end, alt1n);}
                            break;
                        }
                    }
                //draw rest of the connectors
                while(counter < voltage_db_array.length-1 && counter >= 1){
                         if(voltage_db_array[counter].equals("+3")){
                                 if(voltage_db_array[counter-1].equals("+1") && voltage_db_array[counter+1].equals("+1")){g.drawLine(len_start, alt1p, len_start, alt3p); g.drawLine(len_end, alt3p, len_end, alt1p);}
                            else if(voltage_db_array[counter-1].equals("+1") && voltage_db_array[counter+1].equals("-1")){g.drawLine(len_start, alt1p, len_start, alt3p); g.drawLine(len_end, alt3p, len_end, alt1n);}
                            else if(voltage_db_array[counter-1].equals("+1") && voltage_db_array[counter+1].equals("-3")){g.drawLine(len_start, alt1p, len_start, alt3p); g.drawLine(len_end, alt3p, len_end, alt3n);}
                            else if(voltage_db_array[counter-1].equals("+1") && voltage_db_array[counter+1].equals("+3")){g.drawLine(len_start, alt1p, len_start, alt3p); g.drawLine(len_end, alt3p, len_end, alt3p);}
                            else if(voltage_db_array[counter-1].equals("-1") && voltage_db_array[counter+1].equals("+1")){g.drawLine(len_start, alt1n, len_start, alt3p); g.drawLine(len_end, alt3p, len_end, alt1p);}
                            else if(voltage_db_array[counter-1].equals("-1") && voltage_db_array[counter+1].equals("-1")){g.drawLine(len_start, alt1n, len_start, alt3p); g.drawLine(len_end, alt3p, len_end, alt1n);}
                            else if(voltage_db_array[counter-1].equals("-1") && voltage_db_array[counter+1].equals("-3")){g.drawLine(len_start, alt1n, len_start, alt3p); g.drawLine(len_end, alt3p, len_end, alt3n);}
                            else if(voltage_db_array[counter-1].equals("-1") && voltage_db_array[counter+1].equals("+3")){g.drawLine(len_start, alt1n, len_start, alt3p); g.drawLine(len_end, alt3p, len_end, alt3p);}
                            else if(voltage_db_array[counter-1].equals("-3") && voltage_db_array[counter+1].equals("+1")){g.drawLine(len_start, alt3n, len_start, alt3p); g.drawLine(len_end, alt3p, len_end, alt1p);}
                            else if(voltage_db_array[counter-1].equals("-3") && voltage_db_array[counter+1].equals("-1")){g.drawLine(len_start, alt3n, len_start, alt3p); g.drawLine(len_end, alt3p, len_end, alt1n);}
                            else if(voltage_db_array[counter-1].equals("-3") && voltage_db_array[counter+1].equals("-3")){g.drawLine(len_start, alt3n, len_start, alt3p); g.drawLine(len_end, alt3p, len_end, alt3n);}
                            else if(voltage_db_array[counter-1].equals("-3") && voltage_db_array[counter+1].equals("+3")){g.drawLine(len_start, alt3n, len_start, alt3p); g.drawLine(len_end, alt3p, len_end, alt3p);}
                            else if(voltage_db_array[counter-1].equals("+3") && voltage_db_array[counter+1].equals("+1")){g.drawLine(len_start, alt3p, len_start, alt3p); g.drawLine(len_end, alt3p, len_end, alt1p);}
                            else if(voltage_db_array[counter-1].equals("+3") && voltage_db_array[counter+1].equals("-1")){g.drawLine(len_start, alt3p, len_start, alt3p); g.drawLine(len_end, alt3p, len_end, alt1n);}
                            else if(voltage_db_array[counter-1].equals("+3") && voltage_db_array[counter+1].equals("-3")){g.drawLine(len_start, alt3p, len_start, alt3p); g.drawLine(len_end, alt3p, len_end, alt3n);}
                                }
                    else if(voltage_db_array[counter].equals("+1")){
                             if(voltage_db_array[counter-1].equals("+3")&&voltage_db_array[counter+1].equals("+3")){g.drawLine(len_start, alt3p, len_start, alt1p); g.drawLine(len_end, alt1p,len_end , alt3p);}
                        else if(voltage_db_array[counter-1].equals("+3")&&voltage_db_array[counter+1].equals("-1")){g.drawLine(len_start, alt3p, len_start, alt1p); g.drawLine(len_end, alt1p, len_end, alt1n);}
                        else if(voltage_db_array[counter-1].equals("+3")&&voltage_db_array[counter+1].equals("-3")){g.drawLine(len_start, alt3p, len_start, alt1p); g.drawLine(len_end, alt1p, len_end, alt3n);}
                        else if(voltage_db_array[counter-1].equals("+3")&&voltage_db_array[counter+1].equals("+1")){g.drawLine(len_start, alt3p, len_start, alt1p); g.drawLine(len_end, alt1p, len_end, alt1p);}
                        else if(voltage_db_array[counter-1].equals("-1")&&voltage_db_array[counter+1].equals("+3")){g.drawLine(len_start, alt1n, len_start, alt1p); g.drawLine(len_end, alt1p, len_end, alt3p);}
                        else if(voltage_db_array[counter-1].equals("-1")&&voltage_db_array[counter+1].equals("-1")){g.drawLine(len_start, alt1n, len_start, alt1p); g.drawLine(len_end, alt1p, len_end, alt1n);}
                        else if(voltage_db_array[counter-1].equals("-1")&&voltage_db_array[counter+1].equals("-3")){g.drawLine(len_start, alt1n, len_start, alt1p); g.drawLine(len_end, alt1p, len_end, alt3n);}
                        else if(voltage_db_array[counter-1].equals("-1")&&voltage_db_array[counter+1].equals("+1")){g.drawLine(len_start, alt1n, len_start, alt1p); g.drawLine(len_end, alt1p, len_end, alt1p);}
                        else if(voltage_db_array[counter-1].equals("-3")&&voltage_db_array[counter+1].equals("+3")){g.drawLine(len_start, alt3n, len_start, alt1p); g.drawLine(len_end, alt1p, len_end, alt3p);}
                        else if(voltage_db_array[counter-1].equals("-3")&&voltage_db_array[counter+1].equals("-1")){g.drawLine(len_start, alt3n, len_start, alt1p); g.drawLine(len_end, alt1p, len_end, alt1n);}
                        else if(voltage_db_array[counter-1].equals("-3")&&voltage_db_array[counter+1].equals("-3")){g.drawLine(len_start, alt3n, len_start, alt1p); g.drawLine(len_end, alt1p, len_end, alt3n);}
                        else if(voltage_db_array[counter-1].equals("-3")&&voltage_db_array[counter+1].equals("+1")){g.drawLine(len_start, alt3n, len_start, alt1p); g.drawLine(len_end, alt1p, len_end, alt1p);}
                        else if(voltage_db_array[counter-1].equals("+1")&&voltage_db_array[counter+1].equals("+3")){g.drawLine(len_start, alt1p, len_start, alt1p); g.drawLine(len_end, alt1p, len_end, alt3p);}
                        else if(voltage_db_array[counter-1].equals("+1")&&voltage_db_array[counter+1].equals("-1")){g.drawLine(len_start, alt1p, len_start, alt1p); g.drawLine(len_end, alt1p, len_end, alt1n);}
                        else if(voltage_db_array[counter-1].equals("+1")&&voltage_db_array[counter+1].equals("-3")){g.drawLine(len_start, alt1p, len_start, alt1p); g.drawLine(len_end, alt1p, len_end, alt3n);} 
                    }
                    else if(voltage_db_array[counter].equals("-1")){
                         if(voltage_db_array[counter-1].equals("+3") && voltage_db_array[counter+1].equals("+3")){g.drawLine(len_start, alt3p, len_start, alt1n); g.drawLine(len_end, alt1n,len_end , alt3p);}
                    else if(voltage_db_array[counter-1].equals("+3") && voltage_db_array[counter+1].equals("+1")){g.drawLine(len_start, alt3p, len_start, alt1n); g.drawLine(len_end, alt1n, len_end, alt1p);}
                    else if(voltage_db_array[counter-1].equals("+3") && voltage_db_array[counter+1].equals("-3")){g.drawLine(len_start, alt3p, len_start, alt1n); g.drawLine(len_end, alt1n, len_end, alt3n);}
                    else if(voltage_db_array[counter-1].equals("+3") && voltage_db_array[counter+1].equals("-1")){g.drawLine(len_start, alt3p, len_start, alt1n); g.drawLine(len_end, alt1n, len_end, alt1n);}
                    else if(voltage_db_array[counter-1].equals("+1") && voltage_db_array[counter+1].equals("+3")){g.drawLine(len_start, alt1p, len_start, alt1n); g.drawLine(len_end, alt1n, len_end, alt3p);}
                    else if(voltage_db_array[counter-1].equals("+1") && voltage_db_array[counter+1].equals("+1")){g.drawLine(len_start, alt1p, len_start, alt1n); g.drawLine(len_end, alt1n, len_end, alt1p);}
                    else if(voltage_db_array[counter-1].equals("+1") && voltage_db_array[counter+1].equals("-3")){g.drawLine(len_start, alt1p, len_start, alt1n); g.drawLine(len_end, alt1n, len_end, alt3n);}
                    else if(voltage_db_array[counter-1].equals("+1") && voltage_db_array[counter+1].equals("-1")){g.drawLine(len_start, alt1p, len_start, alt1n); g.drawLine(len_end, alt1n, len_end, alt1n);}
                    else if(voltage_db_array[counter-1].equals("-3") && voltage_db_array[counter+1].equals("+3")){g.drawLine(len_start, alt3n, len_start, alt1n); g.drawLine(len_end, alt1n, len_end, alt3p);}
                    else if(voltage_db_array[counter-1].equals("-3") && voltage_db_array[counter+1].equals("+1")){g.drawLine(len_start, alt3n, len_start, alt1n); g.drawLine(len_end, alt1n, len_end, alt1p);}
                    else if(voltage_db_array[counter-1].equals("-3") && voltage_db_array[counter+1].equals("-3")){g.drawLine(len_start, alt3n, len_start, alt1n); g.drawLine(len_end, alt1n, len_end, alt3n);}
                    else if(voltage_db_array[counter-1].equals("-3") && voltage_db_array[counter+1].equals("-1")){g.drawLine(len_start, alt3n, len_start, alt1n); g.drawLine(len_end, alt1n, len_end, alt1n);}
                    else if(voltage_db_array[counter-1].equals("-1") && voltage_db_array[counter+1].equals("+3")){g.drawLine(len_start, alt1n, len_start, alt1n); g.drawLine(len_end, alt1n, len_end, alt3p);}
                    else if(voltage_db_array[counter-1].equals("-1") && voltage_db_array[counter+1].equals("+1")){g.drawLine(len_start, alt1n, len_start, alt1n); g.drawLine(len_end, alt1n, len_end, alt1p);}
                    else if(voltage_db_array[counter-1].equals("-1") && voltage_db_array[counter+1].equals("-3")){g.drawLine(len_start, alt1n, len_start, alt1n); g.drawLine(len_end, alt1n, len_end, alt3n);}
                    }
                    else if(voltage_db_array[counter].equals("-3")){
                         if(voltage_db_array[counter-1].equals("+3") && voltage_db_array[counter+1].equals("+3")){g.drawLine(len_start, alt3p, len_start, alt3n); g.drawLine(len_end, alt3n,len_end , alt3p);}
                    else if(voltage_db_array[counter-1].equals("+3") && voltage_db_array[counter+1].equals("+1")){g.drawLine(len_start, alt3p, len_start, alt3n); g.drawLine(len_end, alt3n, len_end, alt1p);}
                    else if(voltage_db_array[counter-1].equals("+3") && voltage_db_array[counter+1].equals("-1")){g.drawLine(len_start, alt3p, len_start, alt3n); g.drawLine(len_end, alt3n, len_end, alt1n);}
                    else if(voltage_db_array[counter-1].equals("+3") && voltage_db_array[counter+1].equals("-3")){g.drawLine(len_start, alt3p, len_start, alt3n); g.drawLine(len_end, alt3n, len_end, alt3n);}
                    else if(voltage_db_array[counter-1].equals("+1") && voltage_db_array[counter+1].equals("+3")){g.drawLine(len_start, alt1p, len_start, alt3n); g.drawLine(len_end, alt3n, len_end, alt3p);}
                    else if(voltage_db_array[counter-1].equals("+1") && voltage_db_array[counter+1].equals("+1")){g.drawLine(len_start, alt1p, len_start, alt3n); g.drawLine(len_end, alt3n, len_end, alt1p);}
                    else if(voltage_db_array[counter-1].equals("+1") && voltage_db_array[counter+1].equals("-1")){g.drawLine(len_start, alt1p, len_start, alt3n); g.drawLine(len_end, alt3n, len_end, alt1n);}
                    else if(voltage_db_array[counter-1].equals("+1") && voltage_db_array[counter+1].equals("-3")){g.drawLine(len_start, alt1p, len_start, alt3n); g.drawLine(len_end, alt3n, len_end, alt3n);}
                    else if(voltage_db_array[counter-1].equals("-1") && voltage_db_array[counter+1].equals("+3")){g.drawLine(len_start, alt1n, len_start, alt3n); g.drawLine(len_end, alt3n, len_end, alt3p);}
                    else if(voltage_db_array[counter-1].equals("-1") && voltage_db_array[counter+1].equals("+1")){g.drawLine(len_start, alt1n, len_start, alt3n); g.drawLine(len_end, alt3n, len_end, alt1p);}
                    else if(voltage_db_array[counter-1].equals("-1") && voltage_db_array[counter+1].equals("-1")){g.drawLine(len_start, alt1n, len_start, alt3n); g.drawLine(len_end, alt3n, len_end, alt1n);}
                    else if(voltage_db_array[counter-1].equals("-1") && voltage_db_array[counter+1].equals("-3")){g.drawLine(len_start, alt1n, len_start, alt3n); g.drawLine(len_end, alt3n, len_end, alt3n);}
                    else if(voltage_db_array[counter-1].equals("-3") && voltage_db_array[counter+1].equals("+3")){g.drawLine(len_start, alt3n, len_start, alt3n); g.drawLine(len_end, alt3n, len_end, alt3p);}
                    else if(voltage_db_array[counter-1].equals("-3") && voltage_db_array[counter+1].equals("+1")){g.drawLine(len_start, alt3n, len_start, alt3n); g.drawLine(len_end, alt3n, len_end, alt1p);}
                    else if(voltage_db_array[counter-1].equals("-3") && voltage_db_array[counter+1].equals("-1")){g.drawLine(len_start, alt3n, len_start, alt3n); g.drawLine(len_end, alt3n, len_end, alt1n);}
                    }
                    break;
                }
                //draw binary duo value
                g.setColor(Color.WHITE);
                g.drawString(duo_binary_array[counter],len_start+20 , alt3p-5);
                g.setColor(Color.RED);
                //getting ready for next value
                len_start +=50;
                len_end +=50;
                }

        }
         //drawing points on y axis
         g.setColor(Color.WHITE);
         g.fillOval(20, 30, 10, 10); g.fillOval(20, 75, 10, 10); 
         g.fillOval(20, 125, 10, 10); g.fillOval(20, 165, 10, 10);
         
    }
    
    public static void main(String args[]){
        //Set UI Theme according to OS
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {}
        //variable declrations
        JFrame f = new JFrame("2B1Q Decoder - NCTU Class of 2023");
        JLabel hex_label = new JLabel("Enter Hexadecimal Number: ");
        JLabel db_label = new JLabel("Binary Duos: "); 
        JLabel volt_label = new JLabel("Voltage Levels: ");
        JLabel credits_label = new JLabel("<html><font size=\"4\">Made By Mohamed Yousef Al Abd </font><font color=\"blue\" size=\"4\"><u>Click Me!</u></font>");
        JLabel graph_label = new JLabel("Graph: ");
        JLabel ascii_label = new JLabel("ASCII: ");
        JLabel decimal_label = new JLabel("Decimal: ");
        JTextField hex_text = new JTextField("");
        JTextField bin_text = new JTextField("");
        JTextField volt_text = new JTextField("");
        JTextField ascii_text = new JTextField("");
        JTextField decimal_text = new JTextField("");
        JButton operate_bt = new JButton("Draw");
        JButton reset_bt = new JButton("Reset");
        JButton exit_bt = new JButton("Exit");
        DecoderObj p =new DecoderObj(DecoderObj.getDB(), DecoderObj.getVoltage());
        JRadioButton plvl = new JRadioButton("Positive Start");
        JRadioButton nlvl = new JRadioButton("Negative Start");
        JComboBox<String> parity = new JComboBox<>();
        JCheckBox uart_format = new JCheckBox("UART Format");
        ButtonGroup radio_group_lvl = new ButtonGroup();
        //end-variable-declration
        /*method calls*/
        //frame 
        f.setIconImage(new ImageIcon("C:\\Users\\mj\\Documents\\NetBeansProjects\\Decoder2B1Q\\src\\main\\resources\\icon.jpg").getImage());
        Font font = new Font("SansSerif", Font.PLAIN, 15);
        f.setFont(font);
        f.setLayout(null);
        f.setSize(600, 400);
        f.setVisible(true);
        f.getRootPane().setDefaultButton(operate_bt);
        f.setResizable(false);
        //adding components 
        f.add(p); f.add(hex_label); f.add(db_label); f.add(volt_label); f.add(credits_label);
        f.add(graph_label); f.add(decimal_label); f.add(hex_text); f.add(bin_text); f.add(volt_text); f.add(operate_bt);
        f.add(reset_bt); f.add(exit_bt); f.add(plvl); f.add(nlvl); f.add(parity); f.add(uart_format); f.add(ascii_label);f.add(ascii_text); f.add(decimal_text);
        radio_group_lvl.add(plvl); radio_group_lvl.add(nlvl);
        //components alignment
        p.setSize(550, 200); p.setLocation(20, 125);
        hex_label.setBounds(20, 20, 165, 15); db_label.setBounds(395, 20, 150, 15); volt_label.setBounds(395, 70, 150, 15);
        ascii_label.setBounds(185,20,150,15); decimal_label.setBounds(285,20,150,15);credits_label.setBounds(20, 332, 300, 18); graph_label.setBounds(20, 105, 50, 15);
        hex_text.setBounds(20, 40, 155, 25); bin_text.setBounds(395, 40, 175, 25); volt_text.setBounds(395, 90, 175, 25);
        ascii_text.setBounds(185,40,90,25); decimal_text.setBounds(285,40,90,25);
        operate_bt.setBounds(20, 70, 75, 28); reset_bt.setBounds(100, 70, 75, 28); exit_bt.setBounds(495, 330, 75, 20);
        plvl.setBounds(180,75,85,20); nlvl.setBounds(275,75,90,20);
        parity.setBounds(280, 100, 90, 20); uart_format.setBounds(180,100,90,20);
        
        //functionality 
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        plvl.setToolTipText("Sets 2B1Q Algorithm Starting level to +1");
        nlvl.setToolTipText("Sets 2B1Q Algorithm Starting level to -1");
        uart_format.setToolTipText("<html>Adds 1 Start bit @ Logic Level 0<br>Adds 2 End Bits @ Logic Level 1<br> Adds 1 Bit According to Parity Selection<br> Becomes Asynchronous Tx Complaint</html>");
        bin_text.setEditable(false);  volt_text.setEditable(false);
        ascii_text.setEditable(false); decimal_text.setEditable(false);
        parity.setModel(new DefaultComboBoxModel<>(new String[] { "Even","Odd"}));
        parity.setEnabled(false);

        operate_bt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                    String hexa = hex_text.getText();
                    Pattern pattern = Pattern.compile("^[0-9a-fA-F]*$");
                    Matcher matcher = pattern.matcher(hexa);
                    boolean isTextHexa= matcher.find();

                    if(plvl.isSelected()==true){p.setPL(1);}
                    else if(nlvl.isSelected()==true){p.setPL(-1);}

                    if(plvl.isSelected()||nlvl.isSelected()){
                        if(isTextHexa==true){
                            if(uart_format.isSelected()){
                                String uart_parity = parity.getSelectedItem().toString();
                                p.uart_decrypt(hexa,uart_parity);
                                bin_text.setText(Arrays.toString(DecoderObj.duo_binary_array));
                                volt_text.setText(Arrays.toString(DecoderObj.voltage_db_array));
                                ascii_text.setText(convert_hex_to_ascii(hexa));
                                decimal_text.setText(convert_hex_to_decimal(hexa));
                                p.repaint();
                            }else if(!uart_format.isSelected()){
                         p.decrypt(hexa);
                         bin_text.setText(Arrays.toString(DecoderObj.duo_binary_array));
                         volt_text.setText(Arrays.toString(DecoderObj.voltage_db_array));
                         ascii_text.setText(convert_hex_to_ascii(hexa));
                         decimal_text.setText(convert_hex_to_decimal(hexa));
                         p.repaint();}
                        }else{
                            JOptionPane.showMessageDialog(p, "Please Enter a Valid Hexadecimal Number.");
                            reset_bt.doClick();
                            hex_text.requestFocus();} 
                    }else{JOptionPane.showMessageDialog(p,"Please Choose Positive or Negative Starting Level."); }
            }});
        reset_bt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hex_text.setText("");
                bin_text.setText("");
                volt_text.setText("");
                ascii_text.setText("");
                decimal_text.setText("");
                uart_format.setSelected(false);
                if(!uart_format.isSelected()){
                    parity.setEnabled(false);
                }
                DecoderObj.duo_binary_array=null;
                DecoderObj.voltage_db_array=null;
                p.repaint();
                hex_text.requestFocus();
            }
        });
        exit_bt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.exit(0);
            }
        });
        credits_label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String url_open ="https://github.com/9pepsi";
                try {java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));}
                catch (IOException e) {e.printStackTrace();}
        }});
        uart_format.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(uart_format.isSelected()){
                    parity.setEnabled(true);
                }else if(!uart_format.isSelected()){
                    parity.setEnabled(false);
                }
            }
        });
    }
}