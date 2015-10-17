package applicable.omnisim;

public class Button {
    public  static final int
            FlagCircle=1<<0,
            FlagSquare=1<<1,
            FlagRanged=1<<2,
            FlagVector=1<<3,
            FlagClipXY=1<<4,
            FlagUnitXY=1<<5;

    private final double XA1,XB1,YA1,YB1;
    private final int Flags;
    private double Seconds=0.0D;
    private double XA,XB,YA,YB,XM,YM;
    private double RollLast=0.0D,Range=0.0D,Radius=0.0D;
    private int Clicked=0;

    public  final Trig TrigA=new Trig();
    public  final Trig TrigB=new Trig();
    public  final V64 V64A=new V64(0,0,1),V64B=new V64(0,0,1);
    public  double X=0.0D,Y=0.0D,Roll=0.0D,XLoc=0.0D,YLoc=0.0D,Zoom=0.0D,ZoomLast=0.0D;
    public int Dragged=0;

    public boolean Flagged(final int flag){  return 0!=(flag&Flags);}
    public Button(final double xB, final double xA, final double yB, final double yA, final int flags){
        XA1= Hack.Clamp(1.0D, xA, 0.0D);
        XB1= Hack.Clamp(1.0D, xB, 0.0D);
        YA1= Hack.Clamp(1.0D, yA, 0.0D);
        YB1= Hack.Clamp(1.0D, yB, 0.0D);
        Flags=flags;

        XA=XA1*Controls.XM;  YA=YA1*Controls.YM;
        XB=XB1*Controls.XM;  YB=YB1*Controls.YM;
        XM=0.5D*(XA+XB);
        YM=0.5D*(YA+YB);
        Radius=XB-XM<YB-YM?XB-XM:YB-YM;
        Range=(Controls.XD>Controls.YD?Controls.XD:Controls.YD)>>>3;

        TrigA.SetA(XM,YM);  TrigB.SetA(XM,YM);
        TrigA.SetB(XM,YM);  TrigB.SetB(XM,YM);
        TrigA.SetL(XM,YM);  TrigB.SetL(XM,YM);

    }
    public void Bgn(final double x,final double y,final boolean firstTouch){
        final Trig trig=firstTouch?TrigA:TrigB;
        trig.SetA(x,y);
        trig.SetB(x,y);
        trig.SetL(x,y);
        trig.Active=true;
        if(TrigA.Active!=TrigB.Active){
            Clicked=Clock.Seconds-Seconds<0.25D?-2:-1;
            Seconds=Clock.Seconds;
            Dragged=Clicked;
        }else{
            RollLast=Hack.Atan2(TrigB.YB - TrigA.YB, TrigB.XB - TrigA.XB);
            V64A.Set(TrigA.XB,TrigA.YB,Controls.XD);
            V64B.Set(TrigB.XB,TrigB.YB,Controls.XD);  V64B.Sub(V64A);
            ZoomLast=Zoom=Hack.Sqrt(V64B.Mag());
        }

    }
    public void Now(final double x,final double y,final boolean firstTouch){
        final Trig trig=firstTouch?TrigA:TrigB;
        trig.SetB(x,y);
        trig.Run(Range,Range);
        V64A.Set(TrigA.XB,TrigA.YB,Controls.XD);
        V64B.Set(TrigB.XB,TrigB.YB,Controls.XD);  V64B.Sub(V64A);
        Zoom=Hack.Sqrt(V64B.Mag());
        V64B.Set(trig.XA-trig.XA,trig.YB-trig.YA,0.0D);
        final double range=Hack.Sqrt(V64B.Mag());
        if(range>7.0D)Seconds=0.0D;
    }
    public void End(final boolean firstTouch){
        final Trig trig=firstTouch?TrigA:TrigB;
        trig.SetL(trig.XB,trig.YB);
        trig.Active=false;
        if(TrigA.Active==TrigB.Active)Dragged=0;
        if(Clock.Seconds-Seconds<0.25D)Seconds=Clock.Seconds;
    }

    public void Update(){
        if(Flagged(FlagClipXY)){
            X=(TrigA.XB-TrigA.XL)+(TrigB.XB-TrigB.XL);
            Y=(TrigA.YB-TrigA.YL)+(TrigB.YB-TrigB.YL);
        }else{
            X=(TrigA.XB-TrigA.XA)+(TrigB.XB-TrigB.XA);
            Y=(TrigA.YB-TrigA.YA)+(TrigB.YB-TrigB.YA);
        }

        if(Dragged==-2){
            XLoc=Hack.Clamp(1.0D,XLoc+(X/Controls.XD),0.0D);
            YLoc=Hack.Clamp(1.0D,YLoc+(Y/Controls.YD),0.0D);
        }

        if(TrigA.Active)TrigA.SetL(TrigA.XB,TrigA.YB);
        if(TrigB.Active)TrigB.SetL(TrigB.XB,TrigB.YB);
        if(TrigA.Active&&TrigB.Active){
            final double roll= Hack.Atan2(TrigB.YB - TrigA.YB, TrigB.XB - TrigA.XB);
            Roll=roll-RollLast;
            RollLast=roll;
        }else{  Roll=0.0D;}
        if(Flagged(FlagUnitXY)){  X=Hack.Clamp(1.0D,X/Range,-1.0D);  Y=Hack.Clamp(1.0D,Y/Range,-1.0D);}

    }
    public V64 GetV64(){
        V64A.X=(TrigA.XB-XM)*1.125D;
        V64A.Y=(TrigA.YB-YM)*1.125D;
        V64A.Z=(V64A.X*V64A.X)+(V64A.Y*V64A.Y);
        V64A.Z=V64A.Z< Hack.F64Minimum? Hack.F64Minimum: Hack.Sqrt(V64A.Z);
        if(V64A.Z<Radius){
            V64A.Div(Radius);
            V64A.Z= Hack.Sqrt(1.0D - (V64A.Z * V64A.Z));
        }else{  V64A.Set(V64A.X/V64A.Z,V64A.Y/V64A.Z,0.0D);}
        return V64A.Mul(Controls.QO);
    }

    public boolean Covers(final double x,final double y){
        boolean covers=false;
        if(Flagged(FlagCircle))covers=(((x-XM)*(x-XM))+((y-YM)*(y-YM)))<(Radius*Radius);
        if(Flagged(FlagSquare))covers=(XA<=x&&x<=XB&&YA<=y&&y<=YB);
        return covers;
    }

    public int GetClicked(){
        int clicked=0;
        if(Clicked<0){
            if(Hack.ABS(X)>5||Hack.ABS(Y)>5)Clicked=0;
            if(Clock.Seconds-Seconds>=0.25D||!(TrigA.Active||TrigB.Active)){  clicked=-Clicked;  Clicked=0;}
        }
        return clicked;
    }
}
