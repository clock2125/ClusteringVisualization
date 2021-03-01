package com.sptest.pojo;

public class kScores {
    double[] AICScores;
    double[] BICScores;

    public kScores(double[] AICScores, double[] BICScores) {
        this.AICScores = AICScores;
        this.BICScores = BICScores;
    }

    public double[] getAICScores() {
        return AICScores;
    }

    public void setAICScores(double[] AICScores) {
        this.AICScores = AICScores;
    }

    public double[] getBICScores() {
        return BICScores;
    }

    public void setBICScores(double[] BICScores) {
        this.BICScores = BICScores;
    }
}
