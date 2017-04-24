package controllers;

import main.Main;

import java.util.Stack;

/**
 * Created by Nicky on 4/15/2017.
 */
public class UndoStack {
    private static Stack<Action> undoStack = new Stack<>();

    public static Action pop() {
        if (!undoStack.empty()) {
            System.out.println("Undo stack: " + undoStack);
            Action action = undoStack.pop();
            System.out.println("Undo popped: "+action);
            System.out.println("Undo stack after pop: " + undoStack);
            return action;
        } else return null;
    }

    public static void push(Action action) {
        System.out.println("Undo stack: " + undoStack);
        System.out.println("Undo pushed: "+action);
        undoStack.push(action);
        System.out.println("Undo stack after push: " + undoStack);
        Main.setSave(false);
    }

    public static boolean empty() {
        return undoStack.empty();
    }

    public static void clear() {
        undoStack.clear();
    }

    public static void print() {
        System.out.println("undo: " + undoStack);
    }
}
