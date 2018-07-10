package com.umeitime.common.base;

/**
 * Created by hujunwei on 17/7/3.
 */

public class BaseEvent {

    public static class SmoothToTopEvent {
        int pos;
        public SmoothToTopEvent(int pos){
            this.pos = pos;
        }
        public int getPos() {
            return pos;
        }
    }
    public static class LoginOutEvent{

    }
    public static class LoginSuccess{

    }
    public static class TokenExpireEvent{

    }
    public static class NewChangeEvent{

    }

    public static class UpdateUserInfo {

    }
    public static class ModifyNameEvent {
        String name;

        public ModifyNameEvent(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
