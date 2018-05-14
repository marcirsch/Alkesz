package model;

public class TopListEntry implements Comparable<TopListEntry>{


    private String name;
    private int score;

    /**
     * Constructor of top list entry
     * @param score score to initialize with
     * @param name name to initialize with
     */
    TopListEntry(int score,String name){
        this.name=name;
        this.score=score;
    }

    /**
     * Compares 2 entries
     * @param o
     * @return the value 0 if x == y; a value less than 0 if x < y; and a value greater than 0 if x > y
     */
    @Override
    public int compareTo(TopListEntry o) {
        return -1*Integer.compare(this.score, o.score);
    }

    /**
     * Getter method for Name of entry
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for Name of entry
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for store
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * Setter method for score
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }



}