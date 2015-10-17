package applicable.omnisim;

import java.text.DecimalFormat;

public class Hack{
    public static double Clamp(final double max,final double num,final double min){  return num<min?min:num>max?max:num;}
    public static float  Clamp(final float  max,final float  num,final float  min){  return num<min?min:num>max?max:num;}
    public static int    Clamp(final int    max,final int    num,final int    min){  return num<min?min:num>max?max:num;}
    public static long   Clamp(final long   max,final long   num,final long   min){  return num<min?min:num>max?max:num;}
    public static long   Round64(final double a){  return a<0.0D?-(long)(0.5D-a):(long)(0.5D+a);}
    public static long   Floor64(final double a){  return a<0.0D?Round64(a-0.5D):(long)a;}
    public static int    Round32(final double a){  return a<0.0D?-(int)(0.5D-a):(int)(0.5D+a);}
    public static int    Floor32(final double a){  return a<0.0D?Round32(a-0.5D):(int)a;}
    public static double RoundF64(final double a,final double b){  return a-(b*Hack.Floor32(a/b));}
    public static int    ABS(final int a){  return ((a>>31)+a)^(a>>31);}
    public static long   ABS(final long a){  return ((a>>63)+a)^(a>>63);}
    public static float  ABS(final float  a){  return Math.abs(a);}
    public static double ABS(final double a){  return Math.abs(a);}
    public static long Max(final long b,final long a){  return b>a?b:a;}
    public static long Max(final long c,final long b,final long a){  return Max(Max(c,b),Max(b,a));}
    public static long Min(final long b,final long a){  return b<a?b:a;}
    public static long Min(final long c,final long b,final long a){  return Min(Min(c,b),Min(b,a));}
    public static int Max(final int b,final int a){  return b>a?b:a;}
    public static int Max(final int c,final int b,final int a){  return Max(Max(c,b),Max(b,a));}
    public static int Min(final int b,final int a){  return b<a?b:a;}
    public static int Min(final int c,final int b,final int a){  return Min(Min(c,b),Min(b,a));}
    public static  int Shift(final  int i,final int b){  return i<0 ?-(b>0?(-i)<<b:(-i)>>>-b):(b>0?i<<b:i>>>-b);}
    public static long Shift(final long i,final int b){  return i<0L?-(b>0?(-i)<<b:(-i)>>>-b):(b>0?i<<b:i>>>-b);}
    public static  int UShift(final  int i,final int b){  return b>0?i<<b:i>>>-b;}
    public static long UShift(final long i,final int b){  return b>0?i<<b:i>>>-b;}
	public static double Bezier4(final double a,final double b,final double c,final double d,final double t1){
		final double t0=1.0D-t1,t02=t0*t0,t12=t1*t1;
		final double B=(-(5.0D*a)+(18.0D*b)-( 9.0D*c)+(2.0D*d))*0.5D;
		final double C=( (2.0D*a)-( 9.0D*b)+(18.0D*c)-(5.0D*d))*0.5D;
		return (t02*t0*a)+(t02*B*t1)+(t0*C*t12)+(d*t1*t12);
	}
    public static double Atan2(final double y,final double x){  return RoundF64(Math.atan2(y,x),PI2);}
    public static double Sqrt(final double a){  return a==0.0D?0.0D:Math.sqrt(a);}
    public static float  Sqrt(final float  a){  return a==0.0f?0.0f:(float)Math.sqrt((double)a);}
    public static float  SqrtSign(final float  a){  return a<0.0f?-Sqrt(-a):Sqrt(a);}
    public static double Sqrt(final long   a){  return a==  0L?0.0D:Math.sqrt(a);}
    public static double Sqrt(final int    a){  return a==  0 ?0.0D:Math.sqrt(a);}
    public static double Pow(final double a,final double b){  return Math.pow(a,b);}
    public static double CubeRoot(final double a){  return Math.pow(a,1.0D/3.0D);}
    private static final String[] IndexToHex={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
    public static String Hex(final long a,int b){
        String sA="";
        if((b&0x3)!=0)b=(b&~0x3)+4;
        while(b!=0)sA+=IndexToHex[(int)(0xF&(a>>>(b-=4)))];
        return sA;
    }
    public static String Binary(final long a,final int bits){
        String sA="";
        int b=Clamp(64,bits,1);
        while(b--!=0)sA+=IndexToHex[(int)(0x1&(a>>>b))];
        return sA;
    }
    public static String Binary(final long a,final int bits,final int spaceBit){
        String sA="";
        int b=Clamp(64,bits,1),c=0;
        while(b--!=0){
            sA+=IndexToHex[(int)(0x1&(a>>>b))];
            if(++c==spaceBit){  c=0;  sA+=" ";}
        }
        return sA;
    }
    public static String Oct(final long a,int b){  String sA="";  b/=3;  while(b!=0)sA+=0x7&(a>>>(3*--b));  return sA;}
    public static final DecimalFormat DecFormatB=new DecimalFormat("#0.000");
    public static String Space(final long a,final int b,final String spacer){
        String s=""+a;
        for(int i=s.length();i<b;++i)s=spacer+s;
        return s;
    }
    public static String Space(final long a,final int b){  return Space(a,b," ");}
    public static String SpaceF64(final double f){  return (f<0.0D?"":" ")+DecFormatB.format(f);}
    public static String SpaceF64(final double f,final int b){
        final long a=Round64(f*1000.0D);
        return Space(a/1000,b)+"."+Space(ABS(a)%1000,3,"0");
    }
    public static String SpaceF64(final double f,final int lengthB,final int lengthA){
        final long a=Round64(f*1000.0D);
        String s=Space(a/1000,lengthA)+"."+Space(ABS(a)%1000,3,"0");
        while(s.length()<lengthB)s=" "+s;
        return s;
    }
    public static   byte[] I08(final int e){  final   byte[] arr=new   byte[e];  for(int i=0;i!=e;++i)arr[i]=0;  return arr;}
    public static  short[] I16(final int e){  final  short[] arr=new  short[e];  for(int i=0;i!=e;++i)arr[i]=0;  return arr;}
    public static    int[] I32(final int e){  final    int[] arr=new    int[e];  for(int i=0;i!=e;++i)arr[i]=0;  return arr;}
    public static   long[] I64(final int e){  final   long[] arr=new   long[e];  for(int i=0;i!=e;++i)arr[i]=0;  return arr;}
    public static  float[] F32(final int e){  final  float[] arr=new  float[e];  for(int i=0;i!=e;++i)arr[i]=0;  return arr;}
    public static double[] F64(final int e){  final double[] arr=new double[e];  for(int i=0;i!=e;++i)arr[i]=0;  return arr;}
    public static boolean[] ArrayFalse(final int e){  final boolean[] arr=new boolean[e];  for(int i=0;i!=e;++i)arr[i]=false;  return arr;}
    public static boolean[] ArrayTrue (final int e){  final boolean[] arr=new boolean[e];  for(int i=0;i!=e;++i)arr[i]= true;  return arr;}
    public static  String[] ArrayString(final int e){  final String[] arr=new String[e];  for(int i=0;i!=e;++i)arr[i]="";  return arr;}
    public static int[][] I32(final int x,final int y){
        final int[][] arr=new int[x][y];
        for(int a=0;a!=x;++a)for(int b=0;b!=y;++b)arr[a][b]=0;
        return arr;
    }
    public static boolean[][] Bool(final int x,final int y,final boolean initial){
        final boolean[][] arr=new boolean[x][y];
        for(int a=0;a!=x;++a)for(int b=0;b!=y;++b)arr[a][b]=initial;
        return arr;
    }
    public static final double F64Minimum=1.0D/(double)(1L<<60);
    public static final double PI=Math.PI,PI2=PI*2.0D,PIInv=1.0D/PI,PI2Inv=1.0D/PI2,PI05=PI*0.5D;
    public static final double SqrtOf3=Sqrt(3.0D),SqrtOf2=Sqrt(2.0D),SqrtOf2Inv=1.0D/SqrtOf2;

    public static final int StartBitStar=120,StopBitStar=87,SumBitStar=StartBitStar-StopBitStar;
    public static final int GalaxyEnd=(1<<15),StarEnd=27*SumBitStar,GalaxyStarEnd=GalaxyEnd+StarEnd;
    public static final int BitGBgn=100,BitYBgn=83,BitSBgn=58,BitMBgn=30,BitNBgn=0;/*28 17 25 28 30*/
    public static final int
            BitGIndex=BitGBgn>>>6,
            BitYIndex=BitYBgn>>>6,
            BitSIndex=BitSBgn>>>6,
            BitMIndex=BitMBgn>>>6,
            BitNIndex=BitNBgn>>>6;
    public static final int
            BitGShift=BitGBgn-(BitGIndex<<6),
            BitYShift=BitYBgn-(BitYIndex<<6),
            BitSShift=BitSBgn-(BitSIndex<<6),
            BitMShift=BitMBgn-(BitMIndex<<6),
            BitNShift=BitNBgn-(BitGIndex<<6);
    public static final int
            BitN=BitMBgn-BitNBgn,
            BitM=BitSBgn-BitMBgn,
            BitS=BitYBgn-BitSBgn,
            BitY=BitGBgn-BitYBgn,
            BitG=    128-BitGBgn;
    public static final long BitYMask=(1L<<(128-BitYBgn))-1L,BitGMask=(1L<<(128-BitGBgn))-1L;
    public static final int SolarRadiusBits=14+BitSBgn;
    public static final int PlanetRadiusBitsMax=BitMBgn+27;
    public static final int PlanetRadiusBitsMin=BitMBgn+21;

    public static long PowSum(final long base,final int power){
        long a=1L,b=1L;
        for(int i=0;i!=power;++i)b+=(a*=base);
        return b;
    }
    public static long Hash64(final long hash){
        long h=0x14650FB0739D0383L;
        h=(( hash     &0xFF)^h)*0x00000100000001B3L;
        h=(((hash>> 8)&0xFF)^h)*0x00000100000001B3L;
        h=(((hash>>16)&0xFF)^h)*0x00000100000001B3L;
        h=(((hash>>24)&0xFF)^h)*0x00000100000001B3L;
        h=(((hash>>32)&0xFF)^h)*0x00000100000001B3L;
        h=(((hash>>40)&0xFF)^h)*0x00000100000001B3L;
        h=(((hash>>48)&0xFF)^h)*0x00000100000001B3L;
        h=(((hash>>56)&0xFF)^h)*0x00000100000001B3L;
        return h;
    }
    public static int Hash32(final int hash){
        int h=0x811C9DC5;
        h=(( hash      &0xFF)^h)*0x01000193;
        h=(((hash>>> 8)&0xFF)^h)*0x01000193;
        h=(((hash>>>16)&0xFF)^h)*0x01000193;
        h=(((hash>>>24)&0xFF)^h)*0x01000193;
        return h;
    }
    public static int Prng(final int n){  return (n*997)+474077;}
    public static int hash(final int s){
        int h=0x811C9DC5;
        h=(( s      &0xFF)^h)*0x01000193;
        h=(((s>>> 8)&0xFF)^h)*0x01000193;
        h=(((s>>>16)&0xFF)^h)*0x01000193;
        h=(((s>>>24)&0xFF)^h)*0x01000193;
        return h;
    }
    public static int prng    (final int n[]){  return hash(n[0]++)>>>(32-n[1]);}
    public static int prngPrev(final int n[]){  return hash(--n[0])>>>(32-n[1]);}
/*uint32_t GCD(uint32_t a, uint32_t b)

{
        uint32_t x, y, u, v, m, n, q, r, b0;

        x = 0; y = 1; u = 1; v = 0; b0 = b;

        while (a != 0) {
                q = b / a;      r = b % a;

                m = x - u * q;  n = y - v * q;

                while (m > b0)	m += b0;
		while (n > b0)	n += b0;

                b = a;  a = r;  x = u;

                y = v;  u = m;  v = n;

        }
        return x % b0;
}*/

    public static int Murmer32(int hash){
        for(int i=0,k;i!=8;++i){
            k=0xcc9e2d51*(hash>>>(i<<2));
            hash^=0x1b873593*((k<<15)|(k>>>(32-15)));
            hash=((hash<<13)|(hash>>>(32-13)))*5+0xe6546b64;
        }
        hash^=32;
        hash^=(hash>>>16);
        hash*=0x85ebca6b;
        hash^=(hash>>>13);
        hash*=0xc2b2ae35;
        hash^=(hash>>>16);
        return hash;
    }

    /*public static int Hash32(int i){
        long u=i&0x00000000FFFFFFFFL;
		u=(u^61)^(u>>>16);
		u+=(u<<3);
		u^=(u>>>4);
		u=(u*0x27D4EB2D)&0x00000000FFFFFFFFL;
		u^=u>>>15;
		return (int)u;
    }*/





    public static double SqrtInv(double x){
        final double xhalf=0.5D*x;
        long i=Double.doubleToRawLongBits(x);
        i=0x5FE6EB50C7B537AAL-(i>>>1);
        x=Double.longBitsToDouble(i);
        x=x*(1.5D-xhalf*x*x);
        x=x*(1.5D-xhalf*x*x);
        return x;
    }
    private static final double FRAC_BIAS;
    private static final double[] ASIN_TAB,COS_TAB;
    static{
        final int FRAC_EXP=8;
        final int LUT_SIZE=(1<<FRAC_EXP)+1;
        FRAC_BIAS=Double.longBitsToDouble((0x433L-FRAC_EXP)<<52);
        ASIN_TAB= Hack.F64(LUT_SIZE);
        COS_TAB= Hack.F64(LUT_SIZE);
        for(int i=0;i<LUT_SIZE;++i){
            final double asinv=Math.asin((double)i/(double)(1<<8));
            COS_TAB[i]=Math.cos(asinv);
            ASIN_TAB[i]=asinv;
        }
    }
    public static long Cast(final double f){  return Double.doubleToRawLongBits(f);}
    public static double Cast(final long i){  return Double.longBitsToDouble(i);}
    public static int Cast(final float f){  return Float.floatToRawIntBits(f);}
    public static float Cast(final int i){  return Float.intBitsToFloat(i);}
    public static double Atan2Fast(double y,double x){
        final double d2=x*x+y*y;
        if(Double.isNaN(d2)||(Double.doubleToRawLongBits(d2)<0x0010000000000000L))return Double.NaN;

        // Normalise such that 0.0 <= y <= x
        final boolean negY=y<0.0D;
        if(negY)y=-y;
        final boolean negX=x<0.0D;
        if(negX)x=-x;
        final boolean steep=y>x;
        if(steep){  final double t=x;  x=y;  y=t;}

        // Scale to unit circle (0.0 <= y <= x <= 1.0)
        final double rinv=SqrtInv(d2); // rinv ≅ 1.0 / hypot(x, y)
        x*=rinv; // x ≅ cos θ
        y*=rinv; // y ≅ sin θ, hence θ ≅ asin y

        // Hack: we want: ind = floor(y * 256)
        // We deliberately force truncation by adding floating-point numbers whose
        // exponents differ greatly.  The FPU will right-shift y to match exponents,
        // dropping all but the first 9 significant bits, which become the 9 LSBs
        // of the resulting mantissa.
        // Inspired by a similar piece of C code at
        // http://www.shellandslate.com/computermath101.html
        final double yp=FRAC_BIAS+y;
        final int ind=(int)Double.doubleToRawLongBits(yp);

        // Find φ (a first approximation of θ) from the LUT
        final double sin=ASIN_TAB[ind];
        final double cos=COS_TAB[ind]; // cos(φ)

        // sin(φ) == ind / 256.0
        // Note that sφ is truncated, hence not identical to y.
        final double sφ=yp-FRAC_BIAS;
        final double sd=y*cos-x*sφ; // sin(θ-φ) ≡ sinθ cosφ - cosθ sinφ

        // asin(sd) ≅ sd + ⅙sd³ (from first 2 terms of Maclaurin series)
        final double d=(6.0D+sd*sd)*sd*(1.0D/6.0D);
        double angle=sin+d;

        // Translate back to correct octant
        if(steep)angle=PI05-angle;
        if(negX)angle=PI-angle;
        if(negY)angle=-angle;

        return RoundF64(angle,PI2);
    }
    public static float cos(final float angle){  return (float)Math.cos(angle);}
    public static float sin(final float angle){  return (float)Math.sin(angle);}
/*
    public static double Cos(final double a1){
        final double a2=a1*a1;
        final double a4=a2*a2;
        final double a6=a2*a4;
        return 1.0D-(a2*0.5D)+(a4/24.0D)-(a6/720.0D);  //1.0D-(a2/2.0D)+(a4/24.0D)-(a6/720.0D);
    }

    public static double Tan(final double a1){
        final double a2=a1*a1;
        final double a3=a2*a1;
        final double a5=a2*a3;
        final double a7=a2*a5;
        return a1+(a3/3.0D)+(a5/7.5D)+((a7*17.0D)/315.0D);  //a1+(a3/3.0D)+((a5*2.0D)/15.0D)+((a7*17.0D)/315.0D);
    }
*/
    private static double Atan2Faster(final double y,final double x){
        final double coeff_1= Hack.PI/4.0D;
        final double coeff_2 = 3.0D*coeff_1;
        final double abs_y = Hack.ABS(y)+1e-10;
        double angle;
        if(x>=0){
          final double r = (x - abs_y) / (x + abs_y);
          angle = coeff_1 - coeff_1 * r;
        }else{
          final double r = (x + abs_y) / (abs_y - x);
          angle = coeff_2 - coeff_1 * r;
        }
        return y<0.0D?-angle:angle;
    }

	private static final  int[] Reverse32D=I32(256),Reverse32C=I32(256),Reverse32B=I32(256),Reverse32A=I32(256);
    private static final long[] Reverse64H=I64(256),Reverse64G=I64(256),Reverse64F=I64(256),Reverse64E=I64(256);
	private static final long[] Reverse64D=I64(256),Reverse64C=I64(256),Reverse64B=I64(256),Reverse64A=I64(256);

	static{
        for(int i=0;i!=256;++i){
            for(int a=0x01,b=0x80;b!=0x00;a<<=1,b>>=1)if(0!=(i&a))Reverse32A[i]|=b;
            Reverse32D[i]=Reverse32A[i]<<24;  Reverse32C[i]=Reverse32A[i]<<16;  Reverse32B[i]=Reverse32A[i]<< 8;  Reverse64A[i]=Reverse32A[i];
            Reverse64H[i]=Reverse64A[i]<<56;  Reverse64G[i]=Reverse64A[i]<<48;  Reverse64F[i]=Reverse64A[i]<<40;  Reverse64E[i]=Reverse64A[i]<<32;
            Reverse64D[i]=Reverse64A[i]<<24;  Reverse64C[i]=Reverse64A[i]<<16;  Reverse64B[i]=Reverse64A[i]<< 8;
        }
    }
	public static  int reverseBits(final int a){  return Reverse32D[a&0xFF]|Reverse32C[(a>>>8)&0xFF]|Reverse32B[(a>>>16)&0xFF]|Reverse32A[a>>>24];}
    public static  int reverseBits(final int n,final int b){  return reverseBits(n)>>>(32-b);}

	/*public static long BitReverse(final long a){
        return  Reverse64H[(int)(a>>> 0)&0xFF]|Reverse64G[(int)(a>>> 8)&0xFF]|
                Reverse64F[(int)(a>>>16)&0xFF]|Reverse64E[(int)(a>>>24)&0xFF]|
                Reverse64D[(int)(a>>>32)&0xFF]|Reverse64C[(int)(a>>>40)&0xFF]|
                Reverse64B[(int)(a>>>48)&0xFF]|Reverse64A[(int)(a>>>56)&0xFF];
    }*/
	public static long BitReverse(long a){
        a=(((a&0xAAAAAAAAAAAAAAAAL)>>> 1)|((a&0x5555555555555555L)<< 1));
        a=(((a&0xCCCCCCCCCCCCCCCCL)>>> 2)|((a&0x3333333333333333L)<< 2));
        a=(((a&0xF0F0F0F0F0F0F0F0L)>>> 4)|((a&0x0F0F0F0F0F0F0F0FL)<< 4));
        a=(((a&0xFF00FF00FF00FF00L)>>> 8)|((a&0x00FF00FF00FF00FFL)<< 8));
        a=(((a&0xFFFF0000FFFF0000L)>>>16)|((a&0x0000FFFF0000FFFFL)<<16));
        return ((a>>>32)|(a<<32));
    }
	public static int DebugFunctionA(long a){
        a|=(a>>>1);  a|=(a>>>2);  a|=(a>>>4);  a|=(a>>>8);  a|=(a>>>16);  a|=(a>>>32);
        a-= (a>>> 1)&0x5555555555555555L;
        a =((a>>> 2)&0x3333333333333333L)+(a&0x3333333333333333L);
        a =((a>>> 4)+a)&0x0F0F0F0F0F0F0F0FL;
        return (int)((a*0x0101010101010101L)>>>56);
    }
	public static int DebugFunctionB(final long a){  return (a>>>32)!=0L?32+BitCount((int)(a>>>32)):BitCount((int)a);}

    private static long DebugN=-8,DebugSeed=0;
    private static int DebugError=0;
    private static final int DebugEnd=16,DebugLimit=1<<14;
    private static final long[] DebugI=I64(DebugEnd),DebugA=I64(DebugEnd),DebugB=I64(DebugEnd);
    public static void Debug(){
/*
        final int end=1<<10;
        for(int i=0;i!=end;++i){
            if(DebugFunctionA(DebugN)!=DebugFunctionB(DebugN))ErrsA.Add();
            if(DebugError!=DebugEnd){
                DebugI[DebugError]=DebugN;
                DebugA[DebugError]=DebugFunctionA(DebugN);
                DebugB[DebugError]=DebugFunctionB(DebugN);
                ++DebugError;
            }
            ++DebugN;
        }
        GFX.Cout("errors: "+ErrsA.Error+"/"+DebugN,0xFF00FFFF);
        int color;
        for(int i=0;i!=DebugError;++i){
            color=(DebugA[i]==DebugB[i]?0xFF00FF00:0xFF00FFFF);
            GFX.Cout(Hack.Space(DebugI[i], 8)+ Hack.Space(DebugA[i], 4)+ Hack.Space(DebugB[i], 4),color);
        }
*/
/*
        long milliA=0L,milliB=0L;
        milliA=Clock.Now();  for(int a=0;a!=DebugLimit;++a)SqrtA(DebugSeed=Hash64(DebugSeed),30);  milliA=Clock.Now()-milliA;
        milliB=Clock.Now();  for(int a=0;a!=DebugLimit;++a)SqrtB(DebugSeed=Hash64(DebugSeed),30);  milliB=Clock.Now()-milliB;

        GFX.Cout("milliA: "+milliA+"ms");
        GFX.Cout("milliB: "+milliB+"ms");
*/
        //GFX.Cout(Hex(BitReverse(0x8000000000000000L),64),0xFF00FF00);
        //GFX.Cout(Hex(BitReverse(0x8000000000000000L),64),0xFF00FF00);
        //GFX.Cout(Hex(BitReverse(0x8010300000000000L),64),0xFF00FF00);
        //for(int a=0,b=0,e=8;b!=e;++a)if(IsPower4(a)){  GFX.Cout(""+a,0xFF00FF00);  ++b;}
/*
        for(int i=0;i!=48;i+=2){
            final long unit=1L<<i;
            final long sqrt=Sqrt(unit,i);
            if(unit!=sqrt)GFX.Cout(i+" "+SpaceF64((double)sqrt/(double)unit)+" "+Hex(sqrt,64),0xFFFF00FF);
        }
*/
    }
	public static int BitCount(int a){
        int b=0;  a=Reverse32D[a&0xFF]|Reverse32C[(a>>>8)&0xFF]|Reverse32B[(a>>>16)&0xFF]|Reverse32A[a>>>24];  a&=-a;
        if(0!=(a&0x0000FFFF))b|=16;
        if(0!=(a&0x00FF00FF))b|= 8;
        if(0!=(a&0x0F0F0F0F))b|= 4;
        if(0!=(a&0x33333333))b|= 2;
        if(0!=(a&0x55555555))b|= 1;
        if(0!=a)++b;
        return b;
	}
	public static int BitCount(long a){
        a|=(a>>>1);  a|=(a>>>2);  a|=(a>>>4);  a|=(a>>>8);  a|=(a>>>16);  a|=(a>>>32);
        a-= (a>>> 1)&0x5555555555555555L;
        a =((a>>> 2)&0x3333333333333333L)+(a&0x3333333333333333L);
        a =((a>>> 4)+a)&0x0F0F0F0F0F0F0F0FL;
        return (int)((a*0x0101010101010101L)>>>56);//a+=a>>>8;  a+=a>>>16;  a+=a>>>32;
	}
    private static final int[] OneBitsArr=I32(256);
    static{  for(int a=0;a!=256;++a)for(int b=0x001;b!=0x100;b<<=1)OneBitsArr[a]+=((a&b)!=0)?1:0;}
    public static int OneBits(int a){  return OneBitsArr[(a>>>24)&0xFF]+OneBitsArr[(a>>>16)&0xFF]+OneBitsArr[(a>>>8)&0xFF]+OneBitsArr[a&0xFF];}
    public static int OneBits(long a){  return OneBits((int)(a>>>32))+OneBits((int)a);}
	public static long CeilPower2(long a){  --a;  a|=a>>>1;  a|=a>>>2;  a|=a>>>4;  a|=a>>>8;  a|=a>>>16;  a|=a>>>32;  return ++a;}
	public static int  CeilPower2( int a){  --a;  a|=a>>>1;  a|=a>>>2;  a|=a>>>4;  a|=a>>>8;  a|=a>>>16;  return ++a;}
	public static long NextPower4(long a){  --a;  a|=a>>>1;  a|=a>>>2;  a|=a>>>4;  a|=a>>>8;  a|=a>>>16;  a|=a>>>32;  return IsPower4(++a)?a:a>>>1;}
	public static  int NextPower4( int a){  --a;  a|=a>>>1;  a|=a>>>2;  a|=a>>>4;  a|=a>>>8;  a|=a>>>16; return IsPower4(++a)?a:a>>>1;}
    public static boolean IsPower4(final long a){  return ((a&-a)&0x5555555555555554L)==a;}
    public static boolean IsPower4(final  int a){  return ((a&-a)&0x55555554)==a;}
    public static long Sqrt(long a,int bit){
        if(a<1L||(a<<=bit&1)<0L)return 0L;  bit&=~1;
        long c,b=1L<<62;  while(b>a)b>>>=2;  c=b;  a-=b;
        //long c,b=a-1L;  b|=b>>>1;  b|=b>>>2;  b|=b>>>4;  b|=b>>>8;  b|=b>>>16;  a|=a>>>32;  c=IsPower4(++b)?(b>>>=2):(b>>>=1);  a-=b;
        while((b>>>=2)!=0L)if(a<c+b){  c>>>=1;}else{  a-=c+b;  c=(c>>>1)+b;}  c<<=bit;  a<<=bit;  b=1L<<bit;
        while((b>>>=2)!=0L)if(a<c+b){  c>>>=1;}else{  a-=c+b;  c=(c>>>1)+b;}
        return c+(c<a?1L:0L);
    }
    public static int Sqrt(int a,int bit){
        if(a<1||(a<<=bit&1)<0)return 0;  bit&=~1;
        int c,b=1<<30;  while(b>a)b>>>=2;  c=b;  a-=b;
        /*int c,b=a-1;  b|=b>>>1;  b|=b>>>2;  b|=b>>>4;  b|=b>>>8;  b|=b>>>16;  c=IsPower4(++b)?(b>>>=2):(b>>>=1);  a-=b;*/
        while((b>>>=2)!=0)if(a<c+b){  c>>>=1;}else{  a-=c+b;  c=(c>>>1)+b;}  c<<=bit;  a<<=bit;  b=1<<bit;
        while((b>>>=2)!=0)if(a<c+b){  c>>>=1;}else{  a-=c+b;  c=(c>>>1)+b;}
        return c+(c<a?1:0);
    }




    private static final long SqrtOf2_62=0x5A827999FCEF3242L;
    private static final long GetSqrtOf2(final int bit){  return SqrtOf2_62>>>(62-bit);}

    public static long DebugSqrtC(long a,int bit){
        if(a<1L||(a<<=bit&1)<0L)return 0L;  bit&=~1;
        long c,b=1L<<62;  while(b>a)b>>>=2;  c=b;  a-=b;
        //long c,b=a-1L;  b|=b>>>1;  b|=b>>>2;  b|=b>>>4;  b|=b>>>8;  b|=b>>>16;  a|=a>>>32;  c=IsPower4(++b)?(b>>>=2):(b>>>=1);  a-=b;
        while((b>>>=2)!=0L)if(a<c+b){  c>>>=1;}else{  a-=c+b;  c=(c>>>1)+b;}  c<<=bit;  a<<=bit;  b=1L<<bit;
        while((b>>>=2)!=0L)if(a<c+b){  c>>>=1;}else{  a-=c+b;  c=(c>>>1)+b;}
        return c+(c<a?1L:0L);

        /*if((bit&1)==1){
            final int prec=14;
            c*=(int)(GetSqrtOf2(prec));
            c=(c>>>(prec+1))+(1&(c>>>prec));
        }else{  c+=(a<c?0:1);}
        retrun c;*/
    }
    public static int log2(final int n){  int bit=0;  while((1L<<bit)<n)++bit;  return bit;}
}
