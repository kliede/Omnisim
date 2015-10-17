package applicable.omnisim;

public class Xorshift{
    public static Xorshift[] array(final int e){  final Xorshift[] arr=new Xorshift[e];  for(int i=0;i!=e;++i)arr[i]=new Xorshift();  return arr;}
    private final long s[]={2L,1L};

    public void init(int n){
        s[0]=((long)(n=Hack.hash(n)));
        s[0]=((long)(n=Hack.hash(n)))<<32;
        s[1]=((long)(n=Hack.hash(n)));
        s[1]=((long)(n=Hack.hash(n)))<<32;

    }
    public int get(final int bits){
        final long y=s[1];
        long x=s[0];
        s[0]=y;
        x^=x<<23;
        x^=x>>>17;
        x^=y^(y>>>26);
        s[1]=x;
        return (int)((x+y)>>>(64-bits));
    }
    public static final Xorshift Static=new Xorshift();
}
