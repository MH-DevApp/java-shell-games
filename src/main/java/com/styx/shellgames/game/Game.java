package com.styx.shellgames.game;

import com.styx.shellgames.generic.ExitGameException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public abstract class Game {
    protected abstract void init();

    protected abstract void reset();
    public abstract void startGame() throws ExitGameException;

    protected abstract boolean isWon();

    protected abstract boolean isLost();

    protected abstract void print();

}
