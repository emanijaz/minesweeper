/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postlab_minesweeper;

import java.awt.EventQueue;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

/**
 *
 * @author emani
 */
 public class Timer extends JFrame implements Runnable {
 
        long startTime;
        //final static java.text.SimpleDateFormat timerFormat = new java.text.SimpleDateFormat("mm : ss :SSS");
        //final JButton startStopButton= new JButton("Start/stop");
        Board b;
        Thread updater;
        boolean isRunning = false;
        long ticks = 0;
        Runnable displayUpdater = new Runnable() {
 
            public void run() {
                displayCurrentTime(ticks);
                ticks++;
            }
        };
 
        public void stop() {
            long time =ticks;
            isRunning = false;
            try {
                updater.join();
            } catch (InterruptedException ie) {
            }
            displayCurrentTime(time);
            ticks= 0;
        }
 
        public void run() {
            try {
                while (isRunning) {
                    SwingUtilities.invokeAndWait(displayUpdater);
                    Thread.sleep(1000);
                }
            } catch (java.lang.reflect.InvocationTargetException ite) {
                ite.printStackTrace(System.err);
            } catch (InterruptedException ie) {
            }
        }
        private void displayCurrentTime(long time) {
 
            if (time >= 0 && time < 9) {
               b.get_tf_time().setText("00" +time);
            } else if (time > 9 &&time < 99) {
                b.get_tf_time().setText("0" +time);
            } else if (time > 99 && time < 999) {
                b.get_tf_time().setText("" +time);
            }
        }
 
        
 
        public void Start() {
            startTime = System.currentTimeMillis();
            isRunning = true;
            updater = new Thread(this);
            updater.start();
        }
        public void setBoardinTimer(Board  b)
        {
            this.b= b;
        }
    }