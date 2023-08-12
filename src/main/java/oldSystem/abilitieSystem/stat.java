package oldSystem.abilitieSystem;


public class stat{
    private int level;
    private double exp;
    
    public stat(){level=1;exp=0;}
    public stat(int level){this.level=level;exp=0;}
    public stat(int level, double exp){this.level=level;this.exp=exp;}

    public int getLevel(){return level;}
    public double getExp(){return exp;}
    public void setLevel(int level){this.level=level;}
    public void setExp(double exp){this.exp=exp;}
}

