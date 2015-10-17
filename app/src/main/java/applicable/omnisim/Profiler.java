package applicable.omnisim;

public class Profiler{
    public static final int ProfilerEnd=12;
    public static final Profiler[] profiler={
        new Profiler(32,0,"test0"),
        new Profiler(32,0,"test1"),
        new Profiler(32,0,"test2"),
        new Profiler(32,0,"test3"),
        new Profiler(32,0,"test4"),
        new Profiler(32,0,"test5"),
        new Profiler(32,0,"test6"),
        new Profiler(32,0,"test7"),
        new Profiler(32,0,"test8"),
        new Profiler(32,0,"test9"),
        new Profiler(32,0,"testA"),
        new Profiler(32,0,"testB")
    };

    private final int End;
    private final double[] I;
    private String Name;
    private int Index=0;
    private double Total=0.0D;
    public  double Max=0.0D,Min=0.0D,Average=0.0D;
    public static int index=-1;

    public Profiler(final int end,final int initial,final String name){
        End= Hack.Clamp(1024, end, 1);
        I= Hack.F64(End);
        for(int i=0;i!=End;++i)I[i]=initial;
        Average=initial;
        Total=Average*End;
        Name=name;
    }

    public double Add(final double a){
        Total-=I[Index];
        Total+=a;
        I[Index]=a;
        Average=Total/End;
        if(++Index==End)Index=0;
        return Average;
    }
    private long Milli=0L;
    public void Start(final String name){  Name=name;  Milli=Clock.Now();}
    public void Start(){  Milli=Clock.Now();}
    public void Stop(){  Add(Clock.Now()-Milli);}
    public String print(){  return Name+Hack.SpaceF64(Average,7,3);}

}
