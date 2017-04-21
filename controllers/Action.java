package controllers;

/**
 * Created by Nicky on 4/15/2017.
 */
public class Action {
    public int getIndex() {
        return index;
    }

    public Type getType() {
        return type;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public enum Type {
        ADD,
        CHANGE_TYPE,
        CHANGE_COLOR,
        CHANGE_TEXT,
        CHANGE_POINT,
        CHANGE_LOWER,
        CHANGE_UPPER,
        DELETE
    }

    private Type type;
    private int index;
    private Object oldValue;
    private Object newValue;

    public Action(Type type, int index, Object oldValue, Object newValue) {
        this.type = type;
        this.index = index;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public String toString() {
        String s = type + " " + index + " ";
        if (oldValue != null) s += oldValue.toString() + " ";
        else s += "null ";
        if (newValue != null) s += newValue.toString();
        else s += "null ";
        return s;
    }
}
