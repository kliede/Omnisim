package applicable.omnisim;

public class Dataset{
    private static int coutPrecC=1,coutPrecB=1,coutPrecA=1;

    public final Datacon config;
    public final int data[];

    public Dataset(final Datacon datacon){
        config=datacon;
        data=Hack.I32(config.dataUnit);
        coutPrecB=Hack.Max(coutPrecB,(""+config.totalBits).length());
        coutPrecA=Hack.Max(coutPrecA,(""+config.dataWrap ).length());
    }
    public void set32(final int i,final int n){  data[i&config.dataMask]=n;}
    public void set(final int i,final int n){
        final int index=((i*config.width)>>>5)&config.dataMask;
        final int shift= (i*config.width)&31;
        data[index]=(data[index]&~(config.mask<<shift))|((n&config.mask)<<shift);
    }
    public int get(final int i){
        final int index=((i*config.width)>>>5)&config.dataMask;
        final int shift= (i*config.width)&31;
        return (((data[index]>>>shift)<<config.clip)>>config.clip)&config.sign;
    }
    public void setClamp(final int i,final int n){  set(i,Hack.Clamp(config.max,       n,config.min));}
    public void addClamp(final int i,final int n){  set(i,Hack.Clamp(config.max,get(i)+n,config.min));}
    public void subClamp(final int i,final int n){  set(i,Hack.Clamp(config.max,get(i)-n,config.min));}
    public void add(final int i,final int n){  set(i,get(i)+n);}
    public void sub(final int i,final int n){  set(i,get(i)-n);}
    public void mul(final int i,final int n){  set(i,get(i)*n);}
    public void div(final int i,final int n){  set(i,get(i)/n);}
    public void and(final int i,final int n){  set(i,get(i)&n);}
    public void xor(final int i,final int n){  set(i,get(i)^n);}
    public void  or(final int i,final int n){  set(i,get(i)|n);}
    public void mod(final int i,final int n){  set(i,get(i)%n);}
    public void inv(final int i            ){  set(i,~get(i) );}
    public  int shf(final int i,final int n){  set(i,(n>0?get(i)<<n:get(i)>>-n));  return get(i);}
    public void tip(final int i,final int n){  final int a=get(i);  setClamp(i,(a>=0?Hack.Max(a+n,0):Hack.Min(-1,a+n)));}
    public int clamp(final int n){  return Hack.Clamp(config.max,n,config.min);}
    public boolean equals(final int i,final int n){  return get(i)==n;}
    public void and(final Dataset dataset){  if(dataset.config.end==config.end)for(int i=0;i!=config.end;++i)data[i]&=dataset.data[i];}
    public void xor(final Dataset dataset){  if(dataset.config.end==config.end)for(int i=0;i!=config.end;++i)data[i]^=dataset.data[i];}
    public void  or(final Dataset dataset){  if(dataset.config.end==config.end)for(int i=0;i!=config.end;++i)data[i]|=dataset.data[i];}
    public void andInv(final Dataset dataset){  if(dataset.config.end==config.end)for(int i=0;i!=config.end;++i)data[i]&=~dataset.data[i];}
    public void xorInv(final Dataset dataset){  if(dataset.config.end==config.end)for(int i=0;i!=config.end;++i)data[i]^=~dataset.data[i];}
    public void  orInv(final Dataset dataset){  if(dataset.config.end==config.end)for(int i=0;i!=config.end;++i)data[i]|=~dataset.data[i];}

    public void init(){  for(int i=0;i!=config.dataUnit;++i)data[i]=0;}

    private static int coutSpace=1;
    public void cout(final int color){
        String s="";
        for(int i=0;i!=config.end;++i)coutSpace=Hack.Max(coutSpace,(" "+get(i)).length());
        for(int i=config.end;--i!=-1;){
            s+=Hack.Space(get(i),coutSpace);
            if((i&0x7)==0){  Render.cout(s,color);  s="";}
        }
    }

    public String print(){
        return  Hack.Space(config.usingBits,6)+"/"+Hack.Space(config.totalBits,coutPrecB)+
                Hack.Space(config.end,6)+"/"+Hack.Space(config.dataWrap,coutPrecA)+
                Hack.Space(config.dataBits,4)
        ;
    }
}
