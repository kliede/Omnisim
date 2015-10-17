package applicable.omnisim;

public class Lobium{/*
    public  static final Datacon dataconI=new Datacon(32, 1,false);
    public  static final Datacon dataconO=new Datacon( 1,32,false);

    private final Cortex cortexI=new Cortex();
    private final Cortex cortexO=new Cortex();
    public  final Dataset datasetI=new Dataset(dataconI);
    public  final Dataset datasetO=new Dataset(dataconO);

    private final int P[]=Hack.I32(Cortex.bits);
    private final int O[]=Hack.I32(Cortex.bits);

    private int weightLast=0,xLast=0;

    public void input(){
        int iDataO=0;
        for(int x=0;x!=Cortex.bits;++x)O[x]=31&Cortex.getOData(P[x]=cortexI.input(x));
        for(int x=0;x!=Cortex.bits;++x)iDataO|=datasetI.get(O[x])<<x;
        datasetO.set(0,cortexO.input(iDataO));
    }
    public void reinforce(final int weight){
        cortexI.reinforce(P[xLast],weight-weightLast);
        cortexI.reinforce(P[xLast=Cortex.random(Cortex.bitsBit)],weight);
        cortexO.reinforce(datasetO.get(0),weight);
    }





    private static int coutLength=1;
    public static int debugInput=0,debugPred=0,debugWeight=0;
    public static final Lobium Static=new Lobium();
    public static void debug(){
        //for(int t=0;t!=bits;++t){
            //debugInput+=(random(3)*random(3)*random(3)*random(3))>>>6;
            //if(Clock.Cycled){
                //if(debugWeight==1)++debugInput;
                debugInput=Cortex.random(4);//((Cortex.random(4)*Cortex.random(4))>>>4)+Cortex.random(2);
                Static.datasetI.set32(0,debugInput);
                Static.input();
                int i=debugInput;
                int o=Cortex.getOData(Static.datasetO.get(0));
                debugPred=Hack.Sqrt(i,Cortex.bits);
                debugWeight=0-Hack.OneBits(debugPred^o);  if(debugWeight==0)debugWeight=1;
                Static.reinforce(debugWeight);
            //}
        //}
        String s="";
        for(int x=0;x!=Cortex.bits;++x)coutLength=Hack.Max(coutLength,(" "+Static.O[x]).length());
        for(int x=0;x!=Cortex.bits;++x)s=Hack.Space(Static.O[x],coutLength)+s;
        Render.cout(s,0xFFFFFF00);
        Static.cortexI.cout();
    }*/
}
