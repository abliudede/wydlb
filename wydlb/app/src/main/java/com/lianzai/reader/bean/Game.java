package com.lianzai.reader.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;    //先import Scanner语法来接受键盘输入

//游戏大类
public class Game {

    private int playerCount;//玩家数量
    private List<String> playerList = new ArrayList<>();//玩家数组
    private String reg;//规则描述字符串
    private int boundary;//边界值

    //获取边界值,边界值在30到50之间是个随机数
    private int getBoundary(){
        int num=new Random().nextInt(21);
        return num+30;
    }

    //获取骰子点数，这里不建新类，每次骰子点数只有一次性的作用，新类会浪费。直接一个方法获取当前丢的点数即可。
    private int getDice(){
        int num=new Random().nextInt(6);
        return num+1;
    }


    //玩家类
    private class Player{
        private String name;//名字
        private int  point;//点数和
        private boolean ob;//点数和是否超过边界值



        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }

        public boolean isOb() {
            return ob;
        }

        public void setOb(boolean ob) {
            this.ob = ob;
        }
    }

    /**
     * 输入学员名称及总科目,打印每门科目的成绩并算出平均分
     */
    public static void main(String[] args) {
        System.out.print("请输入:");
        Scanner input = new Scanner(System.in);
        String name = input.next();

        System.out.print("请输入:");
        Scanner input1 = new Scanner(System.in);
        int sc = input1.nextInt();


        System.out.println("显示"+sc);

    }

}