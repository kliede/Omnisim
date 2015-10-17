package applicable.omnisim;

//import applicable.a004.Native;

public class Q64 {
    public static Q64[] Array(final int e){  final Q64[] arr=new Q64[e];  for(int i=0;i!=e;++i)arr[i]=new Q64();  return arr;}

    private static final Cycler C=new Cycler(5);
    private static final Q64[] QArr=Array(C.end);
    private static final V64[] VArr=V64.Array(C.end);
    public  static final V64 VAxisX=new V64(1,0,0);
    public  static final V64 VAxisY=new V64(0,1,0);
    public  static final V64 VAxisZ=new V64(0,0,1);

    public double X=0.0D,Y=0.0D,Z=0.0D,W=1.0D;

    public Q64 Set(final double x,final double y,final double z,final double w){  X=x;  Y=y;  Z=z;  W=w;  return this;}
    public Q64 Set(){  return Set(0.0D,0.0D,0.0D,1.0D);}
    public Q64 Set(final Q64 a){  return Set(a.X,a.Y,a.Z,a.W);}
	public Q64 Set(final V64 vZ,final V64 vU){
		final V64 vY=vZ.GetOrtho(vU);
		final V64 vX=vY.GetCross(vZ);
		final double fW=0.5D* Hack.Sqrt(1.0D + vX.X + vY.Y + vZ.Z);
		final double fA=0.25D/fW;
		return Set((vY.Z-vZ.Y)*fA,(vZ.X-vX.Z)*fA,(vX.Y-vY.X)*fA,fW);
	}
	public Q64 Set(final V64 vO,final double fAngle){
		final double sin=Math.sin(fAngle*0.5D);
		final double cos=Math.cos(fAngle*0.5D);
		return Set(sin*vO.X,sin*vO.Y,sin*vO.Z,cos);
	}
	public Q64 Set(final int a){
		return Set(
			(((double)((a>>>24)&0xFF)/255.0D)-0.5D)*2.0D,
			(((double)((a>>>16)&0xFF)/255.0D)-0.5D)*2.0D,
			(((double)((a>>> 8)&0xFF)/255.0D)-0.5D)*2.0D,
			(((double)((a     )&0xFF)/255.0D)-0.5D)*2.0D
		).Norm();
	}
	public int GetI32(){
		return  (Hack.Clamp(255, Hack.Round32(255.0D * (0.5D + (0.5D * X))), 0)<<24)|
			    (Hack.Clamp(255, Hack.Round32(255.0D * (0.5D + (0.5D * Y))), 0)<<16)|
			    (Hack.Clamp(255, Hack.Round32(255.0D * (0.5D + (0.5D * Z))), 0)<< 8)|
			    (Hack.Clamp(255, Hack.Round32(255.0D * (0.5D + (0.5D * W))), 0)    );
	}
    public Q64(){}
    public Q64(final Q64 a){  Set(a.X,a.Y,a.Z,a.W);}
    public Q64(final double x, final double y, final double z, final double w){  Set(x,y,z,w);}

    public Q64 Conj(){  return Set(-X,-Y,-Z,W);}
    public double Dot(final Q64 a){  return (X*a.X)+(Y*a.Y)+(Z*a.Z)+(W*a.W);}
    public Q64 Mul(final double a){  return Set(X*a,Y*a,Z*a,W*a);}
    public Q64 Mul(final Q64 a){
        return Set(
                (W*a.X)+(X*a.W)+(Y*a.Z)-(Z*a.Y),
                (W*a.Y)-(X*a.Z)+(Y*a.W)+(Z*a.X),
                (W*a.Z)+(X*a.Y)-(Y*a.X)+(Z*a.W),
                (W*a.W)-(X*a.X)-(Y*a.Y)-(Z*a.Z)
        );
    }
    public Q64 Sub(final Q64 a){  return Conj().Mul(a);}
    public boolean Equals(final Q64 a){  return (X==a.X&&Y==a.Y&&Z==a.Z&&W==a.W);}
    public V64 XAxis(){  return V64.Get(((W*W)+(X*X)-(Y*Y)-(Z*Z)),(2.0D*X*Y)+(2.0D*W*Z),(2.0D*X*Z)-(2.0D*W*Y));}
    public V64 YAxis(){  return V64.Get((2.0D*X*Y)-(2.0D*W*Z),((W*W)-(X*X)+(Y*Y)-(Z*Z)),(2.0D*Y*Z)+(2.0D*W*X));}
    public V64 ZAxis(){  return V64.Get((2.0D*X*Z)+(2.0D*W*Y),(2.0D*Y*Z)-(2.0D*W*X),((W*W)-(X*X)-(Y*Y)+(Z*Z)));}
    private static final Q64 Q64C=new Q64(),Q64B=new Q64(),Q64A=new Q64();
	public Q64 PYR(final V64 vA){
		final double y=Hack.Sqrt((vA.X*vA.X)+(vA.Y*vA.Y));
		final double z=Hack.Atan2(vA.X,vA.Y);
        Q64C.Set(VAxisY,y);
		Q64B.Set(VAxisZ,z).Mul(Q64C);
		Q64A.Set(VAxisZ,vA.Z-z);
		return Mul(Q64B.Mul(Q64A));
	}
    public double Mag(){  return (X*X)+(Y*Y)+(Z*Z)+(W*W);}
    public Q64 Norm(){
        final double fMag=(X*X)+(Y*Y)+(Z*Z)+(W*W);
		if(fMag==0.0D){  Set();}else if(fMag!=1.0D){  Mul(1.0D/ Hack.Sqrt(fMag));}
		return this;
    }

    public V64 Mul(final V64 vA){
        final double x1=(Y*vA.Z)-(Z*vA.Y);
        final double y1=(Z*vA.X)-(X*vA.Z);
        final double z1=(X*vA.Y)-(Y*vA.X);
        vA.X+=((W*x1)+(Y*z1)-(Z*y1))*2.0D;
        vA.Y+=((W*y1)+(Z*x1)-(X*z1))*2.0D;
        vA.Z+=((W*z1)+(X*y1)-(Y*x1))*2.0D;
        return vA;
    }
    public double[] SetMatrix(final double[] m){
        m[0]=1.0D-(2.0D*Y*Y)-(2.0D*Z*Z);m[1]=2.0D*((X*Y)-(Z*W));        m[2]=2.0D*((X*Z)+(Y*W));
        m[3]=2.0D*((X*Y)+(Z*W));        m[4]=1.0D-(2.0D*X*X)-(2.0D*Z*Z);m[5]=2.0D*((Y*Z)-(X*W));
        m[6]=2.0D*((X*Z)-(Y*W));        m[7]=2.0D*((Y*Z)+(X*W));        m[8]=1.0D-(2.0D*X*X)-(2.0D*Y*Y);
        return m;
    }

    public static Q64 Get(){  return QArr[C.A()];}
    public Q64 Copy(){  return Get().Set(this);}

    public String Print(){  return Hack.SpaceF64(X)+" "+ Hack.SpaceF64(Y)+" "+ Hack.SpaceF64(Z)+" "+ Hack.SpaceF64(W);}
}
