package com.tjufe.graduate.lbsserver.Model;

public class LBSExceptons {
    public static class NoSuchActivity extends Exception {
        public NoSuchActivity(int activityId) {
            super(String.format("activity of %s not exist", activityId));
        }
    }
}
