package com.example.system.myapplication;

    /**
     * Create boolean flags for other classes
     */
    public class BooleanC {
        private boolean bool;
        private int integer;

        public BooleanC(boolean bool){
            this.bool = bool;
            this.integer = 0;
        }

        public void setBool(boolean bool){
            this.bool = bool;
        }

        public boolean getBool(){
            return this.bool;
        }

        public void setInt(int integer){
            this.integer = integer;
        }

        public int getInt(){
            return this.integer;
        }
    }


