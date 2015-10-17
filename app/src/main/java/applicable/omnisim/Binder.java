package applicable.omnisim;

public class Binder{
    public final int EndA,EndB;
    public final boolean[] BoundA,BoundB;
    public final int[] BfromA,AfromB;

    public Binder(final int endA, final int endB){
        EndA= Hack.Clamp(1 << 16, endA, 1);
        EndB= Hack.Clamp(1 << 16, endB, 1);
        BoundA= Hack.ArrayFalse(EndA);
        BoundB= Hack.ArrayFalse(EndB);
        BfromA= Hack.I32(EndA);  for(int a=0;a!=EndA;++a) BfromA[a]=-1;
        AfromB= Hack.I32(EndB);  for(int b=0;b!=EndB;++b) AfromB[b]=-1;
    }

    public boolean Bind(final int a,final int b){
        if(BoundA[a]||BoundB[b])return false;
        BoundA[a]=true;  BfromA[a]=b;
        BoundB[b]=true;  AfromB[b]=a;
        return true;
    }
    public boolean ReleaseA(final int a){
        if(!BoundA[a])return false;
        final int b=BfromA[a];
        BoundA[a]=false;  BfromA[a]=-1;
        BoundB[b]=false;  AfromB[b]=-1;
        return true;
    }
    public boolean ReleaseB(final int b){
        if(!BoundB[b])return false;
        final int a=AfromB[b];
        BoundA[a]=false;  BfromA[a]=-1;
        BoundB[b]=false;  AfromB[b]=-1;
        return true;
    }
}
