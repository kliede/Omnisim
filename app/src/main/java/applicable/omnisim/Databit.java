package applicable.omnisim;

public class Databit{
    public final int bits,mask,sign,index,shift,clip,clipL,clipR,max,min,bgnBit,endBit;
    public final int indexO,shiftO,clipO;
    public final boolean over;
    public Databit(final int bitOffset[],final int width,final boolean signed){
        bits=Hack.Clamp(32,width,1);
        mask=(int)((1L<<bits)-1L);
        bgnBit=bitOffset[0];  bitOffset[0]+=bits;
        endBit=bgnBit+bits;


        sign=(signed?~0:mask);
        max=(signed? (mask>>>1):mask);
        min=(signed?~(mask>>>1):   0);

        index=bgnBit>>>5;
        shift=bgnBit&31;
        shiftO=32-shift;
        clip=(32-bits)&31;
        clipO=64-(shift+bits);
        clipL=32-(shift+bits);
        clipR=32-bits;
        over=clipL<0;
        indexO=(over?index+1:index);
    }
    public int clamp(final int n){  return Hack.Clamp(max,n,min);}
    public int get(final int data[]){
        if(over)return (((data[indexO]<<clipO)|(data[index]>>>-clipL))>>clipR)&sign;
        return ((data[index]<<clipL)>>clipR)&sign;
    }/*possible problem here*/
    public void set(final int data[],final int n){
        data[index]&=~(mask<<shift);
        data[index]|=(mask&n)<<shift;
        if(over)data[indexO]&=~(mask>>>shiftO);
        if(over)data[indexO]|=(mask&n)>>>shiftO;
    }/*possible problem here*/

    public void setClamp(final int data[],final int n){  set(data,clamp(n));}
    public void addClamp(final int data[],final int n){  set(data,clamp(get(data)+n));}
    public void subClamp(final int data[],final int n){  set(data,clamp(get(data)-n));}
    public void add(final int data[],final int n){  set(data,get(data)+n);}
    public void sub(final int data[],final int n){  set(data,get(data)-n);}
    public void mul(final int data[],final int n){  set(data,get(data)*n);}
    public void div(final int data[],final int n){  set(data,get(data)/n);}
    public void and(final int data[],final int n){  set(data,get(data)&n);}
    public void xor(final int data[],final int n){  set(data,get(data)^n);}
    public void  or(final int data[],final int n){  set(data,get(data)|n);}
    public void mod(final int data[],final int n){  set(data,get(data)%n);}
    public void inv(final int data[]            ){  set(data,~get(data) );}
    public  int shf(final int data[],final int n){  set(data,(n>0?get(data)<<n:get(data)>>-n));  return get(data);}

    public void tip(final int data[],final int n){  final int a=get(data);  setClamp(data,(a>=0?Hack.Max(a+n,0):Hack.Min(-1,a+n)));}
}
