/*
1) A simple object with only primitives for instance variables. The user of your program must also be
able to set the values for these fields.
 */

package src;

public class ObjectA{
    public int A;
    public float B;
    private boolean C;
    protected char D;
    private double E;

    public ObjectA(int A, float B, boolean C, char D, double E){
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.E = E;
    }

}