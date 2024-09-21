import java.util.ArrayList;
import java.util.Collections;

public class State implements Comparable{

    private ArrayList<Integer> startSide;
    private ArrayList<Integer> endSide;
    private boolean torch; //start side
    private int score;
    private State father;
    private int totalTime;

    public State(){
        this.startSide = new ArrayList<Integer>();
        this.endSide = new ArrayList<Integer>();
        this.torch = true;
        this.score = 0;
        this.totalTime = 0;
    }

    public State(ArrayList<Integer> startSide, ArrayList<Integer> endSide, int score, int totalTime){
        this.startSide = new ArrayList<Integer>();
        this.endSide = new ArrayList<Integer>();
        this.torch = true;
        this.totalTime = 0;
        this.score = 0;
        for(Integer temp: startSide) this.startSide.add(0+temp);
        for(Integer temp: endSide) this.endSide.add(0+temp);
        this.totalTime = totalTime;
        this.score = score;
    }

    public ArrayList<Integer> getStartSide() {
        return startSide;
    }

    public void setStartSide(ArrayList<Integer> startSide) {
        this.startSide = startSide;
    }

    public ArrayList<Integer> getEndSide() {
        return endSide;
    }

    public void setEndSide(ArrayList<Integer> endSide) {
        this.endSide = endSide;
    }

    public boolean isTorch() {
        return torch;
    }

    public void setTorch(boolean torch) {
        this.torch = torch;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public State getFather() {
        return father;
    }

    public void setFather(State father) {
        this.father = father;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public ArrayList<State> getChildren(){
        ArrayList<State> children = new ArrayList<State>();
        //if torch is true then is moving from start to end 2 persons
        if (this.isTorch()) {
            this.moveStartToEnd(children);
        }else {
            this.moveEndToStart(children);
        }
        return children;
    }

    /**
     * cross 2 persons on end side
     * @param children
     */
    public void moveStartToEnd(ArrayList<State> children){
        if (startSide.isEmpty())return;
        for(int i = 0; i < this.startSide.size(); i++){
            for(int j = i+1; j < this.startSide.size(); j++){
                State temp = new State(startSide,endSide,score,totalTime);
                temp.setFather(this);

                temp.endSide.add(temp.startSide.get(i));
                temp.endSide.add(temp.startSide.get(j));

                int speedi = temp.startSide.get(i);
                int speedj = temp.startSide.get(j);
                temp.totalTime += speedi > speedj ? speedi : speedj;

                temp.startSide.remove(j);
                temp.startSide.remove(i);
                Collections.sort(this.startSide);
                Collections.sort(this.endSide);
                temp.setTorch(!torch);
                temp.heuristic();
                children.add(temp);
            }
        }
    }

    /**
     * cross 1 person on start side
     * @param children
     */
    public void moveEndToStart(ArrayList<State> children) {
        if(startSide.isEmpty()) {
            return;
        }
        for (int i = 0; i < this.endSide.size(); i++){
            State temp = new State(startSide,endSide,score, totalTime);
            temp.setFather(this);
            temp.startSide.add(temp.endSide.get(i));
            temp.totalTime += temp.endSide.get(i);
            temp.endSide. remove(i);
            Collections.sort(this.startSide);
            Collections.sort(this.endSide);
            temp.setTorch(!torch);
            temp.heuristic();
            children.add(temp);
        }
    }

    public void heuristic(){
        if (this.torch) {
            this.score += Collections.min(this.startSide);
        } else {
            Collections.sort(this.startSide);
            Collections.sort(this.endSide);
            if(this.startSide.isEmpty()){
                return;
            }
            if (this.startSide.size() == 1) {
                this.score += Collections.min(this.endSide);
            } else {
                if (this.startSide.get(1) > this.endSide.get(0)) {
                    this.score += Collections.max(this.endSide);
                } else {
                    this.score += Collections.min(this.endSide);
                }
            }
        }
    }

    public boolean isTerminal(){
        return this.startSide.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        if (torch != state.torch) return false;
        if (score != state.score) return false;
        if (!startSide.equals(state.startSide)) return false;
        if (!endSide.equals(state.endSide)) return false;
        return father != null ? father.equals(state.father) : state.father == null;
    }

    @Override
    public int hashCode() {
        int result = startSide.hashCode();
        result = 31 * result + endSide.hashCode();
        result = 31 * result + (torch ? 1 : 0);
        result = 31 * result + score;
        return result;
    }

    @Override
    public int compareTo(Object o) {
        return Double.compare(this.score,( (State) o).score);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("State{");
        sb.append("startSide=").append(startSide);
        sb.append(", endSide=").append(endSide);
        sb.append(", torch=").append(torch);
        sb.append(", score=").append(score);
        sb.append(", totalTime=").append(totalTime);
        sb.append('}');
        return sb.toString();
    }
}