package applicable.omnisim;


public class Cycler{
    private int a=0,b=0,c=0;
    public final int end,mask;
    public Cycler(int bits){
        if(bits<1)bits=1;
        if(bits>8)bits=8;
        end=1<<bits;
        mask=end-1;
    }
    public int A(){  return mask&++a;}
    public int B(){  return mask&++b;}
    public int C(){  return mask&++c;}
}
