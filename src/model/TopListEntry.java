package model;

public class TopListEntry implements Comparable<TopListEntry>{


    private String name;
    private int score;

    TopListEntry(int score,String name){
        this.name=name;
        this.score=score;
    }
    @Override
    public int compareTo(TopListEntry o) {
        return -1*Integer.compare(this.score, o.score);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }



}