package applicable.omnisim;

public class Masker{
    public final int bits,unit,mask,wask;
    public Masker(final int bitsA){
        bits=Hack.Clamp(32,bitsA,1);
        unit=(int)(1L<<bits);
        mask=unit-1;
        wask=~unit;
    }
}
