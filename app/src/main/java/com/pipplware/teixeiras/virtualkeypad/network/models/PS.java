package com.pipplware.teixeiras.virtualkeypad.network.models;

import java.util.ArrayList;

/**
 * Created by teixeiras on 16/03/15.
 */
public class PS {
    public class Process{
        ArrayList<String> cmdline;
        String memory;
        String name;
        String cpu;
    }

    ArrayList<Process> processes;
    ArrayList<String> cpu_percent;

    ArrayList<String> swap;

    ArrayList<String> memory;
    //ArrayList<ItemDTO> partitions;

    private int number_of_processors;

    public ArrayList<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(ArrayList<Process> processes) {
        this.processes = processes;
    }

    public ArrayList<String> getCpu_percent() {
        return cpu_percent;
    }

    public void setCpu_percent(ArrayList<String> cpu_percent) {
        this.cpu_percent = cpu_percent;
    }

    public ArrayList<String> getSwap() {
        return swap;
    }

    public void setSwap(ArrayList<String> swap) {
        this.swap = swap;
    }

    public ArrayList<String> getMemory() {
        return memory;
    }

    public void setMemory(ArrayList<String> memory) {
        this.memory = memory;
    }

    public int getNumber_of_processors() {
        return number_of_processors;
    }

    public void setNumber_of_processors(int number_of_processors) {
        this.number_of_processors = number_of_processors;
    }
}
