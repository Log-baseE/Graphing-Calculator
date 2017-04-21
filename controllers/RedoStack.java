package controllers;

import main.Main;

import java.util.Stack;

/**
 * Created by Nicky on 4/15/2017.
 */
public class RedoStack {
    private static Stack<Action> redoStack = new Stack<>();

    public static Action pop() {
        if (!redoStack.empty()) {
            System.out.println("Redo stack: " + redoStack);
            Action action = redoStack.pop();
            System.out.println("Redo popped: "+action);
            System.out.println("Redo stack after pop: " + redoStack);
            return action;
        } else return null;
    }

    public static void push(Action action) {
        System.out.println("Redo stack: " + redoStack);
        System.out.println("Redo pushed: "+action);
        redoStack.push(action);
        System.out.println("Redo stack after push: " + redoStack);
    }

    public static boolean empty() {
        return redoStack.empty();
    }

    public static void clear() {
        redoStack.clear();
    }

    public static void print() {
        System.out.println("redo: " + redoStack);
    }
}
