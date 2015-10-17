package applicable.omnisim;

public class Datacon{
    public final int dataBits,dataUnit,dataMask,dataWrap,totalBits,usingBits,dataBitsBit;
    public final int widthBits,width,mask,end,max,min,clip,sign;

    private static final int log2(final int n){  int bit=0;  while((1L<<bit)<n)++bit;  return bit;}
    private static int bitsFromWrap(final int idWrap,final int bitWidth){
        final int bits=idWrap*Hack.Clamp(32,bitWidth,1);
        final int b=log2((bits>>>5)+(((bits&31)+31)>>>5));
        return Hack.Clamp(16,b,0);
    }

    public Datacon(final int indexes,final int bitWidth,final boolean signed){
        dataBits=log2(indexes);
        end=1<<dataBits;
        width=Hack.CeilPower2(Hack.Clamp(32,bitWidth,1));
        widthBits=log2(width);
        dataBitsBit=log2(dataBits);

        clip=32-width;
        mask=(int)((1L<<width)-1L);
        sign=(signed?~0         :mask);
        max =(signed? (mask>>>1):mask);
        min =(signed?~(mask>>>1):   0);

        dataUnit=1<<dataBits;
        dataMask =dataUnit-1;
        totalBits=(1<<dataBits)<<5;
        dataWrap =(totalBits/width)+((totalBits%width)!=0?1:0);

        usingBits=(end*width);
    }

}
