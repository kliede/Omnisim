package applicable.omnisim;

public class Trig{
    public double XA,YA,XB,YB,XL,YL;
    public double X,Y,R,C,S;
    public boolean Active=false;

    public Trig(){  Init();}

    public void Init(){
        XA=0.0D;  YA=0.0D;  XB=0.0D;  YB=0.0D;  XL=0.0D;  YL=0.0D;
        X =0.0D;  Y =0.0D;  R =0.0D;  C =0.0D;  S =0.0D;
        Active=false;
    }
    public void Set(final Trig trig){
        XA=trig.XA;  YA=trig.YA;  XB=trig.XB;  YB=trig.YB;
        X =trig.X ;  Y =trig.Y ;  R =trig.R ;  C =trig.C ;  S =trig.S ;
        Active=trig.Active;
    }
    public void SetA(final double x,final double y){  XA=x;  YA=y;}
    public void SetB(final double x,final double y){  XB=x;  YB=y;}
    public void SetL(final double x,final double y){  XL=x;  YL=y;}

    public void Run(){
        X=XB-XA;
        Y=YB-YA;
        if((R= Hack.Sqrt((X * X) + (Y * Y)))< Hack.F64Minimum)R= Hack.F64Minimum;
        C=X/R;
        S=Y/R;
    }
    public void Run(final double range,final double focus){
        Run();
        if(R>range){
            X=XB-(XA=XB-(C*range));
            Y=YB-(YA=YB-(S*range));
            R= Hack.Sqrt((X * X) + (Y * Y));
        }
        X/=focus;
        Y/=focus;
        R/=focus;
    }
}
