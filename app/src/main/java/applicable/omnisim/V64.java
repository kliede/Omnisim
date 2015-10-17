package applicable.omnisim;

public class V64{
    public static V64[] Array(final int e){  final V64[] arr=new V64[e];  for(int i=0;i!=e;++i)arr[i]=new V64();  return arr;}
    private static final Cycler C=new Cycler(5);
    private static final V64[] VArr=Array(C.end);

    public double X=0.0D,Y=0.0D,Z=0.0D;

    public V64(){  }
    public V64(final double x,final double y,final double z){  X=x;  Y=y;  Z=z;}
    public V64(final V64 a){  X=a.X;  Y=a.Y;  Z=a.Z;}
    public V64 Set(final double x,final double y,final double z){  X=x;  Y=y;  Z=z;  return this;}
    public V64 Set(final double a){  return Set(a,a,a);}
    public V64 Set(final    int a){  return Set(a,a,a);}
    public V64 Set(final   long a){  return Set(a,a,a);}
    public V64 Set(final V64 a){  return a==null?Set(0.0D,0.0D,0.0D):Set(a.X,a.Y,a.Z);}
    public V64 Set(){  return Set(0.0D,0.0D,0.0D);}

    public void AddFast(final V64 a){  X+=a.X;  Y+=a.Y;  Z+=a.Z;}

    public V64 Neg(){  return Set(-X,-Y,-Z);}
    public V64 Add(final V64 a){  return Set(X+a.X,Y+a.Y,Z+a.Z);}
    public V64 Sub(final V64 a){  return Set(X-a.X,Y-a.Y,Z-a.Z);}
    public V64 Mul(final V64 a){  return Set(X*a.X,Y*a.Y,Z*a.Z);}
    public V64 Div(final V64 a){  return Set(X/a.X,Y/a.Y,Z/a.Z);}
    public V64 Add(final double a){  return Set(X+a,Y+a,Z+a);}
    public V64 Sub(final double a){  return Set(X-a,Y-a,Z-a);}
    public V64 Mul(final double a){  return Set(X*a,Y*a,Z*a);}
    public V64 Div(final double a){  return Set(X/a,Y/a,Z/a);}
    public V64 Add(final double x,final double y,final double z){  return Set(X+x,Y+y,Z+z);}
    public V64 Cross(final V64 a){  return Set((Y*a.Z)-(Z*a.Y),(Z*a.X)-(X*a.Z),(X*a.Y)-(Y*a.X));}
    public V64 GetCross(final V64 a){  return Copy().Cross(a);}
    public V64 Norm(){
        double fMag=Mag();
		if(fMag==0.0D){  Set(0.0D,0.0D,1.0D);}else
		if(fMag!=1.0D){  fMag=1.0D/Hack.Sqrt(fMag);  Set(X*fMag,Y*fMag,Z*fMag);}
		return this;
    }
    public V64 Swap(final V64 a){
        double f;
        f=X;  X=a.X;  a.X=f;
        f=Y;  Y=a.Y;  a.Y=f;
        f=Z;  Z=a.Z;  a.Z=f;
        return this;
    }
    public boolean Equals(final V64 a){  return (X==a.X&&Y==a.Y&&Z==a.Z);}
    public double Dot(final V64 a){  return (X*a.X)+(Y*a.Y)+(Z*a.Z);}
    public double Mag(){  return (X*X)+(Y*Y)+(Z*Z);}
    public double Sqrt(){  return Hack.Sqrt(Mag());}
    public V64 GetOrtho(final V64 vU){  return Get().Set(vU).Sub(Get().Set(this).Mul(Dot(vU))).Norm();}
    public V64 GetReflect(final V64 vN){
        return Get().Set(this).Sub(Get().Set(vN).Mul(2.0D*vN.Dot(this)));
    }
    public V64 GetABS(){  return Get().Set(Hack.ABS(X), Hack.ABS(Y), Hack.ABS(Z));}
    public V64 GetFloor(){  return Get().Set(Math.floor(X),Math.floor(Y),Math.floor(Z));}
    public V64 GetRound(){  return Get().Set(Hack.Round64(X), Hack.Round64(Y), Hack.Round64(Z));}
	public int Scale(final double fScale){
		if(X==0.0D&&Y==0.0D&&Z==0.0D)return 0;
		final double x=(X<0.0D)?-X:X;
		final double y=(Y<0.0D)?-Y:Y;
		final double z=(Z<0.0D)?-Z:Z;
		int i;  if(x>y){  i=(x>z)?0:2;}else{  i=(y>z)?1:2;}
		if(i==0){  X=X<0?-fScale:fScale;  Y*=fScale/x;  Z*=fScale/x;}else
		if(i==1){  Y=Y<0?-fScale:fScale;  X*=fScale/y;  Z*=fScale/y;}else
		if(i==2){  Z=Z<0?-fScale:fScale;  Y*=fScale/z;  X*=fScale/z;}
		return i;
	}
    public V64 Mul(final Q64 a){
        final double x1=(a.Y*Z)-(a.Z*Y);
        final double y1=(a.Z*X)-(a.X*Z);
        final double z1=(a.X*Y)-(a.Y*X);
        return Set(
                (((a.W*x1)+(a.Y*z1)-(a.Z*y1))*2.0D)+X,
                (((a.W*y1)+(a.Z*x1)-(a.X*z1))*2.0D)+Y,
                (((a.W*z1)+(a.X*y1)-(a.Y*x1))*2.0D)+Z
        );
    }

    public static V64 Get(){  return VArr[C.A()];}
    public static V64 Get(final double x,final double y,final double z){  return Get().Set(x,y,z);}
    public V64 Copy(){  return Get().Set(this);}

    public boolean HasNaN(){  return Double.isNaN(X)||Double.isNaN(Y)||Double.isNaN(Z);}

    public String Print(){  return Hack.SpaceF64(X)+" "+Hack.SpaceF64(Y)+" "+Hack.SpaceF64(Z);}

    public void EulerUnit(){
        double div=Hack.ABS(X);  if(div<Hack.ABS(Y))div=Hack.ABS(Y);  if(div<Hack.ABS(Z))div=Hack.ABS(Z);
        Div(div);
    }
}
