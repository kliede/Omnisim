package applicable.omnisim;


public class Clock{
    public static final int  Bits=10;
    public static final long Unit=1L<<Bits,Mask=Unit-1L,Wask=~Mask;
    public static final double SlicePerFrame=1.0D/32.0D,Scale=1.0D/(double)(1L<<Bits);

    public static final long Unit8=1L<<7,Mask8=Unit8-1L,Wask8=~Mask8;
    public static final long Unit4=1L<<8,Mask4=Unit4-1L,Wask4=~Mask4;
    public static final long Unit2=1L<<9,Mask2=Unit2-1L,Wask2=~Mask2;

    public static long MillisBgn=System.currentTimeMillis(),CycleAtMillis=Unit;
    public static long Millis=0L,Milli=0L,Cool=0L,Cycles=0L;
    public static long MillisLast=0L;
    public static long CycleID=0L;

    public static  int BitMask8=0,BitMask4=0,BitMask2=0,BitShift8=0;

    public static double Seconds=0.0D,Second=0.0D,Slice=0.0D;

    public static boolean Cycled=false;

    public static long Now(){  return System.currentTimeMillis()-MillisBgn;}
    public static int Cooldown(){
        Cool=Now();
        Cool=((Millis+32)&~0x1F)-Cool;
        return Cool<0?0:(int)Cool;
    }
    public static void Update(){
        MillisLast=Millis;
        Millis=Now();
        Cycles=Millis>>Bits;
        Milli=Millis&Mask;

        Cycled=Millis>=CycleAtMillis;
        CycleID=Millis>>>Bits;
        if(Cycled)CycleAtMillis+=Unit;

        BitShift8=(int)(Milli>>>7);
        BitMask8=1<<BitShift8;
        BitMask4=1<<(Milli>>>8);
        BitMask2=1<<(Milli>>>9);

        Slice=Scale*(Millis-MillisLast);
        Second=Scale*(Millis&Mask);
        Seconds=(Millis>>>Bits)+Second;
    }
}
