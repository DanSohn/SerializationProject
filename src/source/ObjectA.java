/*
1) A simple object with only primitives for instance variables. The user of your program must also be
able to set the values for these fields.
 */

package source;

public class ObjectA{
    public int A;
    public float B;
    private boolean C;
    private double D;

    public ObjectA(int A, float B, boolean C, double D){
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
    }

}